package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.dao.InventarioDeclaracionDao;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.EstadoInventarioDeclaracion;
import ec.gob.dinardap.remanente.modelo.InventarioDeclaracion;
import ec.gob.dinardap.remanente.servicio.InventarioDeclaracionServicio;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.sftp.exception.FtpException;
import ec.gob.dinardap.sftp.util.CredencialesSFTP;
import ec.gob.dinardap.sftp.util.GestionSFTP;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "InventarioDeclaracionServicio")
public class InventarioDeclaracionServicioImpl extends GenericServiceImpl<InventarioDeclaracion, Integer> implements InventarioDeclaracionServicio {

    @EJB
    private InventarioDeclaracionDao inventarioDeclaracionDao;
    @EJB
    private ParametroServicio parametroServicio;

    @Override
    public GenericDao<InventarioDeclaracion, Integer> getDao() {
        return inventarioDeclaracionDao;
    }

    @Override
    public InventarioDeclaracion getInventarioPorInstitucionAño(Integer institucionId, Integer año) {
        InventarioDeclaracion inventarioDeclaracion = new InventarioDeclaracion();
        String[] criteriaNombres = {"anio", "institucion.institucionId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {año, institucionId};
        String[] orderBy = {"inventarioDeclaracionId"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        if (!findByCriterias(criteria).isEmpty()) {
            inventarioDeclaracion = findByCriterias(criteria).get(findByCriterias(criteria).size() - 1);
            for (EstadoInventarioDeclaracion eid : inventarioDeclaracion.getEstadoInventarioDeclaracionList()) {
                eid.getEstadoInventarioDeclaracionId();
            }
        }

        return inventarioDeclaracion;
    }

    @Override
    public void editInventarioDeclaracion(InventarioDeclaracion inventarioDeclaracion, SftpDto sftpDto) {
        sftpDto.setCredencialesSFTP(setCredencialesSftp(sftpDto.getCredencialesSFTP()));
        this.update(inventarioDeclaracion);
        guardarArchivo(sftpDto);
    }
    
    @Override
    public byte[] descargarArchivo(SftpDto sftpDto) {
        try {
            sftpDto.setCredencialesSFTP(setCredencialesSftp(sftpDto.getCredencialesSFTP()));
            return GestionSFTP.descargarArchivo(sftpDto.getCredencialesSFTP());
        } catch (FtpException ex) {
            Logger.getLogger(InventarioDeclaracionServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private void guardarArchivo(SftpDto sftpDto) {
        try {
            GestionSFTP.subirArchivo(sftpDto.getArchivo(), sftpDto.getCredencialesSFTP());
        } catch (FtpException ex) {
            Logger.getLogger(InventarioDeclaracionServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private CredencialesSFTP setCredencialesSftp(CredencialesSFTP credencialesSFTP) {
        credencialesSFTP.setHost(parametroServicio.findByPk(ParametroEnum.SERVIDOR_SFTP.name()).getValor());
        credencialesSFTP.setPuerto(Integer.parseInt(parametroServicio.findByPk(ParametroEnum.PUERTO_SFTP.name()).getValor()));
        credencialesSFTP.setUsuario(parametroServicio.findByPk(ParametroEnum.SFTP_USUARIO_REMANENTE.name()).getValor());
        credencialesSFTP.setContrasena(parametroServicio.findByPk(ParametroEnum.SFTP_CONTRASENA_REMANENTE.name()).getValor());
        return credencialesSFTP;
    }

}

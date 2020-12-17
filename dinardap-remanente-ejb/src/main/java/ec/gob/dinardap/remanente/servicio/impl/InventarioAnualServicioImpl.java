package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.dao.InventarioAnualDao;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.EstadoInventarioAnual;
import ec.gob.dinardap.remanente.modelo.InventarioAnual;
import ec.gob.dinardap.remanente.servicio.InventarioAnualServicio;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.sftp.exception.FtpException;
import ec.gob.dinardap.sftp.util.CredencialesSFTP;
import ec.gob.dinardap.sftp.util.GestionSFTP;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "InventarioAnualServicio")
public class InventarioAnualServicioImpl extends GenericServiceImpl<InventarioAnual, Integer> implements InventarioAnualServicio {

    @EJB
    private InventarioAnualDao inventarioAnualDao;
    @EJB
    private ParametroServicio parametroServicio;

    @Override
    public GenericDao<InventarioAnual, Integer> getDao() {
        return inventarioAnualDao;
    }

    @Override
    public InventarioAnual getInventarioPorInstitucionAño(Integer institucionId, Integer año) {
        InventarioAnual inventarioAnual = new InventarioAnual();
        String[] criteriaNombres = {"anio", "institucion.institucionId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {año, institucionId};
        String[] orderBy = {"inventarioAnualId"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        if (!findByCriterias(criteria).isEmpty()) {
            inventarioAnual = findByCriterias(criteria).get(findByCriterias(criteria).size() - 1);
            for (EstadoInventarioAnual eia : inventarioAnual.getEstadoInventarioAnualList()) {
                eia.getEstadoInventarioAnualId();
            }
        }

        return inventarioAnual;
    }

    @Override
    public void editInventarioAnual(InventarioAnual inventarioAnual, SftpDto sftpDto) {
        sftpDto.setCredencialesSFTP(setCredencialesSftp(sftpDto.getCredencialesSFTP()));
        this.update(inventarioAnual);
        guardarArchivo(sftpDto);
    }
    
    @Override
    public byte[] descargarArchivo(SftpDto sftpDto) {
        try {
            sftpDto.setCredencialesSFTP(setCredencialesSftp(sftpDto.getCredencialesSFTP()));
            return GestionSFTP.descargarArchivo(sftpDto.getCredencialesSFTP());
        } catch (FtpException ex) {
            Logger.getLogger(InventarioAnualServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private void guardarArchivo(SftpDto sftpDto) {
        try {
            GestionSFTP.subirArchivo(sftpDto.getArchivo(), sftpDto.getCredencialesSFTP());
        } catch (FtpException ex) {
            Logger.getLogger(InventarioAnualServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
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

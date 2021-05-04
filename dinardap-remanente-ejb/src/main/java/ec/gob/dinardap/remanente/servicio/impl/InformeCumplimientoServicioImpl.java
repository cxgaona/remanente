package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.dao.InformeCumplimientoDao;
import ec.gob.dinardap.remanente.dao.InventarioDeclaracionDao;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.EstadoInventarioDeclaracion;
import ec.gob.dinardap.remanente.modelo.InformeCumplimiento;
import ec.gob.dinardap.remanente.modelo.InventarioDeclaracion;
import ec.gob.dinardap.remanente.servicio.InformeCumplimientoServicio;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.sftp.exception.FtpException;
import ec.gob.dinardap.sftp.util.CredencialesSFTP;
import ec.gob.dinardap.sftp.util.GestionSFTP;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "InformeCumplimientoServicio")
public class InformeCumplimientoServicioImpl extends GenericServiceImpl<InformeCumplimiento, Integer> implements InformeCumplimientoServicio {

    @EJB
    private InformeCumplimientoDao informeCumplimientoDao;
    @EJB
    private ParametroServicio parametroServicio;

    @Override
    public GenericDao<InformeCumplimiento, Integer> getDao() {
        return informeCumplimientoDao;
    }

    @Override
    public List<InformeCumplimiento> getInformesCumplimientoPorInstitucionAñoTipo(Integer institucionId, Integer año, String tipo) {
        List<InformeCumplimiento> informeCumplimientoList = new ArrayList<InformeCumplimiento>();
        String[] criteriaNombres = {"anio", "institucion.institucionId", "tipo"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS,CriteriaTypeEnum.STRING_EQUALS};
        Object[] criteriaValores = {año, institucionId, tipo};
        String[] orderBy = {"informeCumplimientoId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        informeCumplimientoList = findByCriterias(criteria); 
        /*for(InformeCumplimiento informeCumplimiento:informeCumplimientoList){
            for(Tomo tomo:libro.getTomoList()){
                tomo.getTomoId();
            }
        }*/
        return informeCumplimientoList;
    }

    @Override
    public void editInformeCumplimiento(InformeCumplimiento informeCumplimiento, SftpDto sftpDto) {
        sftpDto.setCredencialesSFTP(setCredencialesSftp(sftpDto.getCredencialesSFTP()));
        this.update(informeCumplimiento);
        guardarArchivo(sftpDto);
    }
    
    @Override
    public byte[] descargarArchivo(SftpDto sftpDto) {
        try {
            sftpDto.setCredencialesSFTP(setCredencialesSftp(sftpDto.getCredencialesSFTP()));
            return GestionSFTP.descargarArchivo(sftpDto.getCredencialesSFTP());
        } catch (FtpException ex) {
            Logger.getLogger(InformeCumplimientoServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private void guardarArchivo(SftpDto sftpDto) {
        try {
            GestionSFTP.subirArchivo(sftpDto.getArchivo(), sftpDto.getCredencialesSFTP());
        } catch (FtpException ex) {
            Logger.getLogger(InformeCumplimientoServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
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

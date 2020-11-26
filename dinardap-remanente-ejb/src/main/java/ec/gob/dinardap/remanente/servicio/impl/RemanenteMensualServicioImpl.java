package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.dao.RemanenteMensualDao;
import ec.gob.dinardap.remanente.dto.RemanenteMensualDTO;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteMensual;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Tramite;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.sftp.exception.FtpException;
import ec.gob.dinardap.sftp.util.CredencialesSFTP;
import ec.gob.dinardap.sftp.util.GestionSFTP;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.SerializationUtils;


@Stateless(name = "RemanenteMensualServicio")
public class RemanenteMensualServicioImpl extends GenericServiceImpl<RemanenteMensual, Integer> implements RemanenteMensualServicio {

    @EJB
    private RemanenteMensualDao remanenteMensualDao;

    @EJB
    private ParametroServicio parametroServicio;

    @Override
    public GenericDao<RemanenteMensual, Integer> getDao() {
        return remanenteMensualDao;
    }

    @Override
    public List<RemanenteMensualDTO> getRemanenteMensualByInstitucion(Integer institucionID, Integer año) {
        List<RemanenteMensual> remanenteMensualList = new ArrayList<RemanenteMensual>();
        List<RemanenteMensualDTO> remanenteMensualDTOList = new ArrayList<RemanenteMensualDTO>();
        String[] criteriaNombres = {"remanenteCuatrimestral.remanenteAnual.institucion.institucionId",
            "remanenteCuatrimestral.remanenteAnual.anio"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {institucionID, año};
        String[] orderBy = {"mes"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        remanenteMensualList = findByCriterias(criteria);
        for (RemanenteMensual rm : remanenteMensualList) {
            Collections.sort(rm.getEstadoRemanenteMensualList(), new Comparator<EstadoRemanenteMensual>() {
                @Override
                public int compare(EstadoRemanenteMensual erm1, EstadoRemanenteMensual erm2) {
                    return new Integer(erm1.getEstadoRemanenteMensualId()).compareTo(new Integer(erm2.getEstadoRemanenteMensualId()));
                }
            });
            for (EstadoRemanenteMensual erm : rm.getEstadoRemanenteMensualList()) {
                erm.getEstadoRemanenteMensualId();
            }

            Collections.sort(rm.getRemanenteCuatrimestral().getEstadoRemanenteCuatrimestralList(), new Comparator<EstadoRemanenteCuatrimestral>() {
                @Override
                public int compare(EstadoRemanenteCuatrimestral erm1, EstadoRemanenteCuatrimestral erm2) {
                    return new Integer(erm1.getEstadoRemanenteCuatrimestralId()).compareTo(new Integer(erm2.getEstadoRemanenteCuatrimestralId()));
                }
            });
            for (EstadoRemanenteCuatrimestral erc : rm.getRemanenteCuatrimestral().getEstadoRemanenteCuatrimestralList()) {
                erc.getEstadoRemanenteCuatrimestralId();
            }

            rm.getRemanenteCuatrimestral();
            rm.getRemanenteCuatrimestral().getRemanenteAnual();

            for (Transaccion transaccion : rm.getTransaccionList()) {
                transaccion.getTransaccionId();
                for (Tramite tramite : transaccion.getTramiteList()) {
                    tramite.getTramiteId();
                }
            }

        }
        for (RemanenteMensual rm : remanenteMensualList) {
            remanenteMensualDTOList.add(new RemanenteMensualDTO(rm));
        }
        return remanenteMensualDTOList;
    }

    @Override
    public List<RemanenteMensual> getRemanenteMensualProrroga() {
        List<RemanenteMensual> remanenteMensualList = new ArrayList<RemanenteMensual>();
        String[] criteriaNombres = {"prorrogaRemanenteMensualId.prorrogaRemanenteMensualId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_IS_NOT_NULL};
        Object[] criteriaValores = {};
        String[] orderBy = {"remanenteMensualId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        remanenteMensualList = findByCriterias(criteria);
//        for (RemanenteMensual rm : remanenteMensualList) {
//            rm.getProrrogaRemanenteMensualId();
//        }
        return remanenteMensualList;
    }

    @Override
    public void editRemanenteMensual(RemanenteMensual remanenteMensual) {
        this.update(remanenteMensual);
    }

    @Override
    public void editRemanenteMensual(RemanenteMensual remanenteMensual, SftpDto sftpDto) {
        sftpDto.setCredencialesSFTP(setCredencialesSftp(sftpDto.getCredencialesSFTP()));
        this.update(remanenteMensual);
        guardarArchivo(sftpDto);
    }

    @Override
    public RemanenteMensual getUltimoRemanenteMensual(Integer idInstitucion, Integer anio, Integer mes) {
        List<RemanenteMensual> remanenteMensualList = new ArrayList<RemanenteMensual>();
        String[] criteriaNombres = {"remanenteCuatrimestral.remanenteAnual.institucion.institucionId",
            "remanenteCuatrimestral.remanenteAnual.anio",
            "mes"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {idInstitucion, anio, mes};
        String[] orderBy = {"remanenteMensualId"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        remanenteMensualList = findByCriterias(criteria);
        for (RemanenteMensual rm : remanenteMensualList) {
            Collections.sort(rm.getEstadoRemanenteMensualList(), new Comparator<EstadoRemanenteMensual>() {
                @Override
                public int compare(EstadoRemanenteMensual erm1, EstadoRemanenteMensual erm2) {
                    return new Integer(erm1.getEstadoRemanenteMensualId()).compareTo(new Integer(erm2.getEstadoRemanenteMensualId()));
                }
            });
            for (EstadoRemanenteMensual erm : rm.getEstadoRemanenteMensualList()) {
                erm.getEstadoRemanenteMensualId();
            }
            for (Transaccion transaccion : rm.getTransaccionList()) {
                transaccion.getTransaccionId();
            }
        }
        RemanenteMensual remanenteMensual = new RemanenteMensual();
        if (!remanenteMensualList.isEmpty()) {
            remanenteMensual = remanenteMensualList.get(0);
        }
        return remanenteMensual;
    }

    @Override
    public void crearVersionRemanenteMensual(RemanenteMensual remanenteMensualOrigen) {
        RemanenteMensual remanenteMensualNuevaVersion = new RemanenteMensual();        
        remanenteMensualNuevaVersion = (RemanenteMensual) SerializationUtils.clone(remanenteMensualOrigen);      
        remanenteMensualNuevaVersion.setRemanenteMensualOrigen(remanenteMensualOrigen);
        remanenteMensualNuevaVersion.setRemanenteMensualId(null);
        remanenteMensualNuevaVersion.setComentarios(null);
        remanenteMensualNuevaVersion.setSolicitudCambioUrl(null);
        remanenteMensualNuevaVersion.setInformeAprobacionUrl(null);
        remanenteMensualNuevaVersion.setFechaRegistro(new Date());
        create(remanenteMensualNuevaVersion);
        //        rmo.setProrrogaRemanenteMensualId(null);
    }

    @Override
    public RemanenteMensual obtenerVersionRemanenteMensual(Integer remanenteMensualOrigen) {
        List<RemanenteMensual> remanenteMensualList = new ArrayList<RemanenteMensual>();
        String[] criteriaNombres = {"remanenteMensualOrigenId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {remanenteMensualOrigen};
        String[] orderBy = {"remanenteMensualId"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        remanenteMensualList = findByCriterias(criteria);
        return remanenteMensualList.get(remanenteMensualList.size() - 1);
    }

    private void guardarArchivo(SftpDto sftpDto) {
        try {
            GestionSFTP.subirArchivo(sftpDto.getArchivo(), sftpDto.getCredencialesSFTP());
        } catch (FtpException ex) {
            Logger.getLogger(RemanenteMensualServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private CredencialesSFTP setCredencialesSftp(CredencialesSFTP credencialesSFTP) {
        credencialesSFTP.setHost(parametroServicio.findByPk(ParametroEnum.SERVIDOR_SFTP.name()).getValor());
        credencialesSFTP.setPuerto(Integer.parseInt(parametroServicio.findByPk(ParametroEnum.PUERTO_SFTP.name()).getValor()));
        credencialesSFTP.setUsuario(parametroServicio.findByPk(ParametroEnum.SFTP_USUARIO_REMANENTE.name()).getValor());
        credencialesSFTP.setContrasena(parametroServicio.findByPk(ParametroEnum.SFTP_CONTRASENA_REMANENTE.name()).getValor());
        return credencialesSFTP;
    }

    @Override
    public byte[] descargarArchivo(SftpDto sftpDto) {
        try {
            sftpDto.setCredencialesSFTP(setCredencialesSftp(sftpDto.getCredencialesSFTP()));
            return GestionSFTP.descargarArchivo(sftpDto.getCredencialesSFTP());
        } catch (FtpException ex) {
            Logger.getLogger(RemanenteMensualServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}

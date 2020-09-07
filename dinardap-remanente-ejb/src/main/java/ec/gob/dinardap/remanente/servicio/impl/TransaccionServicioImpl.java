package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.dao.TransaccionDao;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.Tramite;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.sftp.exception.FtpException;
import ec.gob.dinardap.sftp.util.CredencialesSFTP;
import ec.gob.dinardap.sftp.util.GestionSFTP;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "TransaccionServicio")
public class TransaccionServicioImpl extends GenericServiceImpl<Transaccion, Integer> implements TransaccionServicio {

    @EJB
    private TransaccionDao transaccionDao;

    @EJB
    private ParametroServicio parametroServicio;

    @Override
    public GenericDao<Transaccion, Integer> getDao() {
        return transaccionDao;
    }

    @Override
    public void editTransaccion(Transaccion transaccion) {
        this.update(transaccion);
    }

    @Override
    public void editTransaccion(Transaccion transaccion, SftpDto sftpDto) {
        sftpDto.setCredencialesSFTP(setCredencialesSftp(sftpDto.getCredencialesSFTP()));
        this.update(transaccion);
        guardarArchivo(sftpDto);
    }

    @Override
    public List<Transaccion> getTransaccionByInstitucionAñoMes(Integer institucionId, Integer año, Integer mes, Integer remanenteMensualID) {
        List<Transaccion> transaccionList = new ArrayList<Transaccion>();
        String[] criteriaNombres = {"remanenteMensualId.remanenteCuatrimestral.remanenteAnual.institucionRequerida.institucionId",
            "remanenteMensualId.remanenteCuatrimestral.remanenteAnual.anio",
            "remanenteMensualId.mes",
            "remanenteMensualId.remanenteMensualId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS,
            CriteriaTypeEnum.INTEGER_EQUALS,
            CriteriaTypeEnum.INTEGER_EQUALS,
            CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {institucionId, año, mes, remanenteMensualID};
        String[] orderBy = {"catalogoTransaccionId.catalogoTransaccionId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        transaccionList = findByCriterias(criteria);
        for (Transaccion transaccion : transaccionList) {
            for (Tramite tramite : transaccion.getTramiteList()) {
                tramite.getTramiteId();
            }
            for (Nomina nomina : transaccion.getNominaList()) {
                nomina.getNominaId();
            }
            for (FacturaPagada facturaPagada : transaccion.getFacturaPagadaList()) {
                facturaPagada.getFacturaPagadaId();
            }
        }
        return transaccionList;
    }

    @Override
    public List<Transaccion> getTransacciones(Integer remanenteMensualID) {        
        List<Transaccion> transaccionList = new ArrayList<Transaccion>();
        String[] criteriaNombres = {"remanenteMensualId.remanenteMensualId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {remanenteMensualID};
        String[] orderBy = {"catalogoTransaccionId.catalogoTransaccionId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        transaccionList = findByCriterias(criteria);
        for (Transaccion transaccion : transaccionList) {
            for (Tramite tramite : transaccion.getTramiteList()) {
                tramite.getTramiteId();
            }
            for (Nomina nomina : transaccion.getNominaList()) {
                nomina.getNominaId();
            }
            for (FacturaPagada facturaPagada : transaccion.getFacturaPagadaList()) {
                facturaPagada.getFacturaPagadaId();
            }
            transaccion.getCatalogoTransaccion().getCatalogoTransaccionId();            
        }
        return transaccionList;
    }

    @Override
    public Transaccion getTransaccionByInstitucionFechaTipo(Integer idInstitucion, Integer anio, Integer mes, Integer tipo) {
        Transaccion transaccion = new Transaccion();
        transaccion = transaccionDao.getTransaccionByInstitucionFechaTipo(idInstitucion, anio, mes, tipo);
        transaccion.getTramiteList();
        transaccion.getCatalogoTransaccion();
        return transaccion;
    }

    @Override
    public Transaccion crearTransaccion(Transaccion transaccion, SftpDto sftpDto) {
        this.create(transaccion);
        guardarArchivo(sftpDto);
        Transaccion t = new Transaccion();
        List<Transaccion> transaccionList = new ArrayList<Transaccion>();
        String[] criteriaNombres = {"remanenteMensualId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {transaccion.getRemanenteMensual().getRemanenteMensualId()};
        String[] orderBy = {"transaccionId"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        transaccionList = findByCriterias(criteria);
        for (Transaccion tAux : transaccionList) {
            tAux.getTransaccionId();
            tAux.getCatalogoTransaccion();
        }
        for (Transaccion tAux : transaccionList) {
            t = tAux;
            break;
        }
        return t;
    }

    @Override
    public byte[] descargarArchivo(SftpDto sftpDto) {
        try {
            sftpDto.setCredencialesSFTP(setCredencialesSftp(sftpDto.getCredencialesSFTP()));
            return GestionSFTP.descargarArchivo(sftpDto.getCredencialesSFTP());
        } catch (FtpException ex) {
            Logger.getLogger(TransaccionServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private void guardarArchivo(SftpDto sftpDto) {
        try {
            GestionSFTP.subirArchivo(sftpDto.getArchivo(), sftpDto.getCredencialesSFTP());
        } catch (FtpException ex) {
            Logger.getLogger(TransaccionServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private CredencialesSFTP setCredencialesSftp(CredencialesSFTP credencialesSFTP) {
        credencialesSFTP.setHost(parametroServicio.findByPk(ParametroEnum.SERVIDOR_SFTP.name()).getValor());
        credencialesSFTP.setPuerto(Integer.parseInt(parametroServicio.findByPk(ParametroEnum.PUERTO_SFTP.name()).getValor()));
        credencialesSFTP.setUsuario(parametroServicio.findByPk(ParametroEnum.USUARIO_REMANENTE_SFTP.name()).getValor());
        credencialesSFTP.setContrasena(parametroServicio.findByPk(ParametroEnum.CONTRASENA_REMANENTE_SFTP.name()).getValor());
        return credencialesSFTP;
    }
}

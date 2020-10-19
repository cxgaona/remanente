package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.persistence.util.DateBetween;
import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.dao.RemanenteCuatrimestralDao;
import ec.gob.dinardap.remanente.dto.SftpDto;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteAnual;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestralPK;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.servicio.RemanenteCuatrimestralServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.sftp.exception.FtpException;
import ec.gob.dinardap.sftp.util.CredencialesSFTP;
import ec.gob.dinardap.sftp.util.GestionSFTP;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "RemanenteCuatrimestralServicio")
public class RemanenteCuatrimestralServicioImpl extends GenericServiceImpl<RemanenteCuatrimestral, RemanenteCuatrimestralPK> implements RemanenteCuatrimestralServicio {

    @EJB
    private RemanenteCuatrimestralDao remanenteCuatrimestralDao;

    @EJB
    private ParametroServicio parametroServicio;

    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;

    @Override
    public GenericDao<RemanenteCuatrimestral, RemanenteCuatrimestralPK> getDao() {
        return remanenteCuatrimestralDao;
    }

    @Override
    public List<RemanenteCuatrimestral> getRemanenteCuatrimestralList() {
        List<RemanenteCuatrimestral> remanenteCuatrimestralList = new ArrayList<RemanenteCuatrimestral>();
        remanenteCuatrimestralList = remanenteCuatrimestralDao.findAll();
        return remanenteCuatrimestralList;
    }

    @Override
    public List<RemanenteCuatrimestral> getRemanenteCuatrimestralListByAño(RemanenteAnual remanenteAnual) {
        List<RemanenteCuatrimestral> remanenteCuatrimestralList = new ArrayList<RemanenteCuatrimestral>();
        String[] criteriaNombres = {"remanenteAnual.remanenteAnualPK.remanenteAnualId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {remanenteAnual.getRemanenteAnualPK().getRemanenteAnualId()};
        String[] orderBy = {"fecha"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        remanenteCuatrimestralList = findByCriterias(criteria);
        return remanenteCuatrimestralList;
    }

    @Override
    public RemanenteAnual getRemanenteAnual(RemanenteCuatrimestral remanenteCuatrimestral) {
        RemanenteCuatrimestral rc = new RemanenteCuatrimestral();
        rc = findByPk(remanenteCuatrimestral.getRemanenteCuatrimestralPK());
//        return rc.getRemanenteAnual();
        return null;
    }

    @Override
    public void createRemanenteCuatrimestral(Date fecha, Integer institucionId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        if (!remanenteCuatrimestralDao.verificarRemanenteCuatrimestral(fecha)) {
            RemanenteCuatrimestral rc = new RemanenteCuatrimestral();
//            rc.setRemanenteAnual(remanenteCuatrimestralDao.getRemanenteAnual(fecha));
            rc.setRemanenteCuatrimestralPK(new RemanenteCuatrimestralPK(0,
                    remanenteCuatrimestralDao.getRemanenteAnual(fecha).getRemanenteAnualPK().getRemanenteAnualId(),
                    remanenteCuatrimestralDao.getRemanenteAnual(fecha).getRemanenteAnualPK().getInstitucionId()));
        }
    }

    private Boolean verificarRemanenteCuatrimestral(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        Calendar fechaDesde = Calendar.getInstance();
        Calendar fechaHasta = Calendar.getInstance();
        Date dateDesde = new Date();
        Date dateHasta = new Date();
        if (calendar.get(Calendar.MONTH) + 1 <= 4) {
            fechaDesde.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaDesde.set(Calendar.MONTH, 0);
            fechaDesde.set(Calendar.DAY_OF_MONTH, 1);
            dateDesde = fechaDesde.getTime();
            fechaHasta.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaHasta.set(Calendar.MONTH, 3);
            fechaHasta.set(Calendar.DAY_OF_MONTH, 30);
            dateHasta = fechaHasta.getTime();
        } else if (calendar.get(Calendar.MONTH) + 1 > 4 && calendar.get(Calendar.MONTH) + 1 <= 8) {
            fechaDesde.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaDesde.set(Calendar.MONTH, 4);
            fechaDesde.set(Calendar.DAY_OF_MONTH, 1);
            dateDesde = fechaDesde.getTime();
            fechaHasta.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaHasta.set(Calendar.MONTH, 7);
            fechaHasta.set(Calendar.DAY_OF_MONTH, 31);
            dateHasta = fechaHasta.getTime();
        } else if (calendar.get(Calendar.MONTH) + 1 > 8 && calendar.get(Calendar.MONTH) + 1 <= 12) {
            fechaDesde.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaDesde.set(Calendar.MONTH, 7);
            fechaDesde.set(Calendar.DAY_OF_MONTH, 1);
            dateDesde = fechaDesde.getTime();
            fechaHasta.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaHasta.set(Calendar.MONTH, 11);
            fechaHasta.set(Calendar.DAY_OF_MONTH, 31);
            dateHasta = fechaHasta.getTime();
        }

        List<RemanenteCuatrimestral> remanenteCuatrimestralList = new ArrayList<RemanenteCuatrimestral>();
        String[] criteriaNombres = {"fecha"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.DATE_BETWEEN};
        Object[] criteriaValores = {new DateBetween(dateDesde, dateHasta)};
        String[] orderBy = {"fecha"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        remanenteCuatrimestralList = findByCriterias(criteria);
        return true;
    }

    @Override
    @SuppressWarnings("empty-statement")
    public List<RemanenteCuatrimestral> getRemanenteCuatrimestralListByInstitucion(Integer institucionId, Integer año) {
        List<RemanenteCuatrimestral> remanenteCuatrimestralList = new ArrayList<RemanenteCuatrimestral>();
        String[] criteriaNombres = {
            "remanenteAnual.institucion.institucionId",
            "remanenteAnual.anio"
        };
        CriteriaTypeEnum[] criteriaTipos = {
            CriteriaTypeEnum.INTEGER_EQUALS,
            CriteriaTypeEnum.INTEGER_EQUALS
        };
        Object[] criteriaValores = {institucionId, año};
        String[] orderBy = {"cuatrimestre"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        remanenteCuatrimestralList = findByCriterias(criteria);
        for (RemanenteCuatrimestral remanenteCuatrimestral : remanenteCuatrimestralList) {
            remanenteCuatrimestral.getRemanenteCuatrimestralPK();
            for (EstadoRemanenteCuatrimestral estadoRemanenteCuatrimestral : remanenteCuatrimestral.getEstadoRemanenteCuatrimestralList()) {
                estadoRemanenteCuatrimestral.getEstadoRemanenteCuatrimestralId();
            }
            Collections.sort(remanenteCuatrimestral.getEstadoRemanenteCuatrimestralList(), new Comparator<EstadoRemanenteCuatrimestral>() {
                @Override
                public int compare(EstadoRemanenteCuatrimestral erm1, EstadoRemanenteCuatrimestral erm2) {
                    return new Integer(erm1.getEstadoRemanenteCuatrimestralId()).compareTo(new Integer(erm2.getEstadoRemanenteCuatrimestralId()));
                }
            });
            remanenteCuatrimestral.setRemanenteMensualList(new ArrayList<RemanenteMensual>());
        }
        return remanenteCuatrimestralList;
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
        credencialesSFTP.setUsuario(parametroServicio.findByPk(ParametroEnum.USUARIO_REMANENTE_SFTP.name()).getValor());
        credencialesSFTP.setContrasena(parametroServicio.findByPk(ParametroEnum.CONTRASENA_REMANENTE_SFTP.name()).getValor());
        return credencialesSFTP;
    }

    @Override
    public void editRemanenteCuatrimestral(RemanenteCuatrimestral remanenteCuatrimestral, SftpDto sftpDto) {
        sftpDto.setCredencialesSFTP(setCredencialesSftp(sftpDto.getCredencialesSFTP()));
        this.update(remanenteCuatrimestral);
        guardarArchivo(sftpDto);
    }

    @Override
    public byte[] descargarArchivo(SftpDto sftpDto) {
        try {
            sftpDto.setCredencialesSFTP(setCredencialesSftp(sftpDto.getCredencialesSFTP()));
            return GestionSFTP.descargarArchivo(sftpDto.getCredencialesSFTP());
        } catch (FtpException ex) {
            Logger.getLogger(RemanenteCuatrimestralServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}

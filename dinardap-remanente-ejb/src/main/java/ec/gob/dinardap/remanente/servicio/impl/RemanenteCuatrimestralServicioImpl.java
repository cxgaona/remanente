package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.RemanenteCuatrimestralDao;
import ec.gob.dinardap.remanente.modelo.RemanenteAnual;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestralPK;
import ec.gob.dinardap.remanente.servicio.RemanenteCuatrimestralServicio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "RemanenteCuatrimestralServicio")
public class RemanenteCuatrimestralServicioImpl extends GenericServiceImpl<RemanenteCuatrimestral, RemanenteCuatrimestralPK> implements RemanenteCuatrimestralServicio {

    @EJB
    private RemanenteCuatrimestralDao remanenteCuatrimestralDao;

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
        return rc.getRemanenteAnual();
    }

    @Override
    public void createRemanenteCuatrimestral(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        verificarRemanenteCuatrimestral(fecha);

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
            System.out.println("Fecha desde: " + sdf.format(dateDesde));
            System.out.println("Fecha Hasta: " + sdf.format(dateHasta));
        } else if (calendar.get(Calendar.MONTH) + 1 > 4 && calendar.get(Calendar.MONTH) + 1 <= 8) {
            fechaDesde.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaDesde.set(Calendar.MONTH, 4);
            fechaDesde.set(Calendar.DAY_OF_MONTH, 1);
            dateDesde = fechaDesde.getTime();
            fechaHasta.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaHasta.set(Calendar.MONTH, 7);
            fechaHasta.set(Calendar.DAY_OF_MONTH, 31);
            dateHasta = fechaHasta.getTime();
            System.out.println("Fecha desde: " + sdf.format(dateDesde));
            System.out.println("Fecha Hasta: " + sdf.format(dateHasta));
        } else if (calendar.get(Calendar.MONTH) + 1 > 8 && calendar.get(Calendar.MONTH) + 1 <= 12) {
            fechaDesde.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaDesde.set(Calendar.MONTH, 7);
            fechaDesde.set(Calendar.DAY_OF_MONTH, 1);
            dateDesde = fechaDesde.getTime();
            fechaHasta.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaHasta.set(Calendar.MONTH, 11);
            fechaHasta.set(Calendar.DAY_OF_MONTH, 31);
            dateHasta = fechaHasta.getTime();
            System.out.println("Fecha desde: " + sdf.format(dateDesde));
            System.out.println("Fecha Hasta: " + sdf.format(dateHasta));
        }

        List<RemanenteCuatrimestral> remanenteCuatrimestralList = new ArrayList<RemanenteCuatrimestral>();
        String[] criteriaNombres = {"fecha"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.DATE_BETWEEN};
        Object[] criteriaValores = {dateDesde, dateHasta};
        String[] orderBy = {"fecha"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        remanenteCuatrimestralList = findByCriterias(criteria);
        System.out.println("size: " + remanenteCuatrimestralList.size());

        return true;

    }

}

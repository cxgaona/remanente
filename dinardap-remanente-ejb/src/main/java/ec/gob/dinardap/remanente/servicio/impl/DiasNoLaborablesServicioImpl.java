package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.DiasNoLaborablesDao;
import ec.gob.dinardap.remanente.modelo.DiasNoLaborables;
import ec.gob.dinardap.remanente.servicio.DiasNoLaborablesServicio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "DiasNoLaborablesServicio")
public class DiasNoLaborablesServicioImpl extends GenericServiceImpl<DiasNoLaborables, Integer> implements DiasNoLaborablesServicio {

    @EJB
    private DiasNoLaborablesDao diasNoLaborablesDao;

    @Override
    public GenericDao<DiasNoLaborables, Integer> getDao() {
        return diasNoLaborablesDao;
    }

    @Override
    public List<Date> diasFestivosAtivos() {
        try {
            List<Date> diasFestivosList = new ArrayList<Date>();
            List<DiasNoLaborables> fechaList = new ArrayList<DiasNoLaborables>();
            String[] criteriaNombres = {"estado"};
            CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_EQUALS};
            Object[] criteriaValores = {"A"};
            String[] orderBy = {"diasNoLaborablesId"};
            boolean[] asc = {true};
            Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
            fechaList = findByCriterias(criteria);
            for (DiasNoLaborables dnl : fechaList) {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dnl.getAnio() + "-" + dnl.getMes() + "-" + dnl.getDia());
                diasFestivosList.add(date);
            }
            return diasFestivosList;
        } catch (ParseException ex) {
            Logger.getLogger(DiasNoLaborablesServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}

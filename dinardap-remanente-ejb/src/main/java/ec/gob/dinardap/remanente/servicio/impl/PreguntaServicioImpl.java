package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.PreguntaDao;
import ec.gob.dinardap.remanente.modelo.Pregunta;
import ec.gob.dinardap.remanente.servicio.PreguntaServicio;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "PreguntaServicio")
public class PreguntaServicioImpl extends GenericServiceImpl<Pregunta, Integer> implements PreguntaServicio {

    @EJB
    private PreguntaDao preguntaDao;

    @Override
    public GenericDao<Pregunta, Integer> getDao() {
        return preguntaDao;
    }

    @Override
    public List<Pregunta> getPreguntasActivas() {
        List<Pregunta> preguntaList = new ArrayList<Pregunta>();
        String[] criteriaNombres = {"estado"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_EQUALS};
        Object[] criteriaValores = {"A"};
        String[] orderBy = {"preguntaId"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        preguntaList = findByCriterias(criteria);
        return preguntaList;
    }

    

}

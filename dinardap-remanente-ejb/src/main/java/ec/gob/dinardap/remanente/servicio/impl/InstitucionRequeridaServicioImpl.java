package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.InstitucionRequeridaDao;
import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
import java.util.ArrayList;

@Stateless(name = "InstitucionRequeridaServicio")
public class InstitucionRequeridaServicioImpl extends GenericServiceImpl<InstitucionRequerida, Integer> implements InstitucionRequeridaServicio {

    @EJB
    private InstitucionRequeridaDao institucionRequeridaDao;

    @Override
    public GenericDao<InstitucionRequerida, Integer> getDao() {
        return institucionRequeridaDao;
    }

    @Override
    public List<InstitucionRequerida> getInstitucion() {
        List<InstitucionRequerida> institucionRequeridaList = new ArrayList<InstitucionRequerida>();
        institucionRequeridaList = institucionRequeridaDao.findAll();
        for (InstitucionRequerida ir : institucionRequeridaList) {
            if (ir.getGad() == null) {
                ir.setGad(null);
            }
            if (ir.getDireccionRegional() == null) {
                ir.setDireccionRegional(null);
            }
        }
        return institucionRequeridaList;
    }

    @Override
    public List<InstitucionRequerida> getRegistroMixtoList() {
        List<InstitucionRequerida> registroMixtoList = new ArrayList<InstitucionRequerida>();
        String[] criteriaNombres = {"tipo"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_IN_LIST};
        String valores = "'CON GAD','SIN GAD'";
        Object[] criteriaValores = {valores};
        String[] orderBy = {"ruc"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        registroMixtoList = findByCriterias(criteria);
        for (InstitucionRequerida ir : registroMixtoList) {
            if (ir.getGad() == null) {
                ir.setGad(null);
            }
            if (ir.getDireccionRegional() == null) {
                ir.setDireccionRegional(null);
            }
        }
        return registroMixtoList;
    }

    @Override
    public List<InstitucionRequerida> getGADList() {
        List<InstitucionRequerida> gadList = new ArrayList<InstitucionRequerida>();
        String[] criteriaNombres = {"tipo"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_EQUALS};
        Object[] criteriaValores = {"GAD"};
        String[] orderBy = {"ruc"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        gadList = findByCriterias(criteria);
        for (InstitucionRequerida ir : gadList) {
            if (ir.getGad() == null) {
                ir.setGad(null);
            }
            if (ir.getDireccionRegional() == null) {
                ir.setDireccionRegional(null);
            }
        }
        return gadList;
    }

    @Override
    public List<InstitucionRequerida> getDireccionRegionalList() {
        List<InstitucionRequerida> direccionRegionalList = new ArrayList<InstitucionRequerida>();
        String[] criteriaNombres = {"tipo"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_EQUALS};
        Object[] criteriaValores = {"REGIONAL"};
        String[] orderBy = {"institucionId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        direccionRegionalList = findByCriterias(criteria);
        for (InstitucionRequerida ir : direccionRegionalList) {
            if (ir.getGad() == null) {
                ir.setGad(null);
            }
            if (ir.getDireccionRegional() == null) {
                ir.setDireccionRegional(null);
            }
        }
        return direccionRegionalList;
    }
//    @Override
//    public List<InstitucionRequerida> getDireccionRegionalList(InstitucionRequerida registroMixto) {
//        List<InstitucionRequerida> registroMixtoList = new ArrayList<InstitucionRequerida>();
//        String[] criteriaNombres = {"tipo"};
//        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_EQUALS};
//        Object[] criteriaValores = {"REGIONAL"};
//        String[] orderBy = {"institucion_id"};
//        boolean[] asc = {true};
//        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
//        registroMixtoList = findByCriterias(criteria);
//        return registroMixtoList;
//    }

    @Override
    public InstitucionRequerida getInstitucionById(Integer institucionId) {
        InstitucionRequerida institucionRequerida = new InstitucionRequerida();
        institucionRequerida = findByPk(institucionId);
        return institucionRequerida;
    }

    @Override
    public InstitucionRequerida getRegistroMixtoByGad(Integer institucionId) {
        List<InstitucionRequerida> gadList = new ArrayList<InstitucionRequerida>();
        String[] criteriaNombres = {"gad.institucionId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {institucionId};
        String[] orderBy = {"institucionId"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        gadList = findByCriterias(criteria);
        return gadList.get(gadList.size() - 1);
    }

    @Override
    public List<InstitucionRequerida> getRegistroMixtoList(Integer direccionRegionalID) {
        List<InstitucionRequerida> registrosMixtosList = new ArrayList<InstitucionRequerida>();
        String[] criteriaNombres = {"direccionRegional.institucionId", "tipo"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.STRING_NOT_EQUALS};
        Object[] criteriaValores = {direccionRegionalID, "GAD"};
        String[] orderBy = {"institucionId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        registrosMixtosList = findByCriterias(criteria);
        return registrosMixtosList;
    }

    @Override
    public List<InstitucionRequerida> getDireccionNacionalList() {
        List<InstitucionRequerida> dinardapList = new ArrayList<InstitucionRequerida>();
        String[] criteriaNombres = {"tipo"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_EQUALS};
        Object[] criteriaValores = {"DINARDAP"};
        String[] orderBy = {"ruc"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        dinardapList = findByCriterias(criteria);
        return dinardapList;
    }

}

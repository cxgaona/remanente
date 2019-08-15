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
            if (ir.getInstitucionGad() == null) {
                ir.setInstitucionGad(null);
            }
            if (ir.getInstitucionDireccionRegional() == null) {
                ir.setInstitucionDireccionRegional(null);
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
            if (ir.getInstitucionGad() == null) {
                ir.setInstitucionGad(null);
            }
            if (ir.getInstitucionDireccionRegional() == null) {
                ir.setInstitucionDireccionRegional(null);
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
            if (ir.getInstitucionGad() == null) {
                ir.setInstitucionGad(null);
            }
            if (ir.getInstitucionDireccionRegional() == null) {
                ir.setInstitucionDireccionRegional(null);
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
        String[] orderBy = {"ruc"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        direccionRegionalList = findByCriterias(criteria);
        for (InstitucionRequerida ir : direccionRegionalList) {
            if (ir.getInstitucionGad() == null) {
                ir.setInstitucionGad(null);
            }
            if (ir.getInstitucionDireccionRegional() == null) {
                ir.setInstitucionDireccionRegional(null);
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

}

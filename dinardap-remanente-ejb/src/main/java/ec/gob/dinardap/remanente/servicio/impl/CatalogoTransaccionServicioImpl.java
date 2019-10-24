package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.CatalogoTransaccionDao;
import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import ec.gob.dinardap.remanente.servicio.CatalogoTransaccionServicio;
import java.util.ArrayList;

@Stateless(name = "CatalogoTransaccionServicio")
public class CatalogoTransaccionServicioImpl extends GenericServiceImpl<CatalogoTransaccion, Integer> implements CatalogoTransaccionServicio {

    @EJB
    private CatalogoTransaccionDao catalogoTransaccionDao;

    @Override
    public GenericDao<CatalogoTransaccion, Integer> getDao() {
        return catalogoTransaccionDao;
    }

    @Override
    public List<CatalogoTransaccion> getCatalogoTransaccionList() {
        List<CatalogoTransaccion> catalogoTransacciones = new ArrayList<>();
        catalogoTransacciones = catalogoTransaccionDao.findAll();
        return catalogoTransacciones;
    }

    @Override
    public List<CatalogoTransaccion> getCatalogoTransaccionListTipo(String tipo) {
        List<CatalogoTransaccion> catalogoTransaccionList = new ArrayList<CatalogoTransaccion>();
        String[] criteriaNombres = {"tipo"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_EQUALS};
        Object[] criteriaValores = {"Ingreso-" + tipo};
        String[] orderBy = {"tipo"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        catalogoTransaccionList = findByCriterias(criteria);
        return catalogoTransaccionList;
    }
}

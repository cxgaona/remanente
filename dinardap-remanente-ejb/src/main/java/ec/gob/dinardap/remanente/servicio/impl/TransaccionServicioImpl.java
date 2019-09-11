package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.TransaccionDao;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "TransaccionServicio")
public class TransaccionServicioImpl extends GenericServiceImpl<Transaccion, Integer> implements TransaccionServicio {

    @EJB
    private TransaccionDao transaccionDao;

    @Override
    public GenericDao<Transaccion, Integer> getDao() {
        return transaccionDao;
    }

    @Override
    public void editTransaccion(Transaccion transaccion) {
        this.update(transaccion);
    }

    @Override
    public List<Transaccion> getTransaccionByInstitucionAñoMes(Integer institucionId, Integer año, Integer mes) {
        List<Transaccion> transaccionList = new ArrayList<Transaccion>();
        String[] criteriaNombres = {"remanenteMensualId.remanenteCuatrimestral.remanenteAnual.institucionRequerida.institucionId",
            "remanenteMensualId.remanenteCuatrimestral.remanenteAnual.anio",
            "remanenteMensualId.mes"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {institucionId, año, mes};
        String[] orderBy = {"catalogoTransaccionId.catalogoTransaccionId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        transaccionList = findByCriterias(criteria);
        return transaccionList;
    }
    @Override
    public Transaccion getTransaccionByInstitucionFechaTipo(Integer idInstitucion, Integer anio, Integer mes, Integer tipo) {
        Transaccion transaccion = new Transaccion();
        transaccion = transaccionDao.getTransaccionByInstitucionFechaTipo(idInstitucion, anio, mes, tipo);
        return transaccion;
    }
}

package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.FacturaPagadaDao;
import java.util.ArrayList;
import java.util.List;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.FacturaPagadaServicio;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import java.math.BigDecimal;
import java.util.Objects;

@Stateless(name = "FacturaPagadaServicio")
public class FacturaPagadaServicioImpl extends GenericServiceImpl<FacturaPagada, Integer> implements FacturaPagadaServicio {

    @EJB
    private FacturaPagadaDao facturaPagadaDao;

    @EJB
    private TransaccionServicio transaccionServicio;

    @Override
    public GenericDao<FacturaPagada, Integer> getDao() {
        return facturaPagadaDao;
    }

    @Override
    public List<FacturaPagada> getFacturaPagadaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes, Integer idRemanenteMensual) {
        List<FacturaPagada> facturaPagadaList = new ArrayList<FacturaPagada>();
        facturaPagadaList = facturaPagadaDao.getFacturaPagadaByInstitucionFecha(idInstitucion, anio, mes, idRemanenteMensual);
        return facturaPagadaList;
    }

    @Override
    public List<FacturaPagada> getFacturaPagadaByTransaccion(Integer transaccionId) {
        List<FacturaPagada> facturaPagadaList = new ArrayList<FacturaPagada>();
        String[] criteriaNombres = {"transaccion.transaccionId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {transaccionId};
        String[] orderBy = {"facturaPagadaId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        facturaPagadaList = findByCriterias(criteria);
        return facturaPagadaList;
    }

    @Override
    public void crearFacturaPagadas(List<FacturaPagada> facturaPagadas) {
        for (FacturaPagada facturaPagada : facturaPagadas) {
            this.create(facturaPagada);
        }
    }

    @Override
    public void borrarFacturasPagadas(List<FacturaPagada> facturasPagadas) {
        for (FacturaPagada facturaPagada : facturasPagadas) {
            this.delete(facturaPagada.getFacturaPagadaId());
        }
    }

    @Override
    public void actualizarTransaccionValor(Integer remanenteMensualId) {
        Integer catalogoNomina[] = {10, 11, 12};
        List<Transaccion> transaccionList = new ArrayList<Transaccion>();
        transaccionList = transaccionServicio.getTransacciones(remanenteMensualId);
        for (Transaccion transaccion : transaccionList) {
            if (Objects.equals(transaccion.getCatalogoTransaccion().getCatalogoTransaccionId(), catalogoNomina[0])) {
                Double valor = 0.0;
                for (FacturaPagada facturaPagada : this.getFacturaPagadaByTransaccion(transaccion.getTransaccionId())) {
                    valor += facturaPagada.getValor().doubleValue();
                }
                transaccion.setValorTotal(new BigDecimal(valor));
                transaccionServicio.update(transaccion);
            }
            if (Objects.equals(transaccion.getCatalogoTransaccion().getCatalogoTransaccionId(), catalogoNomina[1])) {
                Double valor = 0.0;
                for (FacturaPagada facturaPagada : this.getFacturaPagadaByTransaccion(transaccion.getTransaccionId())) {
                    valor += facturaPagada.getValor().doubleValue();
                }
                transaccion.setValorTotal(new BigDecimal(valor));
                transaccionServicio.update(transaccion);
            }
            if (Objects.equals(transaccion.getCatalogoTransaccion().getCatalogoTransaccionId(), catalogoNomina[2])) {
                Double valor = 0.0;
                for (FacturaPagada facturaPagada : this.getFacturaPagadaByTransaccion(transaccion.getTransaccionId())) {
                    valor += facturaPagada.getValor().doubleValue();
                }
                transaccion.setValorTotal(new BigDecimal(valor));
                transaccionServicio.update(transaccion);
            }
        }
    }

}

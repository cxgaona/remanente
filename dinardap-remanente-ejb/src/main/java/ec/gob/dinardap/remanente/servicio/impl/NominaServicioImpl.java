package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.servicio.NominaServicio;
import java.util.ArrayList;
import java.util.List;
import ec.gob.dinardap.remanente.dao.NominaDao1;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import java.math.BigDecimal;
import java.util.Objects;

@Stateless(name = "NominaServicio")
public class NominaServicioImpl extends GenericServiceImpl<Nomina, Integer> implements NominaServicio {

    @EJB
    private NominaDao1 nominaDao;

    @EJB
    private TransaccionServicio transaccionServicio;

    @Override
    public GenericDao<Nomina, Integer> getDao() {
        return nominaDao;
    }

    @Override
    public List<Nomina> getNominaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes, Integer idRemanenteMensual) {
        List<Nomina> nominaList = new ArrayList<Nomina>();
        nominaList = nominaDao.getNominaByInstitucionFecha(idInstitucion, anio, mes, idRemanenteMensual);
        return nominaList;
    }

    @Override
    public List<Nomina> getNominaByTransaccion(Integer transaccionId) {
        List<Nomina> nominaList = new ArrayList<Nomina>();
        String[] criteriaNombres = {"transaccion.transaccionId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {transaccionId};
        String[] orderBy = {"nominaId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        nominaList = findByCriterias(criteria);
        return nominaList;
    }

    @Override
    public void crearNominas(List<Nomina> nominas) {
        for (Nomina nomina : nominas) {
            this.create(nomina);
        }
    }

    @Override
    public void borrarNominas(List<Nomina> nominas) {
        for (Nomina nomina : nominas) {
            this.delete(nomina.getNominaId());
        }
    }

    @Override
    public void actualizarTransaccionValor(Integer remanenteMensualId) {
        Integer catalogoNomina[] = {9};
        List<Transaccion> transaccionList = new ArrayList<Transaccion>();
        transaccionList = transaccionServicio.getTransacciones(remanenteMensualId);
        for (Transaccion transaccion : transaccionList) {
            if (Objects.equals(transaccion.getCatalogoTransaccion().getCatalogoTransaccionId(), catalogoNomina[0])) {
                Double valor = 0.0;
                for (Nomina nomina : this.getNominaByTransaccion(transaccion.getTransaccionId())) {
                    valor += nomina.getRemuneracion().doubleValue()
                            + nomina.getAportePatronal().doubleValue()
                            + nomina.getFondosReserva().doubleValue()
                            + nomina.getDecimoTercero().doubleValue()
                            + nomina.getDecimoCuarto().doubleValue();
                }
                transaccion.setValorTotal(new BigDecimal(valor));
                transaccionServicio.update(transaccion);
                break;
            }
        }
    }
}

package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.TramiteDao;
import java.util.ArrayList;
import java.util.List;
import ec.gob.dinardap.remanente.modelo.Tramite;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.TramiteServicio;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import java.math.BigDecimal;
import java.util.Objects;

@Stateless(name = "TramiteServicio")
public class TramiteServicioImpl extends GenericServiceImpl<Tramite, Integer> implements TramiteServicio {

    @EJB
    private TramiteDao tramiteDao;

    @EJB
    private TransaccionServicio transaccionServicio;

    @Override
    public GenericDao<Tramite, Integer> getDao() {
        return tramiteDao;
    }

    @Override
    public void crearTramites(List<Tramite> tramites) {
        for (Tramite tramite : tramites) {
            this.create(tramite);
        }
    }

    @Override
    public void borrarTramites(List<Tramite> tramites) {
        for (Tramite tramite : tramites) {
            this.delete(tramite.getTramiteId());
        }
    }

    @Override
    public List<Tramite> getTramiteByInstitucionFechaActividad(Integer idInstitucion, Integer anio, Integer mes, String actividad, Integer idRemanenteMensual) {
        List<Tramite> tramiteList = new ArrayList<Tramite>();
        tramiteList = tramiteDao.getTramiteByInstitucionFechaActividad(idInstitucion, anio, mes, actividad, idRemanenteMensual);
        return tramiteList;
    }

    @Override
    public List<Tramite> getTramiteByTransaccion(Integer transaccionId) {
        List<Tramite> tramiteList = new ArrayList<Tramite>();
        String[] criteriaNombres = {"transaccion.transaccionId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {transaccionId};
        String[] orderBy = {"tramiteId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        tramiteList = findByCriterias(criteria);
        return tramiteList;
    }

    @Override
    public void actualizarTransaccionValor(Integer remanenteMensualId, String actividadRegistral) {
        Integer catalogoPropiedad[] = {1, 2, 4};
        Integer catalogoMercantil[] = {5, 6, 8};
        Integer catalogoTransaccion[] = null;
        if (actividadRegistral.equals("Propiedad")) {
            catalogoTransaccion = catalogoPropiedad;
        } else if (actividadRegistral.equals("Mercantil")) {
            catalogoTransaccion = catalogoMercantil;
        }

        List<Transaccion> transaccionList = new ArrayList<Transaccion>();
        transaccionList = transaccionServicio.getTransacciones(remanenteMensualId);
        Integer numeroCertificaciones = 0;
        Integer numeroInscripciones = 0;
        for (Transaccion transaccion : transaccionList) {
            if (Objects.equals(transaccion.getCatalogoTransaccion().getCatalogoTransaccionId(), catalogoTransaccion[0])) {
                Double valor = 0.0;
                List<Tramite> certificaciones = new ArrayList<Tramite>();
                certificaciones = this.getTramiteByTransaccion(transaccion.getTransaccionId());
                numeroCertificaciones = certificaciones.size();
                for (Tramite tramite : certificaciones) {
                    valor += tramite.getValor().doubleValue();
                }
                transaccion.setValorTotal(new BigDecimal(valor));
                transaccionServicio.update(transaccion);
            }
            if (Objects.equals(transaccion.getCatalogoTransaccion().getCatalogoTransaccionId(), catalogoTransaccion[1])) {
                Double valor = 0.0;
                List<Tramite> inscripciones = new ArrayList<Tramite>();
                inscripciones = this.getTramiteByTransaccion(transaccion.getTransaccionId());
                numeroInscripciones = inscripciones.size();
                for (Tramite tramite : inscripciones) {
                    valor += tramite.getValor().doubleValue();
                }
                transaccion.setValorTotal(new BigDecimal(valor));
                transaccionServicio.update(transaccion);
            }

            if (Objects.equals(transaccion.getCatalogoTransaccion().getCatalogoTransaccionId(), catalogoTransaccion[2])) {
                transaccion.setValorTotal(new BigDecimal(numeroInscripciones + numeroCertificaciones));
                transaccionServicio.update(transaccion);
            }
        }
    }
}

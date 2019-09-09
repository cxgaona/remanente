package ec.gob.dinardap.remanente.servicio.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.dao.TramiteDao;
import java.util.ArrayList;
import java.util.List;
import ec.gob.dinardap.remanente.modelo.Tramite;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.TramiteServicio;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import java.math.BigDecimal;

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
    public void crearTramite(Tramite tramite) {
        this.create(tramite);
    }

    @Override
    public List<Tramite> getTramiteByInstitucionFechaActividad(Integer idInstitucion, Integer anio, Integer mes, String actividad) {
        List<Tramite> tramiteList = new ArrayList<Tramite>();
        tramiteList = tramiteDao.getTramiteByInstitucionFechaActividad(idInstitucion, anio, mes, actividad);
        return tramiteList;
    }

    @Override
    public void editTramite(Tramite tramite) {
        this.update(tramite);           
    }

    @Override
    public void borrarTramite(Tramite tramite) {
        Integer anio, mes, idInstitucion;
        anio = tramite.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio();
        mes = tramite.getTransaccionId().getRemanenteMensualId().getMes();
        idInstitucion = tramite.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId();
        this.delete(tramite.getTramiteId());        
    }
    
    @Override
    public void actualizarTransaccionValor(Integer idInstitucion, Integer anio, Integer mes, Integer tipo) {
        BigDecimal valorTotalTransaccion = new BigDecimal(0);
        Transaccion t = new Transaccion();
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(idInstitucion, anio, mes, tipo);
        for (Tramite tr : t.getTramiteList()) {            
            valorTotalTransaccion = valorTotalTransaccion.add(tr.getValor());
        }
        t.setValorTotal(valorTotalTransaccion);        
        transaccionServicio.editTransaccion(t);
    }

}

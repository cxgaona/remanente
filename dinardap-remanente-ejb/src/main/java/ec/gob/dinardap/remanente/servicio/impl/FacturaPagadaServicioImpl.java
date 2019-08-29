package ec.gob.dinardap.remanente.servicio.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.dao.FacturaPagadaDao;
import java.util.ArrayList;
import java.util.List;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.FacturaPagadaServicio;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import java.math.BigDecimal;

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
    public void crearFacturaPagada(FacturaPagada facturaPagada) {
        this.create(facturaPagada);
    }

    @Override
    public List<FacturaPagada> getFacturaPagadaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes) {
        List<FacturaPagada> facturaPagadaList = new ArrayList<FacturaPagada>();
        facturaPagadaList = facturaPagadaDao.getFacturaPagadaByInstitucionFecha(idInstitucion, anio, mes);
        return facturaPagadaList;
    }

    @Override
    public void editFacturaPagada(FacturaPagada facturaPagada) {
        this.update(facturaPagada);
        actualizarTransaccionValor(facturaPagada.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                facturaPagada.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                facturaPagada.getTransaccionId().getRemanenteMensualId().getMes(),10);
        actualizarTransaccionValor(facturaPagada.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                facturaPagada.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                facturaPagada.getTransaccionId().getRemanenteMensualId().getMes(),11);
        actualizarTransaccionValor(facturaPagada.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                facturaPagada.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                facturaPagada.getTransaccionId().getRemanenteMensualId().getMes(),12);
    }

    @Override
    public void borrarFacturaPagada(FacturaPagada facturaPagada) {
        Integer anio, mes, idInstitucion;
        anio = facturaPagada.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio();
        mes = facturaPagada.getTransaccionId().getRemanenteMensualId().getMes();
        idInstitucion = facturaPagada.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId();
        this.delete(facturaPagada.getFacturaPagadaId());        
    }
    
    @Override
    public void actualizarTransaccionValor(Integer idInstitucion, Integer anio, Integer mes, Integer tipo) {
        BigDecimal valorTotalTransaccion = new BigDecimal(0);
        Transaccion t = new Transaccion();
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(idInstitucion, anio, mes, tipo);
        for (FacturaPagada fp : t.getFacturaPagadaList()) {            
            valorTotalTransaccion = valorTotalTransaccion.add(fp.getValor());
        }
        t.setValorTotal(valorTotalTransaccion);
        transaccionServicio.editTransaccion(t);
    }

}

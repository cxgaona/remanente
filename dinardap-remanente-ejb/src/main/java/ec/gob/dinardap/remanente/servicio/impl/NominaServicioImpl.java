package ec.gob.dinardap.remanente.servicio.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.servicio.NominaServicio;
import java.util.ArrayList;
import java.util.List;
import ec.gob.dinardap.remanente.dao.NominaDao1;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import java.math.BigDecimal;

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
    public void crearNomina(Nomina nomina) {
        this.create(nomina);
    }

    @Override
    public List<Nomina> getNominaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes) {
        List<Nomina> nominaList = new ArrayList<Nomina>();
        nominaList = nominaDao.getNominaByInstitucionFecha(idInstitucion, anio, mes);
        return nominaList;
    }

    @Override
    public void editNomina(Nomina nomina) {
        this.update(nomina);
        actualizarTransaccionValor(nomina.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId(),
                nomina.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio(),
                nomina.getTransaccionId().getRemanenteMensualId().getMes());
    }

    @Override
    public void borrarNomina(Nomina nomina) {
        Integer anio, mes, idInstitucion;
        anio = nomina.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio();
        mes = nomina.getTransaccionId().getRemanenteMensualId().getMes();
        idInstitucion = nomina.getTransaccionId().getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId();
        this.delete(nomina.getNominaId());        
    }
    
    @Override
    public void actualizarTransaccionValor(Integer idInstitucion, Integer anio, Integer mes) {
        BigDecimal valorTotalTransaccion = new BigDecimal(0);        
        Transaccion t = new Transaccion();
        t = transaccionServicio.getTransaccionByInstitucionFechaTipo(idInstitucion, anio, mes, 9);
        for (Nomina n : t.getNominaList()) {            
            valorTotalTransaccion = valorTotalTransaccion.add(n.getRemuneracion());
            valorTotalTransaccion = valorTotalTransaccion.add(n.getDecimoTercero());
            valorTotalTransaccion = valorTotalTransaccion.add(n.getDecimoCuarto());
        }
        t.setValorTotal(valorTotalTransaccion);
        transaccionServicio.editTransaccion(t);
    }

}

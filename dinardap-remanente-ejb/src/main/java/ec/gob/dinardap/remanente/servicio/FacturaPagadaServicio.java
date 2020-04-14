package ec.gob.dinardap.remanente.servicio;

import java.util.List;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;

@Local
public interface FacturaPagadaServicio extends GenericService<FacturaPagada, Integer> {

    public List<FacturaPagada> getFacturaPagadaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes, Integer idRemanenteMensual);

    public List<FacturaPagada> getFacturaPagadaByTransaccion(Integer transaccionId);

    public void crearFacturaPagadas(List<FacturaPagada> facturaPagadas);

    public void borrarFacturasPagadas(List<FacturaPagada> facturasPagadas);

    public void actualizarTransaccionValor(Integer remanenteMensualId);

}

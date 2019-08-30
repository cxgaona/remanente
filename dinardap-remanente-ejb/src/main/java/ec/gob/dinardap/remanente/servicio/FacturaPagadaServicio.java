package ec.gob.dinardap.remanente.servicio;

import java.util.List;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;

@Local
public interface FacturaPagadaServicio extends GenericService<FacturaPagada, Integer> {

    public void crearFacturaPagada(FacturaPagada facturaPagada);
    
    public List<FacturaPagada> getFacturaPagadaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes);
    
    public void editFacturaPagada(FacturaPagada facturaPagada);
    
    public void borrarFacturaPagada(FacturaPagada facturaPagada);
    
    public void actualizarTransaccionValor(Integer idInstitucion, Integer anio, Integer mes, Integer tipo);
}

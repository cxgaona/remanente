package ec.gob.dinardap.remanente.servicio;

import java.util.List;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.Tramite;

@Local
public interface TramiteServicio extends GenericService<Tramite, Integer> {

    public void crearTramite(Tramite tramite);
    
    public List<Tramite> getTramiteByInstitucionFechaActividad(Integer idInstitucion, Integer anio, Integer mes, String actividad);
    
    public void editTramite(Tramite tramite);
    
    public void borrarTramite(Tramite tramite);
    
    public void actualizarTransaccionValor(Integer idInstitucion, Integer anio, Integer mes, Integer tipo);
}

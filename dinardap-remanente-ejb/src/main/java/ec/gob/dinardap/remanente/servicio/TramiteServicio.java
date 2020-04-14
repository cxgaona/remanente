package ec.gob.dinardap.remanente.servicio;

import java.util.List;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Tramite;

@Local
public interface TramiteServicio extends GenericService<Tramite, Integer> {

    public void crearTramites(List<Tramite> tramites);

    public void borrarTramites(List<Tramite> tramites);

    public List<Tramite> getTramiteByInstitucionFechaActividad(Integer idInstitucion, Integer anio, Integer mes, String actividad, Integer idRemanenteMensual);

    public List<Tramite> getTramiteByTransaccion(Integer transaccionId);

    public void actualizarTransaccionValor(Integer remanenteMensualId, String actividadRegistral);

}

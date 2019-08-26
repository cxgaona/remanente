package ec.gob.dinardap.remanente.servicio;

import java.util.List;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.Nomina;

@Local
public interface NominaServicio extends GenericService<Nomina, Integer> {

    public void crearNomina(Nomina nomina);
    
    public List<Nomina> getNominaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes);
}

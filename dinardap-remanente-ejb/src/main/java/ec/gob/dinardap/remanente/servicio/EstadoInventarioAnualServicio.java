package ec.gob.dinardap.remanente.servicio;


import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.EstadoInventarioAnual;


@Local
public interface EstadoInventarioAnualServicio extends GenericService<EstadoInventarioAnual, Integer> { 

}

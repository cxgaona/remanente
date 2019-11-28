package ec.gob.dinardap.remanente.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteCuatrimestral;

@Local
public interface EstadoRemanenteCuatrimestralServicio extends GenericService<EstadoRemanenteCuatrimestral, Integer> {

}

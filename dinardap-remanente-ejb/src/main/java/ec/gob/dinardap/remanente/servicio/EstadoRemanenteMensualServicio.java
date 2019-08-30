package ec.gob.dinardap.remanente.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteMensual;

@Local
public interface EstadoRemanenteMensualServicio extends GenericService<EstadoRemanenteMensual, Integer> {

    public void createEstadoRemanenteMensual(EstadoRemanenteMensual erm);

}

package ec.gob.dinardap.remanente.servicio;


import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.Transaccion;

@Local
public interface TransaccionServicio extends GenericService<Transaccion, Integer> {

    public void editTransaccion(Transaccion transaccion);

}

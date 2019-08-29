package ec.gob.dinardap.remanente.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import java.util.List;

@Local
public interface TransaccionServicio extends GenericService<Transaccion, Integer> {

    public void editTransaccion(Transaccion transaccion);

    public List<Transaccion> getTransaccionByInstitucionAñoMes(Integer institucionId, Integer año, Integer mes);

}

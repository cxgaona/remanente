package ec.gob.dinardap.remanente.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.ProrrogaRemanenteMensual;
import java.util.List;

@Local
public interface ProrrogaRemanenteMensualServicio extends GenericService<ProrrogaRemanenteMensual, Integer> {

    public List<ProrrogaRemanenteMensual> getListProrrogaRemanenteMensualEstado(String estado);

}

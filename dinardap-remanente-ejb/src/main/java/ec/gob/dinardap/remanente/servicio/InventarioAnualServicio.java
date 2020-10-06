package ec.gob.dinardap.remanente.servicio;


import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.InventarioAnual;


@Local
public interface InventarioAnualServicio extends GenericService<InventarioAnual, Integer> { 
    public InventarioAnual getInventarioPorInstitucionAño(Integer institucionId, Integer año);

}

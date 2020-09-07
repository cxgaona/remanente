package ec.gob.dinardap.remanente.servicio;


import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import java.util.List;

@Local
public interface InstitucionServicio extends GenericService<Institucion, Integer> {
    public List<Institucion> getRegistroMixtoList(Integer direccionRegionalId);
   
}

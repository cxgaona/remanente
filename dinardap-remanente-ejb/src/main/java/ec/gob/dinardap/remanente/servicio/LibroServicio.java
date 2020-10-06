package ec.gob.dinardap.remanente.servicio;

import java.util.List;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.Libro;


@Local
public interface LibroServicio extends GenericService<Libro, Integer> {   

    public List<Libro> getLibrosActivosPorInventarioTipo(Integer inventarioAnualId, Integer tipoLibroId);
    

}

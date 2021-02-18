package ec.gob.dinardap.remanente.servicio;

import java.util.List;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.Tomo;


@Local
public interface TomoServicio extends GenericService<Tomo, Integer> {   

    public List<Tomo> getTomosActivosPorLibro(Integer libroId);
    public List<Tomo> getTomosPorInstitucionAñoTipo(Integer institucionId, Integer año, Integer tipoLibroId);
    public Tomo getUltimoTomoPorLibro(Integer libroId);

}

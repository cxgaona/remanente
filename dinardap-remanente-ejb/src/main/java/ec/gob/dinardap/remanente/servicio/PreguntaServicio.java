package ec.gob.dinardap.remanente.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.Pregunta;
import java.util.List;

@Local
public interface PreguntaServicio extends GenericService<Pregunta, Integer> {

    public List<Pregunta> getPreguntasActivas();    

}

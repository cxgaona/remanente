package ec.gob.dinardap.remanente.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.Respuesta;
import java.util.List;

@Local
public interface RespuestaServicio extends GenericService<Respuesta, Integer> {

    public Respuesta getRespuestaByUsuario(Integer idUsuario, Integer idPregunta);    

}

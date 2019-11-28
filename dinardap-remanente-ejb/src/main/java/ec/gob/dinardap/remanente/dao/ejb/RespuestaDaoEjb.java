package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.RespuestaDao;
import ec.gob.dinardap.remanente.modelo.Respuesta;

import javax.ejb.Stateless;

@Stateless(name = "RespuestaDao")
public class RespuestaDaoEjb extends RemanenteGenericDao<Respuesta, Integer> implements RespuestaDao {

    public RespuestaDaoEjb() {
        super(Respuesta.class);
    }

}

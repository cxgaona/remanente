package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.PreguntaDao;
import ec.gob.dinardap.remanente.modelo.Pregunta;

import javax.ejb.Stateless;

@Stateless(name = "PreguntaDao")
public class PreguntaDaoEjb extends RemanenteGenericDao<Pregunta, Integer> implements PreguntaDao {

    public PreguntaDaoEjb() {
        super(Pregunta.class);
    }

}

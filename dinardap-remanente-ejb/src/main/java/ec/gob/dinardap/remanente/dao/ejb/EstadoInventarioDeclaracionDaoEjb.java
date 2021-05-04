package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.EstadoInventarioDeclaracionDao;
import ec.gob.dinardap.remanente.modelo.EstadoInventarioDeclaracion;

import javax.ejb.Stateless;

@Stateless(name = "EstadoInventarioDeclaracionDao")
public class EstadoInventarioDeclaracionDaoEjb extends RemanenteGenericDao<EstadoInventarioDeclaracion, Integer> implements EstadoInventarioDeclaracionDao {

    public EstadoInventarioDeclaracionDaoEjb() {
        super(EstadoInventarioDeclaracion.class);
    }

}

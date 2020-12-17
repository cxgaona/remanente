package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.EstadoInventarioAnualDao;
import ec.gob.dinardap.remanente.modelo.EstadoInventarioAnual;

import javax.ejb.Stateless;

@Stateless(name = "EstadoInventarioAnualDao")
public class EstadoInventarioAnualDaoEjb extends RemanenteGenericDao<EstadoInventarioAnual, Integer> implements EstadoInventarioAnualDao {

    public EstadoInventarioAnualDaoEjb() {
        super(EstadoInventarioAnual.class);
    }

}

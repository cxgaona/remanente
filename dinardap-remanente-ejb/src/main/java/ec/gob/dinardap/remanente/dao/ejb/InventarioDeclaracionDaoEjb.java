package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.InventarioDeclaracionDao;
import ec.gob.dinardap.remanente.modelo.InventarioDeclaracion;

import javax.ejb.Stateless;

@Stateless(name = "InventarioDeclaracionDao")
public class InventarioDeclaracionDaoEjb extends RemanenteGenericDao<InventarioDeclaracion, Integer> implements InventarioDeclaracionDao {

    public InventarioDeclaracionDaoEjb() {
        super(InventarioDeclaracion.class);
    }

}

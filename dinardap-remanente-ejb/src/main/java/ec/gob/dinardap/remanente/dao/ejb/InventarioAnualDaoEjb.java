package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.InventarioAnualDao;
import ec.gob.dinardap.remanente.modelo.InventarioAnual;

import javax.ejb.Stateless;

@Stateless(name = "InventarioAnualDao")
public class InventarioAnualDaoEjb extends RemanenteGenericDao<InventarioAnual, Integer> implements InventarioAnualDao {

    public InventarioAnualDaoEjb() {
        super(InventarioAnual.class);
    }

}

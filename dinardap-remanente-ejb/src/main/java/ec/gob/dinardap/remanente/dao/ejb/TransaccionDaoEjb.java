package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.TransaccionDao;
import ec.gob.dinardap.remanente.modelo.Transaccion;

import javax.ejb.Stateless;

@Stateless(name = "TransaccionDao")
public class TransaccionDaoEjb extends RemanenteGenericDao<Transaccion, Integer> implements TransaccionDao {

    public TransaccionDaoEjb() {
        super(Transaccion.class);
    }

}

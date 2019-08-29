package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.EstadoRemanenteMensualDao;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteMensual;

import javax.ejb.Stateless;

@Stateless(name = "EstadoRemanenteMensualDao")
public class EstadoRemanenteMensualDaoEjb extends RemanenteGenericDao<EstadoRemanenteMensual, Integer> implements EstadoRemanenteMensualDao {

    public EstadoRemanenteMensualDaoEjb() {
        super(EstadoRemanenteMensual.class);
    }

}

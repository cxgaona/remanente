package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.ProrrogaRemanenteMensualDao;
import ec.gob.dinardap.remanente.modelo.ProrrogaRemanenteMensual;

import javax.ejb.Stateless;

@Stateless(name = "ProrrogaRemanenteMensualDao")
public class ProrrogaRemanenteMensualDaoEjb extends RemanenteGenericDao<ProrrogaRemanenteMensual, Integer> implements ProrrogaRemanenteMensualDao {

    public ProrrogaRemanenteMensualDaoEjb() {
        super(ProrrogaRemanenteMensual.class);
    }
}

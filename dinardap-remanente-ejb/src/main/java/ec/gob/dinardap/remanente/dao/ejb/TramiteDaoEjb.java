package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.TramiteDao;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.Tramite;

import javax.ejb.Stateless;

@Stateless(name = "NominaDao")
public class TramiteDaoEjb extends RemanenteGenericDao<Tramite, Integer> implements TramiteDao {

    public TramiteDaoEjb() {
        super(Tramite.class);
    }

}

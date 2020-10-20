package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.RemanenteHistoricoDao;
import ec.gob.dinardap.remanente.modelo.RemanenteHistorico;

import javax.ejb.Stateless;

@Stateless(name = "RemanenteHistoricoDao")
public class RemanenteHistoricoDaoEjb extends RemanenteGenericDao<RemanenteHistorico, Integer> implements RemanenteHistoricoDao {

    public RemanenteHistoricoDaoEjb() {
        super(RemanenteHistorico.class);
    }
    
}

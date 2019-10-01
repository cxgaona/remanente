package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.BandejaDao;
import ec.gob.dinardap.remanente.modelo.Bandeja;

import javax.ejb.Stateless;

@Stateless(name = "BandejaDao")
public class BandejaDaoEjb extends RemanenteGenericDao<Bandeja, Integer> implements BandejaDao {

    public BandejaDaoEjb() {
        super(Bandeja.class);
    }
}

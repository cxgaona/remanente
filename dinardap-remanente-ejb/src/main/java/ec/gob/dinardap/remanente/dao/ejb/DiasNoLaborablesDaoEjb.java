package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.DiasNoLaborablesDao;
import ec.gob.dinardap.remanente.modelo.DiasNoLaborables;

import javax.ejb.Stateless;

@Stateless(name = "DiasNoLaborablesDao")
public class DiasNoLaborablesDaoEjb extends RemanenteGenericDao<DiasNoLaborables, Integer> implements DiasNoLaborablesDao {

    public DiasNoLaborablesDaoEjb() {
        super(DiasNoLaborables.class);
    }
}

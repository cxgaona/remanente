package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.InformeCumplimientoDao;
import ec.gob.dinardap.remanente.modelo.InformeCumplimiento;

import javax.ejb.Stateless;

@Stateless(name = "InformeCumplimientoDao")
public class InformeCumplimientoDaoEjb extends RemanenteGenericDao<InformeCumplimiento, Integer> implements InformeCumplimientoDao {

    public InformeCumplimientoDaoEjb() {
        super(InformeCumplimiento.class);
    }

}

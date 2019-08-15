package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.RemanenteCuatrimestralDao;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestralPK;

import javax.ejb.Stateless;

@Stateless(name = "RemanenteCuatrimestralDao")
public class RemanenteCuatrimestralDaoEjb extends RemanenteGenericDao<RemanenteCuatrimestral, RemanenteCuatrimestralPK> implements RemanenteCuatrimestralDao {

    public RemanenteCuatrimestralDaoEjb() {
        super(RemanenteCuatrimestral.class);
    }

}

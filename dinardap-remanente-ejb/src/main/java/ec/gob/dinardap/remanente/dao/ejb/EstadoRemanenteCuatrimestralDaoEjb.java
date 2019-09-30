package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.EstadoRemanenteCuatrimestralDao;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteCuatrimestral;

import javax.ejb.Stateless;

@Stateless(name = "EstadoRemanenteCuatrimestralDao")
public class EstadoRemanenteCuatrimestralDaoEjb extends RemanenteGenericDao<EstadoRemanenteCuatrimestral, Integer> implements EstadoRemanenteCuatrimestralDao {

    public EstadoRemanenteCuatrimestralDaoEjb() {
        super(EstadoRemanenteCuatrimestral.class);
    }

}

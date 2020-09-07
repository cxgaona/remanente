package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.InstitucionDao;
import ec.gob.dinardap.seguridad.modelo.Institucion;

import javax.ejb.Stateless;

@Stateless(name = "InstitucionDao")
public class InstitucionDaoEjb extends RemanenteGenericDao<Institucion, Integer> implements InstitucionDao {

    public InstitucionDaoEjb() {
        super(Institucion.class);
    }

}

package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.AsignacionInstitucionDao;
import ec.gob.dinardap.seguridad.modelo.AsignacionInstitucion;

import javax.ejb.Stateless;

@Stateless(name = "AsignacionInstitucionDao")
public class AsignacionInstitucionDaoEjb extends RemanenteGenericDao<AsignacionInstitucion, Integer> implements AsignacionInstitucionDao {

    public AsignacionInstitucionDaoEjb() {
        super(AsignacionInstitucion.class);
    }

}

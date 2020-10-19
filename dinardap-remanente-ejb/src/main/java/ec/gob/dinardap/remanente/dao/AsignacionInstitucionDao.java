package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.seguridad.modelo.AsignacionInstitucion;

@Local
public interface AsignacionInstitucionDao extends GenericDao<AsignacionInstitucion, Integer> {

}

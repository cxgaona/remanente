package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.modelo.Respuesta;

@Local
public interface RespuestaDao extends GenericDao<Respuesta, Integer> {

}

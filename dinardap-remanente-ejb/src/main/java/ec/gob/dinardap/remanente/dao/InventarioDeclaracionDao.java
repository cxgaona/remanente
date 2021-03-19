package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.modelo.InventarioDeclaracion;

@Local
public interface InventarioDeclaracionDao extends GenericDao<InventarioDeclaracion, Integer> {

}

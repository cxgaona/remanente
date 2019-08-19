package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import java.util.List;

@Local
public interface RemanenteMensualDao extends GenericDao<RemanenteMensual, Integer> {

}

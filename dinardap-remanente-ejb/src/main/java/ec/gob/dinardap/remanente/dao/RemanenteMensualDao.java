package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;

@Local
public interface RemanenteMensualDao extends GenericDao<RemanenteMensual, Integer> {
    

}

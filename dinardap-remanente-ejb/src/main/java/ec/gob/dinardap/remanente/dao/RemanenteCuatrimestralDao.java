package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestralPK;

@Local
public interface RemanenteCuatrimestralDao extends GenericDao<RemanenteCuatrimestral, RemanenteCuatrimestralPK> {
}

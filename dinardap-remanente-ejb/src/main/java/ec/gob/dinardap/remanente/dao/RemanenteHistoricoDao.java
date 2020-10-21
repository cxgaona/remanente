package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.modelo.RemanenteHistorico;

@Local
public interface RemanenteHistoricoDao extends GenericDao<RemanenteHistorico, Integer> {

}

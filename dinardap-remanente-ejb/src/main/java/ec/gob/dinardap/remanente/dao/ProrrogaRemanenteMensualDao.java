package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.dto.ProrrogaRemanenteMensualDTO;
import ec.gob.dinardap.remanente.modelo.ProrrogaRemanenteMensual;
import java.util.List;

@Local
public interface ProrrogaRemanenteMensualDao extends GenericDao<ProrrogaRemanenteMensual, Integer> {

    public List<ProrrogaRemanenteMensualDTO> getProrrogaListEstado(String estado);

}

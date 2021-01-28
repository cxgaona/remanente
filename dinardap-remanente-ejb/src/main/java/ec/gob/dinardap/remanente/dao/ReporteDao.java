package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.dto.ConteoTramitesDTO;
import ec.gob.dinardap.remanente.dto.UltimoEstadoDTO;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import java.util.List;

@Local
public interface ReporteDao extends GenericDao<RemanenteMensual, Integer> {

    public List<UltimoEstadoDTO> getUltimoEstado(Integer año);

    public List<ConteoTramitesDTO> getConteoTramites(Integer año);

}

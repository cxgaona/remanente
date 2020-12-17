package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.dto.ResumenLibroDTO;
import ec.gob.dinardap.remanente.modelo.Tomo;

@Local
public interface TomoDao extends GenericDao<Tomo, Integer> {
    public ResumenLibroDTO getResumenPorInstitucionAñoTipo(Integer institucionId, Integer año, Integer tipoLibroId);
}

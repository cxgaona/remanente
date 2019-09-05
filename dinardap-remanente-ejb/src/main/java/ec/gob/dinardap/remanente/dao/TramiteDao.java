package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.modelo.Tramite;
import java.util.List;

@Local
public interface TramiteDao extends GenericDao<Tramite, Integer> {

    public List<Tramite> getTramiteByInstitucionFechaActividad(Integer idInstitucion, Integer anio, Integer mes, String actividad);
}

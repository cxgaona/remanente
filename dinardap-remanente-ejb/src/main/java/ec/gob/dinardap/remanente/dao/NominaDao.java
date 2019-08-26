package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.modelo.Nomina;
import java.util.List;

@Local
public interface NominaDao extends GenericDao<Nomina, Integer> {

    public List<Nomina> getNominaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes);
}

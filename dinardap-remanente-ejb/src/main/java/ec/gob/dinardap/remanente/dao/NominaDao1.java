package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.modelo.Nomina;
import java.util.List;

@Local
public interface NominaDao1 extends GenericDao<Nomina, Integer> {

    public List<Nomina> getNominaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes);
}

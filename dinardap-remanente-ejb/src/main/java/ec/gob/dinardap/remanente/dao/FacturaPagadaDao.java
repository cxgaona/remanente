package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;
import java.util.List;

@Local
public interface FacturaPagadaDao extends GenericDao<FacturaPagada, Integer> {

    public List<FacturaPagada> getFacturaPagadaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes);
}

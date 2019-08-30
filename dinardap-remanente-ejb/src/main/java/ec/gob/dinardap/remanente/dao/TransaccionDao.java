package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import java.util.List;

@Local
public interface TransaccionDao extends GenericDao<Transaccion, Integer> {
    public Transaccion getTransaccionByInstitucionFechaTipo(Integer idInstitucion, Integer anio, Integer mes, Integer tipo);
}

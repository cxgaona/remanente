package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.modelo.Bandeja;
import java.util.List;

@Local
public interface BandejaDao extends GenericDao<Bandeja, Integer> {
    
public List<Bandeja> getBandejaByUsuarioAñoMes(Integer usuarioId, Integer anio, Integer mes);

}

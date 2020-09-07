package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import java.util.List;

@Local
public interface InstitucionDao extends GenericDao<Institucion, Integer> {
    public List<Institucion> getRegistroMixtoList(Integer direccionRegionalId);
}

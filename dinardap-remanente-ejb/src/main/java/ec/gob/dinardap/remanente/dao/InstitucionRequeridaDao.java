package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import java.util.List;

@Local
public interface InstitucionRequeridaDao extends GenericDao<InstitucionRequerida, Integer> {

    public List<InstitucionRequerida> getDireccionRegionalList();

}

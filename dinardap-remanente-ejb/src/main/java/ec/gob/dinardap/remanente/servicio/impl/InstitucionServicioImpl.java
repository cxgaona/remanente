package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.dao.GenericDao;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.dao.InstitucionDao;
import ec.gob.dinardap.remanente.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import javax.ejb.EJB;

@Stateless(name = "InstitucionServicio")
public class InstitucionServicioImpl extends GenericServiceImpl<Institucion, Integer> implements InstitucionServicio {
    @EJB
    private InstitucionDao institucionDao;
    @Override
    public GenericDao<Institucion, Integer> getDao() {
        return institucionDao;
    }
    
}

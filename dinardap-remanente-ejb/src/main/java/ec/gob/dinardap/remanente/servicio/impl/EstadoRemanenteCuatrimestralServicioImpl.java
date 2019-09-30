package ec.gob.dinardap.remanente.servicio.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.dao.EstadoRemanenteCuatrimestralDao;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteCuatrimestral;
import ec.gob.dinardap.remanente.servicio.EstadoRemanenteCuatrimestralServicio;

@Stateless(name = "EstadoRemanenteCustrimestralServicio")
public class EstadoRemanenteCuatrimestralServicioImpl extends GenericServiceImpl<EstadoRemanenteCuatrimestral, Integer> implements EstadoRemanenteCuatrimestralServicio {

    @EJB
    private EstadoRemanenteCuatrimestralDao estadoRemanenteCuatrimestralDao;

    @Override
    public GenericDao<EstadoRemanenteCuatrimestral, Integer> getDao() {
        return estadoRemanenteCuatrimestralDao;
    }
}

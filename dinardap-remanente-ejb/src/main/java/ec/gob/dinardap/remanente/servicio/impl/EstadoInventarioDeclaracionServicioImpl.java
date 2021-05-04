package ec.gob.dinardap.remanente.servicio.impl;


import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.dao.EstadoInventarioDeclaracionDao;
import ec.gob.dinardap.remanente.modelo.EstadoInventarioDeclaracion;
import ec.gob.dinardap.remanente.servicio.EstadoInventarioDeclaracionServicio;


@Stateless(name = "EstadoInventarioDeclaracionServicio")
public class EstadoInventarioDeclaracionServicioImpl extends GenericServiceImpl<EstadoInventarioDeclaracion, Integer> implements EstadoInventarioDeclaracionServicio {

    @EJB
    private EstadoInventarioDeclaracionDao estadoInventarioDeclaracionDao;

    @Override
    public GenericDao<EstadoInventarioDeclaracion, Integer> getDao() {
        return estadoInventarioDeclaracionDao;
    }

}

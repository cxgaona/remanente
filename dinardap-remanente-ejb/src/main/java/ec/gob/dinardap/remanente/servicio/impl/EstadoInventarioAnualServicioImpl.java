package ec.gob.dinardap.remanente.servicio.impl;


import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.dao.EstadoInventarioAnualDao;
import ec.gob.dinardap.remanente.modelo.EstadoInventarioAnual;
import ec.gob.dinardap.remanente.servicio.EstadoInventarioAnualServicio;


@Stateless(name = "EstadoInventarioAnualServicio")
public class EstadoInventarioAnualServicioImpl extends GenericServiceImpl<EstadoInventarioAnual, Integer> implements EstadoInventarioAnualServicio {

    @EJB
    private EstadoInventarioAnualDao estadoInventarioAnualDao;

    @Override
    public GenericDao<EstadoInventarioAnual, Integer> getDao() {
        return estadoInventarioAnualDao;
    }

}

package ec.gob.dinardap.remanente.servicio.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.dao.EstadoRemanenteMensualDao;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteMensual;
import ec.gob.dinardap.remanente.servicio.EstadoRemanenteMensualServicio;

@Stateless(name = "EstadoRemanenteMensualServicio")
public class EstadoRemanenteMensualServicioImpl extends GenericServiceImpl<EstadoRemanenteMensual, Integer> implements EstadoRemanenteMensualServicio {

    @EJB
    private EstadoRemanenteMensualDao estadoRemanenteMensualDao;

    @Override
    public GenericDao<EstadoRemanenteMensual, Integer> getDao() {
        return estadoRemanenteMensualDao;
    }

    @Override
    public void createEstadoRemanenteMensual(EstadoRemanenteMensual erm) {
        this.create(erm);
    }

}

package ec.gob.dinardap.remanente.servicio.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.dao.TransaccionDao;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;

@Stateless(name = "TransaccionServicio")
public class TransaccionServicioImpl extends GenericServiceImpl<Transaccion, Integer> implements TransaccionServicio {

    @EJB
    private TransaccionDao transaccionDao;

    @Override
    public GenericDao<Transaccion, Integer> getDao() {
        return transaccionDao;
    }

    @Override
    public void editTransaccion(Transaccion transaccion) {
        this.update(transaccion);
    }
}

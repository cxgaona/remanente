package ec.gob.dinardap.remanente.servicio.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.dao.RemanenteHistoricoDao;
import ec.gob.dinardap.remanente.modelo.RemanenteHistorico;

import ec.gob.dinardap.remanente.servicio.RemanenteHistoricoServicio;

@Stateless(name = "RemanenteHistoricoServicio")
public class RemanenteHistoricoServicioImpl extends GenericServiceImpl<RemanenteHistorico, Integer> implements RemanenteHistoricoServicio {

    @EJB
    private RemanenteHistoricoDao remanenteHistoricoDao;

    @Override
    public GenericDao<RemanenteHistorico, Integer> getDao() {
        return remanenteHistoricoDao;
    }
    
}

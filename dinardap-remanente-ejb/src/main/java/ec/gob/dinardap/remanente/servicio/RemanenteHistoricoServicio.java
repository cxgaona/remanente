package ec.gob.dinardap.remanente.servicio;


import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.RemanenteHistorico;


@Local
public interface RemanenteHistoricoServicio extends GenericService<RemanenteHistorico, Integer> {   

}

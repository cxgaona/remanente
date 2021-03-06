package ec.gob.dinardap.remanente.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.DiasNoLaborables;

@Local
public interface DiasNoLaborablesServicio extends GenericService<DiasNoLaborables, Integer> {

    public Boolean habilitarDiasAdicionales(Integer año, Integer mes);

    public Boolean habilitarDiasAdicionales(Integer año, Integer mes, Integer remanenteMensualId);

    public Boolean habilitarDiasAdicionalesCS(Integer año, Integer mes, Integer dia, Integer remanenteMensualId);

}

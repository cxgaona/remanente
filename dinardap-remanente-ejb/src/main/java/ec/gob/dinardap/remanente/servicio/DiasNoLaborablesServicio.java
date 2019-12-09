package ec.gob.dinardap.remanente.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.DiasNoLaborables;
import java.util.Date;
import java.util.List;

@Local
public interface DiasNoLaborablesServicio extends GenericService<DiasNoLaborables, Integer> {

    public List<Date> diasFestivosAtivos();

    public Boolean habilitarDiasAdicionales(Integer mes);

}

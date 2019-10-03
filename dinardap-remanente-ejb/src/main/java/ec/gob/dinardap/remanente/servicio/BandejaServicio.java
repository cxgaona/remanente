package ec.gob.dinardap.remanente.servicio;

import java.util.List;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.Bandeja;

@Local
public interface BandejaServicio extends GenericService<Bandeja, Integer> {   

    public List<Bandeja> getBandejaByUsuarioAñoMes(Integer usuarioId, Integer año, Integer mes);
    
    public void crearBandeja(Bandeja bandeja);
    
    public void editBandeja(Bandeja bandeja);

}

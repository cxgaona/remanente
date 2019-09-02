package ec.gob.dinardap.remanente.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.Usuario;
import java.util.List;

@Local
public interface UsuarioServicio extends GenericService<Usuario, Integer> {

    public List<Usuario> getUsuariosActivos();

    public void createUsuarioACtivo();

    public void editUsuarioActivo();

}

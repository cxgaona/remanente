package ec.gob.dinardap.remanente.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.Usuario;
import java.util.List;

@Local
public interface UsuarioServicio extends GenericService<Usuario, Integer> {

    public List<Usuario> getUsuariosActivos();

    public void createUsuario(Usuario usuario);

    public void editUsuario(Usuario usuario);

    public Usuario login(String usuario, String contraseña);
    
    public Usuario getUsuarioByUsername(String username);
    
    public List<Usuario> getUsuarioByIstitucionRol(InstitucionRequerida institucion, String rolAsignado, String rolSolicitante, Integer idDinardap, RemanenteCuatrimestral remanenteCuatrimestral);

}

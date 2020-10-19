package ec.gob.dinardap.remanente.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.servicio.GenericService;
import ec.gob.dinardap.seguridad.modelo.UsuarioPerfil;

@Local
public interface UsuarioPerfilServicio extends GenericService<UsuarioPerfil, Integer> {    

}

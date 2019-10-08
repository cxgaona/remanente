package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.dto.UsuarioDTO;
import ec.gob.dinardap.remanente.modelo.Usuario;

@Local
public interface UsuarioDao extends GenericDao<Usuario, Integer> {

    public Usuario login(String usuario, String contraseña);
}

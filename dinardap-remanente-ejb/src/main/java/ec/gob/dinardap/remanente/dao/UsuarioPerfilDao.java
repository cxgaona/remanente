package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.seguridad.modelo.UsuarioPerfil;
import java.util.List;

@Local
public interface UsuarioPerfilDao extends GenericDao<UsuarioPerfil, Integer> {

    public List<UsuarioPerfil> getUsuarioPerfilListPorUsuarioActivo(Integer usuarioId);

    public List<UsuarioPerfil> getUsuarioPerfilListPorUsuarioInactivo(Integer usuarioId);

}

package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.dto.UsuarioDTO;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import java.util.List;

@Local
public interface UsuarioDao extends GenericDao<Usuario, Integer> {

    public List<Usuario> getUsuarioActivos(Integer sistemaID);

    public UsuarioDTO getUsuarioByCedula(String cedula);
}

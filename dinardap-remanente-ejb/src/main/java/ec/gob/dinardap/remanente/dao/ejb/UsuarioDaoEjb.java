package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.constante.SistemaIdEnum;
import ec.gob.dinardap.remanente.dao.UsuarioDao;
import ec.gob.dinardap.remanente.dto.UsuarioDTO;
import ec.gob.dinardap.seguridad.modelo.AsignacionInstitucion;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.modelo.UsuarioPerfil;
import ec.gob.dinardap.util.constante.EstadoEnum;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;

@Stateless(name = "UsuarioDao")
public class UsuarioDaoEjb extends RemanenteGenericDao<Usuario, Integer> implements UsuarioDao {

    public UsuarioDaoEjb() {
        super(Usuario.class);
    }

    @Override
    public List<Usuario> getUsuarioActivos(Integer sistemaID) {
        List<Usuario> usuarioList = new ArrayList<Usuario>();
        Query query = em.createQuery("SELECT u FROM Usuario u "
                + "WHERE u.estado=:estado");
//        Query query = em.createQuery("SELECT u FROM Usuario u INNER JOIN u.usuarioPerfilList upl "
//                + "WHERE upl.perfil.sistema.sistemaId=:sistemaId "
//                + "AND upl.perfil.sistema.estado=:estado "
//                + "AND u.estado=:estado ");
//        query.setParameter("sistemaId", sistemaID);
        query.setParameter("estado", EstadoEnum.ACTIVO.getEstado());

        usuarioList = query.getResultList();
        for (Usuario u : usuarioList) {
            for (AsignacionInstitucion ai : u.getAsignacionInstitucions()) {
                ai.getAsignacionInstitucionId();
            }
            for (UsuarioPerfil up : u.getUsuarioPerfilList()) {
                up.getUsuarioPerfilId();
            }
        }
        return usuarioList;
    }

    @Override
    public UsuarioDTO getUsuarioByCedula(String cedula) {
        List<Usuario> usuarioList = new ArrayList<Usuario>();
        Query query = em.createQuery("SELECT u FROM Usuario u INNER JOIN u.usuarioPerfilList upl WHERE upl.perfil.sistema.sistemaId=:sistemaID AND u.cedula=:cedula ORDER BY u.usuarioId ASC");
        query.setParameter("cedula", cedula);
        query.setParameter("sistemaID", SistemaIdEnum.REMANENTES_SISTEMA_ID.getSistemaId());
        usuarioList = query.getResultList();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        for (Usuario u : usuarioList) {
            for (AsignacionInstitucion ai : u.getAsignacionInstitucions()) {
                ai.getAsignacionInstitucionId();
            }
            for (UsuarioPerfil up : u.getUsuarioPerfilList()) {
                up.getUsuarioPerfilId();
            }
        }
        if (!usuarioList.isEmpty()) {
            Usuario u = new Usuario();
            u = usuarioList.get(usuarioList.size() - 1);
            usuarioDTO.setUsuario(u);
            usuarioDTO.setInstitucion(u.getAsignacionInstitucions().get(u.getAsignacionInstitucions().size() - 1).getInstitucion());
            List<String> strPerfilList = new ArrayList<String>();
            for (UsuarioPerfil up : u.getUsuarioPerfilList()) {
                strPerfilList.add(up.getPerfil().getNombre());
            }
            usuarioDTO.setPerfil(StringUtils.join(strPerfilList, " / "));
        }
        return usuarioDTO;
    }
}

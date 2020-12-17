package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.constante.SistemaIdEnum;
import ec.gob.dinardap.remanente.dao.UsuarioDao;
import ec.gob.dinardap.remanente.dto.UsuarioDTO;
import ec.gob.dinardap.seguridad.modelo.AsignacionInstitucion;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.modelo.UsuarioPerfil;
import ec.gob.dinardap.util.constante.EstadoEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
            List<AsignacionInstitucion> asignacionInstitucionList = new ArrayList<AsignacionInstitucion>();
            for (AsignacionInstitucion ai : u.getAsignacionInstitucions()) {
                if (ai.getEstado().equals(EstadoEnum.ACTIVO.getEstado())) {
                    asignacionInstitucionList.add(ai);
                }
            }
            Collections.sort(asignacionInstitucionList, new Comparator<AsignacionInstitucion>() {
                @Override
                public int compare(AsignacionInstitucion ai1, AsignacionInstitucion ai2) {
                    return new Integer(ai1.getAsignacionInstitucionId()).compareTo(new Integer(ai2.getAsignacionInstitucionId()));
                }
            });
            u.setAsignacionInstitucions(asignacionInstitucionList);
            
            
            List<UsuarioPerfil> usuarioPerfilList = new ArrayList<UsuarioPerfil>();
            for (UsuarioPerfil up : u.getUsuarioPerfilList()) {
                if (up.getEstado().equals(EstadoEnum.ACTIVO.getEstado())) {
                    usuarioPerfilList.add(up);
                }
            }
            Collections.sort(usuarioPerfilList, new Comparator<UsuarioPerfil>() {
                @Override
                public int compare(UsuarioPerfil up1, UsuarioPerfil up2) {
                    return new Integer(up1.getUsuarioPerfilId()).compareTo(new Integer(up2.getUsuarioPerfilId()));
                }
            });
            u.setUsuarioPerfilList(usuarioPerfilList);
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
            List<AsignacionInstitucion> asignacionInstitucionList = new ArrayList<AsignacionInstitucion>();
            for (AsignacionInstitucion ai : u.getAsignacionInstitucions()) {
                if (ai.getEstado().equals(EstadoEnum.ACTIVO.getEstado())) {
                    asignacionInstitucionList.add(ai);
                }
            }
            Collections.sort(asignacionInstitucionList, new Comparator<AsignacionInstitucion>() {
                @Override
                public int compare(AsignacionInstitucion ai1, AsignacionInstitucion ai2) {
                    return new Integer(ai1.getAsignacionInstitucionId()).compareTo(new Integer(ai2.getAsignacionInstitucionId()));
                }
            });
            u.setAsignacionInstitucions(asignacionInstitucionList);
            
            
            List<UsuarioPerfil> usuarioPerfilList = new ArrayList<UsuarioPerfil>();
            for (UsuarioPerfil up : u.getUsuarioPerfilList()) {
                if (up.getEstado().equals(EstadoEnum.ACTIVO.getEstado())) {
                    usuarioPerfilList.add(up);
                }
            }
            Collections.sort(usuarioPerfilList, new Comparator<UsuarioPerfil>() {
                @Override
                public int compare(UsuarioPerfil up1, UsuarioPerfil up2) {
                    return new Integer(up1.getUsuarioPerfilId()).compareTo(new Integer(up2.getUsuarioPerfilId()));
                }
            });
            u.setUsuarioPerfilList(usuarioPerfilList);
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

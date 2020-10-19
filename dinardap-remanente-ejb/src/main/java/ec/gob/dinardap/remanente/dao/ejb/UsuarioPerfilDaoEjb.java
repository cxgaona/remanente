package ec.gob.dinardap.remanente.dao.ejb;

import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.ejb.GenericDaoEjb;
import ec.gob.dinardap.remanente.constante.SistemaIdEnum;
import ec.gob.dinardap.remanente.dao.UsuarioPerfilDao;
import ec.gob.dinardap.seguridad.modelo.UsuarioPerfil;
import ec.gob.dinardap.util.constante.EstadoEnum;
import java.util.List;
import javax.persistence.Query;

@Stateless(name = "UsuarioPerfilDao")
public class UsuarioPerfilDaoEjb extends GenericDaoEjb<UsuarioPerfil, Integer> implements UsuarioPerfilDao {

    public UsuarioPerfilDaoEjb() {
        super(UsuarioPerfil.class);
    }

    @Override
    public List<UsuarioPerfil> getUsuarioPerfilListPorUsuarioActivo(Integer usuarioId) {
        Query query = em.createQuery("SELECT up FROM UsuarioPerfil up WHERE up.usuario.usuarioId=:usuarioID AND up.perfil.sistema.sistemaId=:sistemaID AND up.perfil.sistema.estado=:estado AND up.estado=:estado");
        query.setParameter("estado", EstadoEnum.ACTIVO.getEstado());
        query.setParameter("usuarioID", usuarioId);
        query.setParameter("sistemaID", SistemaIdEnum.REMANENTES_SISTEMA_ID.getSistemaId());
        return query.getResultList();
    }

    @Override
    public List<UsuarioPerfil> getUsuarioPerfilListPorUsuarioInactivo(Integer usuarioId) {
        Query query = em.createQuery("SELECT up FROM UsuarioPerfil up WHERE up.usuario.usuarioId=:usuarioID AND up.perfil.sistema.sistemaId=:sistemaID AND up.perfil.sistema.estado=:estadoActivo AND up.estado=:estadoInactivo");
        query.setParameter("estadoActivo", EstadoEnum.ACTIVO.getEstado());
        query.setParameter("estadoInactivo", EstadoEnum.INACTIVO.getEstado());
        query.setParameter("usuarioID", usuarioId);
        query.setParameter("sistemaID", SistemaIdEnum.REMANENTES_SISTEMA_ID.getSistemaId());
        return query.getResultList();
    }

}

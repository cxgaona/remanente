package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.UsuarioDao;
import ec.gob.dinardap.remanente.modelo.Usuario;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless(name = "UsuarioDao")
public class UsuarioDaoEjb extends RemanenteGenericDao<Usuario, Integer> implements UsuarioDao {

    public UsuarioDaoEjb() {
        super(Usuario.class);
    }

    @Override
    public Usuario login(String usuario, String contraseña) {
        Query query = em.createQuery("SELECT u FROM Usuario u "
                + "WHERE u.usuario=:usuario "
                + "and u.contrasena=:contrasena");
        query.setParameter("usuario", usuario);
        query.setParameter("contrasena", contraseña);
        Usuario u = new Usuario();
        if (query.getResultList().size() != 0) {
            u = (Usuario) query.getResultList().get(0);
            if (u.getInstitucionId().getGad() == null) {
                u.getInstitucionId().setGad(null);
            }
            return u;
        } else {
            return null;
        }
    }

}

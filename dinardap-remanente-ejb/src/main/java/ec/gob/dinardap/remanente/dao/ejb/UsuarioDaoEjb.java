package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.UsuarioDao;
import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless(name = "UsuarioDao")
public class UsuarioDaoEjb extends RemanenteGenericDao<Usuario, Integer> implements UsuarioDao {

    public UsuarioDaoEjb() {
        super(Usuario.class);
    }

    @Override
    public Usuario login(String usuario, String contraseña) {
        System.out.println("===usuario DAO===");
        Query query = em.createQuery("SELECT u FROM Usuario u "
                + "WHERE u.usuario=:usuario "
                + "and u.contrasena=:contrasena");
        query.setParameter("usuario", usuario);
        query.setParameter("contrasena", contraseña);
        Usuario u = new Usuario();
        if (query.getResultList().size() != 0) {
            u = (Usuario) query.getResultList().get(0);
            System.out.println("Usuario: " + u.getInstitucionId().getInstitucionId().toString());
            if (u.getInstitucionId().getGad() == null) {
                System.out.println("ntro en el if");
                u.getInstitucionId().setGad(null);
                System.out.println("Acabo el if");
            }
            return u;
        } else {
            return null;
        }
    }

}

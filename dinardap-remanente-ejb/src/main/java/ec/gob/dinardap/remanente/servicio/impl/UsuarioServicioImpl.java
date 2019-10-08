package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.UsuarioDao;
import ec.gob.dinardap.remanente.dto.UsuarioDTO;
import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.Usuario;
import ec.gob.dinardap.remanente.servicio.UsuarioServicio;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "UsuarioServicio")
public class UsuarioServicioImpl extends GenericServiceImpl<Usuario, Integer> implements UsuarioServicio {

    @EJB
    private UsuarioDao usuarioDao;

    @Override
    public GenericDao<Usuario, Integer> getDao() {
        return usuarioDao;
    }

    @Override
    public List<Usuario> getUsuariosActivos() {
        List<Usuario> usuarioList = new ArrayList<Usuario>();
        String[] criteriaNombres = {"estado"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_EQUALS};
        Object[] criteriaValores = {"A"};
        String[] orderBy = {"usuarioId"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        usuarioList = findByCriterias(criteria);
        return usuarioList;
    }

    @Override
    public void createUsuario(Usuario usuario) {
        this.create(usuario);
    }

    @Override
    public void editUsuario(Usuario usuario) {
        this.update(usuario);
    }

    @Override
    public UsuarioDTO login(String usuario, String contraseña) {
        UsuarioDTO udto = new UsuarioDTO();
        try {
            List<Usuario> usuarioList = new ArrayList<Usuario>();
            String[] criteriaNombres = {"usuario", "contrasena", "estado"};
            CriteriaTypeEnum[] criteriaTipos = {
                CriteriaTypeEnum.STRING_EQUALS,
                CriteriaTypeEnum.STRING_EQUALS,
                CriteriaTypeEnum.STRING_EQUALS
            };
            Object[] criteriaValores = {usuario, contraseña, "A"};
            String[] orderBy = {"usuarioId"};
            boolean[] asc = {false};
            Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
            usuarioList = findByCriterias(criteria);
            Usuario u = new Usuario();
            u = usuarioList.get(0);
            System.out.println("===Usuario==");
            System.out.println("Usuario: " + u.getNombre());
            System.out.println("Usuario: " + u.getUsuarioId());
            System.out.println("Usuario: " + u.getUsuario());
            System.out.println("Usuario: " + u.getInstitucionId().getInstitucionId());
            udto.setUsuarioID(u.getUsuarioId());
            udto.setInstitucionID(u.getInstitucionId().getInstitucionId());
            udto.setRegistrador(u.getRegistrador());
            udto.setVerificador(u.getVerificador());
            udto.setValidador(u.getValidador());
            udto.setAdministrador(u.getAdministrador());
            if (u.getInstitucionId().getGad() != null) {
                System.out.println("Dentro del if");
                udto.setGadID(u.getInstitucionId().getGad().getInstitucionId());
            } else {
                udto.setGadID(-1);
            }          

//        System.out.println("Usuario: " + uu.getInstitucionId().getGad().getInstitucionId());
//        System.out.println("Fin del Servicio");
            System.out.println("DTO: " + udto.getGadID());
            return udto;
        } catch (Exception e) {
            e.printStackTrace();
            return udto;
        }
    }

}

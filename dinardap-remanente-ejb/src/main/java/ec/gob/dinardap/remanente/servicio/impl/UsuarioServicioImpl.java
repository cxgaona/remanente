package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.UsuarioDao;
import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
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
    public Usuario login(String usuario, String contraseña) {
        Usuario u = new Usuario();
        u = usuarioDao.login(usuario, contraseña);
        return u;
    }

    @Override
    public Usuario getUsuarioByUsername(String username) {
        Usuario user = new Usuario();
        String[] criteriaNombres = {"usuario", "estado"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_EQUALS, CriteriaTypeEnum.STRING_EQUALS};
        Object[] criteriaValores = {username, "A"};
        String[] orderBy = {"usuarioId"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        System.out.println("tamaño: " + findByCriterias(criteria).size());
        if (findByCriterias(criteria).size() != 0) {
            user = findByCriterias(criteria).get(0);
        }
        return user;
    }

    @Override
    public List<Usuario> getUsuarioByIstitucionRol(InstitucionRequerida institucion, String rolAsignado, String rolSolicitante, Integer idDinardap, RemanenteCuatrimestral remanenteCuatrimestral) {
        List<Usuario> userList = new ArrayList<Usuario>();
        switch (rolSolicitante) {
            case "REM-Registrador":
                if (rolAsignado.equals("REM-Verificador")) {
                    Integer idInstitucionBusqueda = 0;
                    String[] criteriaNombres = {"institucionId.institucionId", "verificador", "estado"};
                    CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.BOOLEAN_POSTGRESQL, CriteriaTypeEnum.STRING_EQUALS};
                    if (institucion.getTipo().equals("SIN GAD")) {
                        idInstitucionBusqueda = institucion.getInstitucionId();
                    } else if (institucion.getTipo().equals("CON GAD")) {
                        idInstitucionBusqueda = institucion.getGad().getInstitucionId();
                    }
                    Object[] criteriaValores = {idInstitucionBusqueda, Boolean.TRUE, "A"};
                    String[] orderBy = {"usuarioId"};
                    boolean[] asc = {true};
                    Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
                    System.out.println("tamañoVerificador: " + findByCriterias(criteria).size());
                    userList = findByCriterias(criteria);
                } else if (rolAsignado.equals("REM-Administrador")) {
                    String[] criteriaNombres = {"institucionId.institucionId", "administrador", "estado"};
                    CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.BOOLEAN_POSTGRESQL, CriteriaTypeEnum.STRING_EQUALS};
                    Object[] criteriaValores = {idDinardap, Boolean.TRUE, "A"};
                    String[] orderBy = {"usuarioId"};
                    boolean[] asc = {true};
                    Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
                    System.out.println("tamañoAdministrador: " + findByCriterias(criteria).size());
                    userList = findByCriterias(criteria);
                } else if (rolAsignado.equals("REM-Validador")) {
                    String[] criteriaNombres = {"institucionId.institucionId", "validador", "estado"};
                    CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.BOOLEAN_POSTGRESQL, CriteriaTypeEnum.STRING_EQUALS};
                    Object[] criteriaValores = {institucion.getDireccionRegional().getInstitucionId(), Boolean.TRUE, "A"};
                    String[] orderBy = {"usuarioId"};
                    boolean[] asc = {true};
                    Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
                    System.out.println("tamañoValidador: " + findByCriterias(criteria).size());
                    userList = findByCriterias(criteria);
                }
                break;
            case "REM-Verificador":
                if (rolAsignado.equals("REM-Registrador")) {
                    String criterio = "";
                    if (institucion.getTipo().equals("SIN GAD")) {
                        criterio = "institucionId.institucionId";
                    } else if (institucion.getTipo().equals("GAD")) {
                        criterio = "institucionId.gad.institucionId";
                    }
                    String[] criteriaNombres = {criterio, "registrador", "estado"};
                    CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.BOOLEAN_POSTGRESQL, CriteriaTypeEnum.STRING_EQUALS};
                    Object[] criteriaValores = {institucion.getInstitucionId(), Boolean.TRUE, "A"};
                    String[] orderBy = {"usuarioId"};
                    boolean[] asc = {true};
                    Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
                    System.out.println("tamañoRegistrador: " + findByCriterias(criteria).size());
                    userList = findByCriterias(criteria);
                } else if (rolAsignado.equals("REM-Validador")) {
                    String[] criteriaNombres = {"institucionId.institucionId", "validador", "estado"};
                    CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.BOOLEAN_POSTGRESQL, CriteriaTypeEnum.STRING_EQUALS};
                    Object[] criteriaValores = {institucion.getDireccionRegional().getInstitucionId(), Boolean.TRUE, "A"};
                    String[] orderBy = {"usuarioId"};
                    boolean[] asc = {true};
                    Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
                    System.out.println("tamañoValidador: " + findByCriterias(criteria).size());
                    userList = findByCriterias(criteria);
                }
                break;
            case "REM-Validador":
                if (rolAsignado.equals("REM-Registrador")) {
                    String[] criteriaNombres = {"institucionId.institucionId", "registrador", "estado"};
                    CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.BOOLEAN_POSTGRESQL, CriteriaTypeEnum.STRING_EQUALS};
                    Object[] criteriaValores = {remanenteCuatrimestral.getRemanenteAnual().getInstitucionRequerida().getInstitucionId(), Boolean.TRUE, "A"};
                    String[] orderBy = {"usuarioId"};
                    boolean[] asc = {true};
                    Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
                    System.out.println("tamaño: " + findByCriterias(criteria).size());
                    userList = findByCriterias(criteria);
                } else if (rolAsignado.equals("REM-Verificador")) {
                    String[] criteriaNombres = {"institucionId.institucionId", "verificador", "estado"};
                    CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.BOOLEAN_POSTGRESQL, CriteriaTypeEnum.STRING_EQUALS};
                    Object[] criteriaValores = {remanenteCuatrimestral.getRemanenteAnual().getInstitucionRequerida().getInstitucionId(), Boolean.TRUE, "A"};
                    String[] orderBy = {"usuarioId"};
                    boolean[] asc = {true};
                    Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
                    System.out.println("tamaño: " + findByCriterias(criteria).size());
                    userList = findByCriterias(criteria);
                }
                break;
            case "REM-Administrador":
                if (rolAsignado.equals("REM-Registrador")) {
                    String[] criteriaNombres = {"institucionId.institucionId", "registrador", "estado"};
                    CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.BOOLEAN_POSTGRESQL, CriteriaTypeEnum.STRING_EQUALS};
                    Object[] criteriaValores = {remanenteCuatrimestral.getRemanenteAnual().getInstitucionRequerida().getInstitucionId(), Boolean.TRUE, "A"};
                    String[] orderBy = {"usuarioId"};
                    boolean[] asc = {true};
                    Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
                    System.out.println("tamaño: " + findByCriterias(criteria).size());
                    userList = findByCriterias(criteria);
                } else if (rolAsignado.equals("REM-Verificador")) {
                    String[] criteriaNombres = {"institucionId.institucionId", "verificador", "estado"};
                    CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.BOOLEAN_POSTGRESQL, CriteriaTypeEnum.STRING_EQUALS};
                    Object[] criteriaValores = {remanenteCuatrimestral.getRemanenteAnual().getInstitucionRequerida().getInstitucionId(), Boolean.TRUE, "A"};
                    String[] orderBy = {"usuarioId"};
                    boolean[] asc = {true};
                    Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
                    System.out.println("tamaño: " + findByCriterias(criteria).size());
                    userList = findByCriterias(criteria);
                } else if (rolAsignado.equals("REM-Validador")) {
                    String[] criteriaNombres = {"institucionId.institucionId", "validador", "estado"};
                    CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.BOOLEAN_POSTGRESQL, CriteriaTypeEnum.STRING_EQUALS};
                    Object[] criteriaValores = {remanenteCuatrimestral.getRemanenteAnual().getInstitucionRequerida().getInstitucionId(), Boolean.TRUE, "A"};
                    String[] orderBy = {"usuarioId"};
                    boolean[] asc = {true};
                    Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
                    System.out.println("tamaño: " + findByCriterias(criteria).size());
                    userList = findByCriterias(criteria);
                }
                break;
        }
        return userList;
    }

}

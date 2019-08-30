package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.UsuarioDao;
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
    public void createUsuarioACtivo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void editUsuarioActivo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

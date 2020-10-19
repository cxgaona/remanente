package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import ec.gob.dinardap.persistence.dao.GenericDao;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.AsignacionInstitucionDao;
import ec.gob.dinardap.remanente.servicio.AsignacionInstitucionServicio;
import ec.gob.dinardap.seguridad.modelo.AsignacionInstitucion;
import ec.gob.dinardap.util.constante.EstadoEnum;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

@Stateless(name = "AsignacionInstitucionServicio")
public class AsignacionInstitucionServicioImpl extends GenericServiceImpl<AsignacionInstitucion, Integer> implements AsignacionInstitucionServicio {

    @EJB
    private AsignacionInstitucionDao asignacionInstitucionDao;

    @Override
    public GenericDao<AsignacionInstitucion, Integer> getDao() {
        return asignacionInstitucionDao;
    }

    @Override
    public AsignacionInstitucion getAsignacionPorUsuarioActivo(Integer usuarioId) {
        AsignacionInstitucion asignacionInstitucion = new AsignacionInstitucion();
        List<AsignacionInstitucion> asignacionInstitucionList = new ArrayList<AsignacionInstitucion>();
        String[] criteriaNombres = {"usuario.usuarioId", "estado"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.SHORT_EQUALS};
        Object[] criteriaValores = {usuarioId, EstadoEnum.ACTIVO.getEstado()};
        String[] orderBy = {"asignacionInstitucionId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        asignacionInstitucionList = findByCriterias(criteria);
        if (!asignacionInstitucionList.isEmpty()) {
            asignacionInstitucion = asignacionInstitucionList.get(asignacionInstitucionList.size() - 1);
        }
        return asignacionInstitucion;
    }

    @Override
    public AsignacionInstitucion getAsignacionPorUsuarioInactivo(Integer usuarioId, Integer institucionId) {
        AsignacionInstitucion asignacionInstitucion = new AsignacionInstitucion();
        List<AsignacionInstitucion> asignacionInstitucionList = new ArrayList<AsignacionInstitucion>();
        String[] criteriaNombres = {"usuario.usuarioId", "institucion.institucionId", "estado"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.SHORT_EQUALS};
        Object[] criteriaValores = {usuarioId, institucionId, EstadoEnum.INACTIVO.getEstado()};
        String[] orderBy = {"asignacionInstitucionId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        asignacionInstitucionList = findByCriterias(criteria);
        if (!asignacionInstitucionList.isEmpty()) {
            asignacionInstitucion = asignacionInstitucionList.get(asignacionInstitucionList.size() - 1);
        }
        return asignacionInstitucion;
    }

}

package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.TomoDao;

import ec.gob.dinardap.remanente.modelo.Tomo;
import ec.gob.dinardap.remanente.servicio.TomoServicio;
import ec.gob.dinardap.util.constante.EstadoEnum;
import java.util.ArrayList;

@Stateless(name = "TomoServicio")
public class TomoServicioImpl extends GenericServiceImpl<Tomo, Integer> implements TomoServicio {

    @EJB
    private TomoDao tomoDao;

    @Override
    public GenericDao<Tomo, Integer> getDao() {
        return tomoDao;
    }

    @Override
    public List<Tomo> getTomosActivosPorLibro(Integer libroId) {
        List<Tomo> tomoList = new ArrayList<Tomo>();
        String[] criteriaNombres = {"estado", "libro.libroId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.SHORT_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {EstadoEnum.ACTIVO.getEstado(), libroId};
        String[] orderBy = {"tomoId"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        tomoList = findByCriterias(criteria);        
        return tomoList;
    }
    
    @Override
    public List<Tomo> getTomosPorInstitucionAñoTipo(Integer institucionId, Integer año, Integer tipoLibroId) {
        List<Tomo> tomoList = new ArrayList<Tomo>();
        String[] criteriaNombres = {"estado", "libro.inventarioAnual.institucion.institucionId", "libro.inventarioAnual.anio", "libro.estado", "libro.tipoLibro.tipoLibroId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.SHORT_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.SHORT_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {EstadoEnum.ACTIVO.getEstado(), institucionId, año, EstadoEnum.ACTIVO.getEstado(), tipoLibroId};
        String[] orderBy = {"tomoId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        tomoList = findByCriterias(criteria);
        return tomoList;
    }

}

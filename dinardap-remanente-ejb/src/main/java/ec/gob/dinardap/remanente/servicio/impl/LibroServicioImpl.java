package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.LibroDao;
import ec.gob.dinardap.remanente.modelo.Libro;
import ec.gob.dinardap.remanente.modelo.Tomo;

import ec.gob.dinardap.remanente.servicio.LibroServicio;
import ec.gob.dinardap.util.constante.EstadoEnum;
import java.util.ArrayList;

@Stateless(name = "LibroServicio")
public class LibroServicioImpl extends GenericServiceImpl<Libro, Integer> implements LibroServicio {

    @EJB
    private LibroDao libroDao;

    @Override
    public GenericDao<Libro, Integer> getDao() {
        return libroDao;
    }

    @Override
    public List<Libro> getLibrosActivosPorInventarioTipo(Integer inventarioAnualId, Integer tipoLibroId) {
        List<Libro> libroList = new ArrayList<Libro>();
        String[] criteriaNombres = {"estado", "inventarioAnual.inventarioAnualId", "tipoLibro.tipoLibroId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.SHORT_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {EstadoEnum.ACTIVO.getEstado(), inventarioAnualId, tipoLibroId};
        String[] orderBy = {"libroId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        libroList = findByCriterias(criteria); 
        for(Libro libro:libroList){
            for(Tomo tomo:libro.getTomoList()){
                tomo.getTomoId();
            }
        }
        return libroList;
    }

}

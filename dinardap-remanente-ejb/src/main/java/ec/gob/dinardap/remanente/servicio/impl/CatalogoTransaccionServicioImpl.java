package ec.gob.dinardap.remanente.servicio.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.dao.CatalogoTransaccionDao;
import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import ec.gob.dinardap.remanente.servicio.CatalogoTransaccionServicio;
import java.util.ArrayList;

@Stateless(name = "CatalogoTransaccionServicio")
public class CatalogoTransaccionServicioImpl extends GenericServiceImpl<CatalogoTransaccion, Integer> implements CatalogoTransaccionServicio {

    @EJB
    private CatalogoTransaccionDao catalogoTransaccionDao;

    @Override
    public GenericDao<CatalogoTransaccion, Integer> getDao() {
        return catalogoTransaccionDao;
    }

    @Override
    public List<CatalogoTransaccion> getCatalogoTransaccionList() {
        List<CatalogoTransaccion> catalogoTransacciones = new ArrayList<>();
        catalogoTransacciones = catalogoTransaccionDao.findAll();
        return catalogoTransacciones;
    }

//    public List<CatalogoTransaccion> getCatalogoTransaccionList() {
//        List<CatalogoTransaccion> valor = new ArrayList<>();
//        //valor = catalogoTransaccionDao.findAll();
//        
//        String[] criteriaNombres = {"tipo"};
//	CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_EQUALS};
//	Object[] criteriaValores = {"asasd"};
//	String[] orderBy = {"nombre"};
//	boolean[] asc = {true};
//		
//		//Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores);
//	Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
//	valor = findByCriterias(criteria);
//        
//        System.out.println(valor.size());
//        return valor;
//        
//    }
//
//    @Override
//    public List<Pregunta> obtenerPreguntasPorUsuario(String usuaioIS) {
//        String[] criteriaNombres = {"registradoPor"};
//        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_EQUALS};
//        Object[] criteriaValores = {usuaioIS};
//        String[] orderBy = {"preguntaId"};
//        boolean[] asc = {true};
//
//        //Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores);
//        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
//        return findByCriterias(criteria);
//    }
//
//    @Override
//    public List<Pregunta> obtenerPreguntasPorValidar() {
//        String[] criteriaNombres = {"estado"};
//        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.SHORT_EQUALS, CriteriaTypeEnum.STRING_IN_LIST};
//        Object[] criteriaValores = {EstadoEnum.CREADO.getEstado(), EstadoEnum.REVISADO.getEstado()};
//        String[] orderBy = {"preguntaId"};
//        boolean[] asc = {true};
//
//        //Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores);
//        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, null, null, null, orderBy, asc);
//        return findByCriterias(criteria);
//    }
//
//    @Override
//    public void guardarValidacion(PreguntaDto preguntaDto) {
//        preguntaDto.getPregunta().setFechaRevision(new Timestamp(new Date().getTime()));
//        this.update(preguntaDto.getPregunta());
//
//        preguntaDto.getRespuestaA().setEstado(preguntaDto.getPregunta().getEstado());
//        preguntaDto.getRespuestaB().setEstado(preguntaDto.getPregunta().getEstado());
//        preguntaDto.getRespuestaC().setEstado(preguntaDto.getPregunta().getEstado());
//        preguntaDto.getRespuestaD().setEstado(preguntaDto.getPregunta().getEstado());
//        respuestaServicio.update(preguntaDto.getRespuestaA());
//        respuestaServicio.update(preguntaDto.getRespuestaB());
//        respuestaServicio.update(preguntaDto.getRespuestaC());
//        respuestaServicio.update(preguntaDto.getRespuestaD());
//
//    }
}

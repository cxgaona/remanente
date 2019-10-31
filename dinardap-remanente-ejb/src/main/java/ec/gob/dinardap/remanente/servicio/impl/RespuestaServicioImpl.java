package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.RespuestaDao;
import ec.gob.dinardap.remanente.modelo.Pregunta;
import ec.gob.dinardap.remanente.modelo.Respuesta;
import ec.gob.dinardap.remanente.servicio.RespuestaServicio;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "RespuestaServicio")
public class RespuestaServicioImpl extends GenericServiceImpl<Respuesta, Integer> implements RespuestaServicio {

    @EJB
    private RespuestaDao respuestaDao;

    @Override
    public GenericDao<Respuesta, Integer> getDao() {
        return respuestaDao;
    }

    @Override
    public List<Respuesta> getRespuestasActivas(Integer usuarioId) {
        List<Respuesta> respuestaList = new ArrayList<Respuesta>();
        String[] criteriaNombres = {"estado", "usuarioId.usuarioId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {"A", usuarioId};
        String[] orderBy = {"respuestaId"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        respuestaList = findByCriterias(criteria);
        for (Respuesta r : respuestaList) {
            r.getPreguntaId();
        }
        return respuestaList;
    }

}

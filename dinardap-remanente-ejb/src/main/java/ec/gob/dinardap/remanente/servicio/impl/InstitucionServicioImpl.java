package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.dao.GenericDao;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.constante.TipoInstitucionEnum;
import ec.gob.dinardap.remanente.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.dao.InstitucionDao;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

@Stateless(name = "InstitucionServicio")
public class InstitucionServicioImpl extends GenericServiceImpl<Institucion, Integer> implements InstitucionServicio {
    @EJB
    private InstitucionDao institucionDao;
    @Override
    public GenericDao<Institucion, Integer> getDao() {
        return institucionDao;
    }

    @Override
    public List<Institucion> getRegistroMixtoList(Integer direccionRegionalId) {
        List<Institucion> registrosMixtosList = new ArrayList<Institucion>();
        List<Integer> direccionRegionalIdList = new ArrayList<Integer>();
        direccionRegionalIdList.add(direccionRegionalId);
        registrosMixtosList = institucionDao.obtenerHijosPorInstitucion(direccionRegionalIdList, TipoInstitucionEnum.RMX_AUTONOMIA_FINANCIERA.getTipoInstitucion());
        List<Institucion> gadList = new ArrayList<Institucion>();
        gadList = institucionDao.obtenerHijosPorInstitucion(direccionRegionalIdList, TipoInstitucionEnum.GAD.getTipoInstitucion());
        List<Institucion> registrosMixtosSinAutonomiaList = new ArrayList<Institucion>();
        List<Integer> gadIdList = new ArrayList<Integer>();
        for(Institucion institucion:gadList){
            gadIdList.add(institucion.getInstitucionId());
        }
        registrosMixtosList.addAll(institucionDao.obtenerHijosPorInstitucion(gadIdList, TipoInstitucionEnum.RMX_SIN_AUTONOMIA_FINANCIERA.getTipoInstitucion()));
        return registrosMixtosList;
    }

    @Override
    public List<Institucion> getRegistroMercantilList(Integer direccionRegionalId) {
        List<Institucion> registrosMercantilesList = new ArrayList<Institucion>();
        List<Integer> direccionRegionalIdList = new ArrayList<Integer>();
        direccionRegionalIdList.add(direccionRegionalId);
        registrosMercantilesList = institucionDao.obtenerHijosPorInstitucion(direccionRegionalIdList, TipoInstitucionEnum.REGISTRO_MERCANTIL.getTipoInstitucion());
        return registrosMercantilesList;
    }

    @Override
    public List<Institucion> getRegistroPropiedadList(Integer direccionRegionalId) {
        List<Institucion> registrosPropiedadList = new ArrayList<Institucion>();
        List<Integer> direccionRegionalIdList = new ArrayList<Integer>();
        direccionRegionalIdList.add(direccionRegionalId);
        registrosPropiedadList = institucionDao.obtenerHijosPorInstitucion(direccionRegionalIdList, TipoInstitucionEnum.REGISTRO_PROPIEDAD.getTipoInstitucion());
        return registrosPropiedadList;
    }
    
}

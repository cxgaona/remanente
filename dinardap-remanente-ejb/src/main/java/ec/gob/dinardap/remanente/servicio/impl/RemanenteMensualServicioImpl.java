package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.persistence.util.DateBetween;
import ec.gob.dinardap.remanente.dao.RemanenteCuatrimestralDao;
import ec.gob.dinardap.remanente.dao.RemanenteMensualDao;
import ec.gob.dinardap.remanente.modelo.RemanenteAnual;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestralPK;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Stateless(name = "RemanenteMensualServicio")
public class RemanenteMensualServicioImpl extends GenericServiceImpl<RemanenteMensual, Integer> implements RemanenteMensualServicio {

    @EJB
    private RemanenteMensualDao remanenteMensualDao;

    @Override
    public GenericDao<RemanenteMensual, Integer> getDao() {
        return remanenteMensualDao;
    }

    @Override
    public List<RemanenteMensual> getRemanenteMensualByInstitucion(Integer institucionID) {
        List<RemanenteMensual> remanenteMensualList = new ArrayList<RemanenteMensual>();
        String[] criteriaNombres = {"remanenteCuatrimestral.remanenteAnual.institucionRequerida.institucionId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {institucionID};
        String[] orderBy = {"remanenteMensualId"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        remanenteMensualList = findByCriterias(criteria);
        for (RemanenteMensual rm : remanenteMensualList) {
            System.out.println("RM: " + rm.getMes());
            System.out.println("RM: " + rm.getSolicitudCambioUrl());
            System.out.println("RM: " + rm.getInformeAprobacionUrl());
            System.out.println("RM: " + rm.getComentarios());
            System.out.println("RM: " + rm.getTotal());
            System.out.println("RM: " + rm.getFechaRegistro());
            System.out.println("RM INS: " + rm.getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getInstitucionId());
            System.out.println("RM INS: " + rm.getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getNombre());
        }
        return remanenteMensualList;
    }

}

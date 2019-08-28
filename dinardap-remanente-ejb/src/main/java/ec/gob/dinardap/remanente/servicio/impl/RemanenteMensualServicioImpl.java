package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.InstitucionRequeridaDao;
import ec.gob.dinardap.remanente.dao.RemanenteMensualDao;
import ec.gob.dinardap.remanente.dao.TransaccionDao;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteMensual;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Tramite;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import java.math.BigDecimal;
import java.util.ArrayList;

@Stateless(name = "RemanenteMensualServicio")
public class RemanenteMensualServicioImpl extends GenericServiceImpl<RemanenteMensual, Integer> implements RemanenteMensualServicio {

    @EJB
    private RemanenteMensualDao remanenteMensualDao;

    @EJB
    private InstitucionRequeridaDao institucionRequeridaDao;

    @Override
    public GenericDao<RemanenteMensual, Integer> getDao() {
        return remanenteMensualDao;
    }

    @Override
    public List<RemanenteMensual> getRemanenteMensualByInstitucion(Integer institucionID, Integer año) {
        List<RemanenteMensual> remanenteMensualList = new ArrayList<RemanenteMensual>();
        String[] criteriaNombres = {"remanenteCuatrimestral.remanenteAnual.institucionRequerida.institucionId",
            "remanenteCuatrimestral.remanenteAnual.anio"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {institucionID, año};
        String[] orderBy = {"remanenteMensualId"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        remanenteMensualList = findByCriterias(criteria);
        for (RemanenteMensual rm : remanenteMensualList) {
            for (EstadoRemanenteMensual erm : rm.getEstadoRemanenteMensualList()) {
                System.out.println("===" + rm.getMes() + "===");
                System.out.println("erm: " + erm.getEstadoRemanenteMensualId());
                System.out.println("erm: " + erm.getRemanenteMensualId().getRemanenteMensualId());
                System.out.println("erm: " + erm.getUsuarioId().getUsuario());
                System.out.println("erm: " + erm.getFechaRegistro());
                System.out.println("erm: " + erm.getDescripcion());
            }
        }
        /*Por lo que mas quieras no Borres este bloque de código sino luego Diosito se molestará contigo T_T*/

        BigDecimal valorFacturasPagadas = new BigDecimal(0);

        for (RemanenteMensual rm : remanenteMensualList) {
            for (Transaccion t : rm.getTransaccionList()) {
                System.out.println("T: " + t.getCatalogoTransaccionId().getNombre());
                System.out.println("T: " + t.getValorTotal());
                System.out.println("T: " + t.getRespaldoUrl());
            }
        }
        /*Por lo que mas quieras no Borres este bloque de código sino luego Diosito se molestará contigo T_T*/        
        return remanenteMensualList;
    }
}

package ec.gob.dinardap.remanente.servicio.impl;


import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.dao.NominaDao;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.servicio.NominaServicio;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "NominaServicio")
public class NominaServicioImpl extends GenericServiceImpl<Nomina, Integer> implements NominaServicio {

    @EJB
    private NominaDao nominaDao;

    @Override
    public GenericDao<Nomina, Integer> getDao() {
        return nominaDao;
    }

    @Override
    public void crearNomina(Nomina nomina) {
        this.create(nomina);
    }

    @Override
    public List<Nomina> getNominaByInstitucionFecha(Integer idInstitucion, String fecha) {
        List<Nomina> nominaList = new ArrayList<Nomina>();
        String[] criteriaNombres = {"transaccionId.remanenteMensualId.remanenteCuatrimestral.remanenteAnual.institucionRequerida.institucionId",""};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {institucionID};
        String[] orderBy = {"remanenteMensualId"};
        boolean[] asc = {false};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        remanenteMensualList = findByCriterias(criteria);
        for (RemanenteMensual rm : remanenteMensualList) {
            for (Transaccion t : rm.getTransaccionList()) {

                t.getCatalogoTransaccionId().getCatalogoTransaccionId();
            }

        }
        return remanenteMensualList;
        return null;
    }

}

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
    public List<Nomina> getNominaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes) {
        List<Nomina> nominaList = new ArrayList<Nomina>();
        nominaList = nominaDao.getNominaByInstitucionFecha(idInstitucion, anio, mes);
        return nominaList;
    }

}

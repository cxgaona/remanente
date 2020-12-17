package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.TramiteDao;
import ec.gob.dinardap.remanente.modelo.Tramite;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless(name = "TramiteDao")
public class TramiteDaoEjb extends RemanenteGenericDao<Tramite, Integer> implements TramiteDao {

    public TramiteDaoEjb() {
        super(Tramite.class);
    }

    @Override
    public List<Tramite> getTramiteByInstitucionFechaActividad(Integer idInstitucion, Integer anio, Integer mes, String actividad, Integer idRemanenteMensual) {
        Query query = em.createQuery("SELECT t FROM Tramite t WHERE t.transaccion.remanenteMensual.remanenteMensualId=:idRemanenteMensual AND "
                + "t.transaccion.remanenteMensual.remanenteCuatrimestral.remanenteAnual.institucion.institucionId=:idInstitucion AND "
                + "t.transaccion.remanenteMensual.remanenteCuatrimestral.remanenteAnual.anio=:anio AND "
                + "t.transaccion.remanenteMensual.mes=:mes AND "
                + "t.actividadRegistral=:actividad");
        query.setParameter("idRemanenteMensual", idRemanenteMensual);
        query.setParameter("idInstitucion", idInstitucion);
        query.setParameter("anio", anio);
        query.setParameter("mes", mes);
        query.setParameter("actividad", actividad);
        List<Tramite> tramiteList = new ArrayList<Tramite>();
        tramiteList = query.getResultList();
        return tramiteList;
    }

}

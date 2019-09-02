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
    public List<Tramite> getTramiteByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes) {
        Query query = em.createQuery("SELECT t FROM Tramite t WHERE "
                + "t.transaccionId.remanenteMensualId.remanenteCuatrimestral.remanenteAnual.institucionRequerida.institucionId=:idInstitucion AND "
                + "t.transaccionId.remanenteMensualId.remanenteCuatrimestral.remanenteAnual.anio=:anio AND "
                + "t.transaccionId.remanenteMensualId.mes=:mes");
        query.setParameter("idInstitucion", idInstitucion);
        query.setParameter("anio", anio);
        query.setParameter("mes", mes);
        List<Tramite> tramiteList = new ArrayList<Tramite>();
        tramiteList = query.getResultList();        
        return tramiteList;
    }

}

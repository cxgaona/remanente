package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.NominaDao;
import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import ec.gob.dinardap.remanente.modelo.Nomina;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless(name = "NominaDao")
public class NominaDaoEjb extends RemanenteGenericDao<Nomina, Integer> implements NominaDao {

    public NominaDaoEjb() {
        super(Nomina.class);
    }

    @Override
    public List<Nomina> getNominaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes) {
        Query query = em.createQuery("SELECT n FROM Nomina n WHERE "
                + "n.transaccionId.remanenteMensualId.remanenteCuatrimestral.remanenteAnual.institucionRequerida.institucionId=:idInstitucion AND "
                + "n.transaccionId.remanenteMensualId.remanenteCuatrimestral.remanenteAnual.anio=:anio AND "
                + "n.transaccionId.remanenteMensualId.mes=:mes");
        query.setParameter("idInstitucion", idInstitucion);
        query.setParameter("anio", anio);
        query.setParameter("mes", mes);
        List<Nomina> lista = query.getResultList();
        List<RemanenteCuatrimestral> rc = new ArrayList<RemanenteCuatrimestral>();
        rc = query.getResultList();
        System.out.println("size: " + rc.size());
        return rc.size() > 0 ? true : false;
        return null;
    }

}

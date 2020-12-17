package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.modelo.Nomina;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import ec.gob.dinardap.remanente.dao.NominaDao1;

@Stateless(name = "NominaDao1")
public class NominaDaoEjb extends RemanenteGenericDao<Nomina, Integer> implements NominaDao1 {

    public NominaDaoEjb() {
        super(Nomina.class);
    }

    @Override
    public List<Nomina> getNominaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes, Integer idRemanenteMensual) {
        Query query = em.createQuery("SELECT n FROM Nomina n WHERE "
                + "n.transaccion.remanenteMensual.remanenteMensualId=:idRemanenteMensual AND "
                + "n.transaccion.remanenteMensual.remanenteCuatrimestral.remanenteAnual.institucion.institucionId=:idInstitucion AND "
                + "n.transaccion.remanenteMensual.remanenteCuatrimestral.remanenteAnual.anio=:anio AND "
                + "n.transaccion.remanenteMensual.mes=:mes");
        query.setParameter("idRemanenteMensual", idRemanenteMensual);
        query.setParameter("idInstitucion", idInstitucion);
        query.setParameter("anio", anio);
        query.setParameter("mes", mes);
        List<Nomina> nominaList = new ArrayList<Nomina>();
        nominaList = query.getResultList();
        return nominaList;
    }

}

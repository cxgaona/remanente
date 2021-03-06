package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.FacturaPagadaDao;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import ec.gob.dinardap.remanente.modelo.FacturaPagada;

@Stateless(name = "FacturaPagadaDao")
public class FacturaPagadaDaoEjb extends RemanenteGenericDao<FacturaPagada, Integer> implements FacturaPagadaDao {

    public FacturaPagadaDaoEjb() {
        super(FacturaPagada.class);
    }

    @Override
    public List<FacturaPagada> getFacturaPagadaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes, Integer idRemanenteMensual) {
        Query query = em.createQuery("SELECT fp FROM FacturaPagada fp WHERE "
                + "fp.transaccion.remanenteMensual.remanenteMensualId=:idRemanenteMensual AND "
                + "fp.transaccion.remanenteMensual.remanenteCuatrimestral.remanenteAnual.institucion.institucionId=:idInstitucion AND "
                + "fp.transaccion.remanenteMensual.remanenteCuatrimestral.remanenteAnual.anio=:anio AND "
                + "fp.transaccion.remanenteMensual.mes=:mes");
        query.setParameter("idRemanenteMensual", idRemanenteMensual);
        query.setParameter("idInstitucion", idInstitucion);
        query.setParameter("anio", anio);
        query.setParameter("mes", mes);
        List<FacturaPagada> facturaPagadaList = new ArrayList<FacturaPagada>();
        facturaPagadaList = query.getResultList();
        return facturaPagadaList;
    }
}

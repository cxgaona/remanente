package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.TransaccionDao;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless(name = "TransaccionDao")
public class TransaccionDaoEjb extends RemanenteGenericDao<Transaccion, Integer> implements TransaccionDao {

    public TransaccionDaoEjb() {
        super(Transaccion.class);
    }

    @Override
    public Transaccion getTransaccionByInstitucionFechaTipo(Integer idInstitucion, Integer anio, Integer mes, Integer tipo) {
        Query query = em.createQuery("SELECT t FROM Transaccion t WHERE "
                + "t.remanenteMensualId.remanenteCuatrimestral.remanenteAnual.institucionRequerida.institucionId=:idInstitucion AND "
                + "t.remanenteMensualId.remanenteCuatrimestral.remanenteAnual.anio=:anio AND "
                + "t.remanenteMensualId.mes=:mes AND "
                + "t.catalogoTransaccionId.catalogoTransaccionId=:tipo");
        query.setParameter("idInstitucion", idInstitucion);
        query.setParameter("anio", anio);
        query.setParameter("mes", mes);
        query.setParameter("tipo", tipo);
        List<Transaccion> transaccionList = new ArrayList<Transaccion>();
        transaccionList = query.getResultList();
        List<Transaccion> transaccionListActiva = new ArrayList<Transaccion>();
        for (Transaccion transaccion : transaccionList) {
            if (!transaccion.getRemanenteMensualId().getEstadoRemanenteMensualList().get(transaccion.getRemanenteMensualId().getEstadoRemanenteMensualList().size() - 1).getDescripcion().equals("CambioAprobado")) {
                transaccionListActiva.add(transaccion);                
            }
        }
        for (Transaccion transaccion : transaccionListActiva) {
            System.out.println("transaccionActiva: " + transaccion.getTransaccionId());
        }
        return transaccionListActiva.get(transaccionListActiva.size() - 1);
    }

}

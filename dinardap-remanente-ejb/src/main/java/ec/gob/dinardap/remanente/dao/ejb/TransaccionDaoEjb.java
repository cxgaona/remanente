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
        System.out.println("transaccionList = " + transaccionList.size());
        
        return transaccionList.get(transaccionList.size()-1);
    }

    

}

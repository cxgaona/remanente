package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.InstitucionRequeridaDao;
import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless(name = "InstitucionRequeridaDao")
public class InstitucionRequeridaDaoEjb extends RemanenteGenericDao<InstitucionRequerida, Integer> implements InstitucionRequeridaDao {

    public InstitucionRequeridaDaoEjb() {
        super(InstitucionRequerida.class);
    }

    @Override
    public List<InstitucionRequerida> getDireccionRegionalList() {
        Query query = em.createQuery("SELECT ir "
                + "FROM InstitucionRequerida ir "
                + "WHERE ir.tipo='REGIONAL' "
                + "ORDER BY ir.institucionId asc");
        List<InstitucionRequerida> list = query.getResultList();
        if (list != null && !list.isEmpty()) {
            for(InstitucionRequerida ir: list){
                ir.toString();
            }
        } 
        return list;
    }
}

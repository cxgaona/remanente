package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.CatalogoTransaccionDao;
import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;

import javax.ejb.Stateless;

@Stateless(name = "CatalogoTransaccionDao")
public class CatalogoTransaccionDaoEjb extends RemanenteGenericDao<CatalogoTransaccion, Integer> implements CatalogoTransaccionDao {

    public CatalogoTransaccionDaoEjb() {
        super(CatalogoTransaccion.class);
    }

//    @SuppressWarnings("unchecked")
//    @Override
//    public Pregunta obtenerPregunta(Integer preguntaId) {
//        Query query = em.createQuery("select p "
//                + "from Pregunta p, "
//                + "in (p.respuestas) r "
//                + "where "
//                + "p.preguntaId = :preguntaId "
//                + "order by r.respuestaId asc");
//        query.setParameter("preguntaId", preguntaId);
//        List<Pregunta> lista = query.getResultList();
//        if (lista != null && !lista.isEmpty()) {
//            return lista.get(0);
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public List<Pregunta> obtenerPreguntasPorValidar() {
//        List<Short> estado = new ArrayList<Short>();
//        estado.add(EstadoEnum.CREADO.getEstado());
//        estado.add(EstadoEnum.REVISADO.getEstado());
//        return obtenerPreguntaPorEstado(estado);
//    }
//
//    @Override
//    public List<Pregunta> obtenerPreguntasValidadas() {
//        List<Short> estado = new ArrayList<Short>();
//        estado.add(EstadoEnum.APROBADO.getEstado());
//        estado.add(EstadoEnum.RECHAZADO.getEstado());
//        return obtenerPreguntaPorEstado(estado);
//    }
//
//    @SuppressWarnings("unchecked")
//    private List<Pregunta> obtenerPreguntaPorEstado(List<Short> estado) {
//        Query query = em.createQuery("select p "
//                + "from Pregunta p "
//                + "where "
//                + "p.estado in (:estado) "
//                + "order by p.preguntaId");
//        query.setParameter("estado", estado);
//        return query.getResultList();
//    }

}

package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.TomoDao;
import ec.gob.dinardap.remanente.dto.ResumenLibroDTO;
import ec.gob.dinardap.remanente.modelo.Libro;
import ec.gob.dinardap.remanente.modelo.Tomo;
import ec.gob.dinardap.util.constante.EstadoEnum;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless(name = "TomoDao")
public class TomoDaoEjb extends RemanenteGenericDao<Tomo, Integer> implements TomoDao {

    public TomoDaoEjb() {
        super(Tomo.class);
    }

    @Override
    public ResumenLibroDTO getResumenPorInstitucionAñoTipo(Integer institucionId, Integer año, Integer tipoLibroId) {
        Query query = em.createQuery("SELECT l FROM Libro l WHERE l.inventarioAnual.institucion.institucionId=: institucionId AND l.inventarioAnual.anio=: año AND l.tipoLibro.tipoLibroId=: tipoLibroId AND l.estado=: estado");
        query.setParameter("institucionId", institucionId);
        query.setParameter("año", año);
        query.setParameter("tipoLibroId", tipoLibroId);
        query.setParameter("estado", EstadoEnum.ACTIVO.getEstado());
        List<Libro> libroList = new ArrayList<Libro>();
        libroList = query.getResultList();

        ResumenLibroDTO resumenLibroDTO = new ResumenLibroDTO();
        resumenLibroDTO.setTotalLibros(0);
        resumenLibroDTO.setTotalPaginasLibros(0);
        resumenLibroDTO.setNombreRegistrador("");

        if (!libroList.isEmpty()) {
            Query query1 = em.createQuery("SELECT t FROM Tomo t WHERE t.libro IN (:libroList) AND t.estado=: estado");
            query1.setParameter("libroList", libroList);
            query1.setParameter("estado", EstadoEnum.ACTIVO.getEstado());
            List<Tomo> tomoList = new ArrayList<Tomo>();
            tomoList = query1.getResultList();
            if(libroList.get(0).getInventarioAnual().getNombreRegistrador()!=null){
                resumenLibroDTO.setNombreRegistrador(libroList.get(0).getInventarioAnual().getNombreRegistrador());
            }
            resumenLibroDTO.setTotalLibros(libroList.size());
            for (Tomo tomo : tomoList) {
                resumenLibroDTO.setTotalPaginasLibros(resumenLibroDTO.getTotalPaginasLibros() + tomo.getNumeroTotalHojas());
            }
        }

        return resumenLibroDTO;
    }

}

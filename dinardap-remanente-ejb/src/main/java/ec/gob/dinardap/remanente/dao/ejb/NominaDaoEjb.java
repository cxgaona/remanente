package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.modelo.Nomina;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import ec.gob.dinardap.remanente.dao.NominaDao1;
import ec.gob.dinardap.remanente.modelo.EstadoRemanenteMensual;

@Stateless(name = "NominaDao1")
public class NominaDaoEjb extends RemanenteGenericDao<Nomina, Integer> implements NominaDao1 {

    public NominaDaoEjb() {
        super(Nomina.class);
    }

    @Override
    public List<Nomina> getNominaByInstitucionFecha(Integer idInstitucion, Integer anio, Integer mes) {
        Query query = em.createQuery("SELECT n FROM Nomina n WHERE n.transaccionId.remanenteMensualId.remanenteCuatrimestral.remanenteAnual.institucionRequerida.institucionId=:idInstitucion AND n.transaccionId.remanenteMensualId.remanenteCuatrimestral.remanenteAnual.anio=:anio AND n.transaccionId.remanenteMensualId.mes=:mes");
        query.setParameter("idInstitucion", idInstitucion);
        query.setParameter("anio", anio);
        query.setParameter("mes", mes);
        List<Nomina> nominaList = new ArrayList<Nomina>();
        nominaList = query.getResultList();
        List<Nomina> nominaListActiva = new ArrayList<Nomina>();
        for (Nomina nomina : nominaList) {
            System.out.println("Ultimo estado: " + nomina.getTransaccionId().getRemanenteMensualId().getEstadoRemanenteMensualList().get(nomina.getTransaccionId().getRemanenteMensualId().getEstadoRemanenteMensualList().size() - 1).getDescripcion());
            if (!nomina.getTransaccionId().getRemanenteMensualId().getEstadoRemanenteMensualList().get(nomina.getTransaccionId().getRemanenteMensualId().getEstadoRemanenteMensualList().size() - 1).getDescripcion().equals("CambioAprobado")) {
                nominaListActiva.add(nomina);
                System.out.println("Nomina Agregada: "+nomina.getFechaRegistro());
                System.out.println("Nomina Agregada: "+nomina.getTransaccionId().getTransaccionId());
                System.out.println("Nomina Agregada: "+nomina.getNominaId());
            }
        }
        for (Nomina nomina : nominaListActiva) {
            System.out.println("nominaActiva: " + nomina.getNomNombres());
        }
        return nominaListActiva;
    }

}

package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.BandejaDao;
import ec.gob.dinardap.remanente.modelo.Bandeja;
import ec.gob.dinardap.remanente.servicio.impl.BandejaServicioImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless(name = "BandejaDao")
public class BandejaDaoEjb extends RemanenteGenericDao<Bandeja, Integer> implements BandejaDao {

    public BandejaDaoEjb() {
        super(Bandeja.class);
    }

    @Override
    public List<Bandeja> getBandejaByUsuarioAñoMes(Integer usuarioId, Integer anio, Integer mes) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Bandeja> bandejaList = new ArrayList<Bandeja>();
        Date fechaHasta = new Date();
        Date fechaDesde = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaHasta);
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        Integer anioMin = 0;
        Integer mesMin = 0;
        String strfechaDesde = "";
        if (mes == 1) {
            anioMin = anio - 1;
            strfechaDesde = anioMin + "-12-01";

        } else {
            mesMin = mes - 1;
            strfechaDesde = anio + "-" + mesMin + "-01 00:00:00";
        }
        try {
            fechaDesde = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strfechaDesde);
        } catch (ParseException ex) {
            Logger.getLogger(BandejaServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        Query query = em.createQuery("SELECT b FROM Bandeja b WHERE b.usuarioAsignadoId.usuarioId=:usuarioId "
                + "AND b.fechaRegistro BETWEEN '" + strfechaDesde + "' AND '" + sdf.format(fechaHasta) + "' ORDER BY b.fechaRegistro desc");
        query.setParameter("usuarioId", usuarioId);
        List<Bandeja> b = new ArrayList<Bandeja>();
        b = query.getResultList();  
        return b.size() > 0 ? b : null;
    }
}

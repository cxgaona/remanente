package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.RemanenteCuatrimestralDao;
import ec.gob.dinardap.remanente.modelo.RemanenteAnual;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestralPK;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless(name = "RemanenteCuatrimestralDao")
public class RemanenteCuatrimestralDaoEjb extends RemanenteGenericDao<RemanenteCuatrimestral, RemanenteCuatrimestralPK> implements RemanenteCuatrimestralDao {

    public RemanenteCuatrimestralDaoEjb() {
        super(RemanenteCuatrimestral.class);
    }

    @Override
    public Boolean verificarRemanenteCuatrimestral(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        Calendar fechaDesde = Calendar.getInstance();
        Calendar fechaHasta = Calendar.getInstance();
        Date dateDesde = new Date();
        Date dateHasta = new Date();
        if (calendar.get(Calendar.MONTH) + 1 <= 4) {
            fechaDesde.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaDesde.set(Calendar.MONTH, 0);
            fechaDesde.set(Calendar.DAY_OF_MONTH, 1);
            dateDesde = fechaDesde.getTime();
            fechaHasta.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaHasta.set(Calendar.MONTH, 3);
            fechaHasta.set(Calendar.DAY_OF_MONTH, 30);
            dateHasta = fechaHasta.getTime();
        } else if (calendar.get(Calendar.MONTH) + 1 > 4 && calendar.get(Calendar.MONTH) + 1 <= 8) {
            fechaDesde.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaDesde.set(Calendar.MONTH, 4);
            fechaDesde.set(Calendar.DAY_OF_MONTH, 1);
            dateDesde = fechaDesde.getTime();
            fechaHasta.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaHasta.set(Calendar.MONTH, 7);
            fechaHasta.set(Calendar.DAY_OF_MONTH, 31);
            dateHasta = fechaHasta.getTime();
        } else if (calendar.get(Calendar.MONTH) + 1 > 8 && calendar.get(Calendar.MONTH) + 1 <= 12) {
            fechaDesde.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaDesde.set(Calendar.MONTH, 7);
            fechaDesde.set(Calendar.DAY_OF_MONTH, 1);
            dateDesde = fechaDesde.getTime();
            fechaHasta.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            fechaHasta.set(Calendar.MONTH, 11);
            fechaHasta.set(Calendar.DAY_OF_MONTH, 31);
            dateHasta = fechaHasta.getTime();
        }
        Query query = em.createQuery("SELECT rc FROM RemanenteCuatrimestral rc "
                + "WHERE rc.fecha BETWEEN '" + sdf.format(dateDesde) + "' AND '" + sdf.format(dateDesde) + "'");
        List<RemanenteCuatrimestral> rc = new ArrayList<RemanenteCuatrimestral>();
        rc = query.getResultList();
        return rc.size() > 0 ? true : false;
    }

    public RemanenteAnual getRemanenteAnual(Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        Query query = em.createQuery("SELECT ra FROM RemanenteAnual ra WHERE ra.anio = " + calendar.get(Calendar.YEAR));
        Query query1 = em.createQuery("SELECT ra FROM Respuesta ra WHERE ra.usuarioId.usuarioId");
        List<RemanenteAnual> ras = new ArrayList<RemanenteAnual>();
        ras = query.getResultList();
        RemanenteAnual ra = new RemanenteAnual();
        ra = ras.get(0);
        return ra != null ? ra : null;
    }

}

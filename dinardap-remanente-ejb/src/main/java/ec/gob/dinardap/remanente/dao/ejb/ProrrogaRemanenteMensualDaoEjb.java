package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.ProrrogaRemanenteMensualDao;
import ec.gob.dinardap.remanente.dto.ProrrogaRemanenteMensualDTO;
import ec.gob.dinardap.remanente.modelo.ProrrogaRemanenteMensual;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless(name = "ProrrogaRemanenteMensualDao")
public class ProrrogaRemanenteMensualDaoEjb extends RemanenteGenericDao<ProrrogaRemanenteMensual, Integer> implements ProrrogaRemanenteMensualDao {

    public ProrrogaRemanenteMensualDaoEjb() {
        super(ProrrogaRemanenteMensual.class);
    }

    @Override
    public List<ProrrogaRemanenteMensualDTO> getProrrogaListEstado(String estado) {
        List<RemanenteMensual> remanenteMensualList = new ArrayList<RemanenteMensual>();
        List<ProrrogaRemanenteMensualDTO> prorrogaRemanenteMensualList = new ArrayList<ProrrogaRemanenteMensualDTO>();
        Query query = em.createQuery("SELECT rm FROM RemanenteMensual rm WHERE rm.prorrogaRemanenteMensualId IS NOT NULL AND rm.prorrogaRemanenteMensualId.estado=:estado");
        query.setParameter("estado", estado);
        if (!query.getResultList().isEmpty()) {
            remanenteMensualList = query.getResultList();
            for (RemanenteMensual rm : remanenteMensualList) {
                prorrogaRemanenteMensualList.add(new ProrrogaRemanenteMensualDTO(
                        rm.getProrrogaRemanenteMensualId(),
                        rm.getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida(),
                        rm.getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getNombre(),
                        rm.getMes(),
                        rm.getRemanenteCuatrimestral().getRemanenteAnual().getAnio()));
            }
        }
        return prorrogaRemanenteMensualList;
    }
}

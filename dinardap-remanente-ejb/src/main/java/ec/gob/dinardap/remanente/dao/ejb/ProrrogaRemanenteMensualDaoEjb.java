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
        List<ProrrogaRemanenteMensual> prorrogaRemanenteMensualList = new ArrayList<ProrrogaRemanenteMensual>();
        List<ProrrogaRemanenteMensualDTO> prorrogaDtoList = new ArrayList<ProrrogaRemanenteMensualDTO>();
        Query query = em.createQuery("SELECT prm FROM ProrrogaRemanenteMensual prm WHERE prm.prorrogaRemanenteMensualId.estado=:estado");
        query.setParameter("estado", estado);
        if (!query.getResultList().isEmpty()) {
            prorrogaRemanenteMensualList = query.getResultList();
            for (ProrrogaRemanenteMensual prm : prorrogaRemanenteMensualList) {
                prorrogaDtoList.add(new ProrrogaRemanenteMensualDTO(
                        prm,
                        prm.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida(),
                        prm.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucionRequerida().getNombre(),
                        prm.getRemanenteMensualId().getMes(),
                        prm.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio()));
            }
        }
        return prorrogaDtoList;
    }
}

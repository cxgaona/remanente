package ec.gob.dinardap.remanente.dao.ejb;

import ec.gob.dinardap.remanente.dao.ProrrogaRemanenteMensualDao;
import ec.gob.dinardap.remanente.dto.ProrrogaRemanenteGeneralDTO;
import ec.gob.dinardap.remanente.dto.ProrrogaRemanenteMensualDTO;
import ec.gob.dinardap.remanente.dto.SolicitudCambioDTO;
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
                        prm.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion(),
                        prm.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getInstitucion().getNombre(),
                        prm.getRemanenteMensualId().getMes(),
                        prm.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio()));
            }
        }
        return prorrogaDtoList;
    }

    @Override
    public List<ProrrogaRemanenteGeneralDTO> getProrrogaGeneralListEstado(String estado) {
        List<ProrrogaRemanenteMensual> prorrogaRemanenteMensualList = new ArrayList<ProrrogaRemanenteMensual>();
        List<ProrrogaRemanenteGeneralDTO> prorrogaDtoList = new ArrayList<ProrrogaRemanenteGeneralDTO>();
        Query query = em.createQuery("SELECT prm FROM ProrrogaRemanenteMensual prm WHERE prm.prorrogaRemanenteMensualId.estado=:estado");
        query.setParameter("estado", estado);
        if (!query.getResultList().isEmpty()) {
            prorrogaRemanenteMensualList = query.getResultList();
            for (ProrrogaRemanenteMensual prm : prorrogaRemanenteMensualList) {
                prorrogaDtoList.add(new ProrrogaRemanenteGeneralDTO(prm));
            }
        }
        return prorrogaDtoList;
    }

    @Override
    public List<RemanenteMensual> getRemanenteMensualListAñoMes(Integer año, Integer mes) {
        List<RemanenteMensual> remanenteMensualList = new ArrayList<RemanenteMensual>();
        List<Integer> remanenteIdList = new ArrayList<Integer>();
        Query query = em.createQuery("SELECT MAX(rm.remanenteMensualId) FROM RemanenteMensual rm WHERE rm.mes=:mes AND rm.remanenteCuatrimestral.remanenteAnual.anio=:año GROUP BY rm.remanenteCuatrimestral.remanenteAnual.institucion.institucionId");
        query.setParameter("mes", mes);
        query.setParameter("año", año);
        if (!query.getResultList().isEmpty()) {
            remanenteIdList = query.getResultList();
        }
        for (Integer rm : remanenteIdList) {
            System.out.println("RM: " + rm);
            RemanenteMensual remanenteMensual = new RemanenteMensual();
            remanenteMensual.setRemanenteMensualId(rm);
            remanenteMensualList.add(remanenteMensual);
        }
        return remanenteMensualList;
    }

    @Override
    public List<SolicitudCambioDTO> getRemanenteMensualSolicitudCambioAprobada(Integer institucionId) {
        List<RemanenteMensual> remanenteMensualList = new ArrayList<RemanenteMensual>();
        List<SolicitudCambioDTO> solicitudCambiolDTOList = new ArrayList<SolicitudCambioDTO>();
        Query query = em.createQuery("SELECT rm FROM RemanenteMensual rm INNER JOIN rm.estadoRemanenteMensualList erml WHERE rm.remanenteCuatrimestral.remanenteAnual.institucion.institucionId=:institucion AND erml.descripcion='GeneradoNuevaVersion' AND erml.estadoRemanenteMensualId IN ( SELECT MAX(erm.estadoRemanenteMensualId) FROM EstadoRemanenteMensual erm WHERE erm.remanenteMensual IS NOT NULL GROUP BY erm.remanenteMensual) ");
        query.setParameter("institucion", institucionId);
        if (!query.getResultList().isEmpty()) {
            remanenteMensualList = query.getResultList();
        }
        for (RemanenteMensual rm : remanenteMensualList) {
            solicitudCambiolDTOList.add(new SolicitudCambioDTO(rm));
        }
        return solicitudCambiolDTOList;
    }

    @Override
    public ProrrogaRemanenteMensual getProrrogaRemanenteMensual(Integer remanenteMensualId) {
        ProrrogaRemanenteMensual prorrogaRemanenteMensual = new ProrrogaRemanenteMensual();
        Query query = em.createQuery("SELECT prm FROM ProrrogaRemanenteMensual prm "
                + "WHERE prm.remanenteMensualId.remanenteMensualId =:remanenteMensualId "
                + "AND (prm.estado='A' OR prm.estado='AC')");
        query.setParameter("remanenteMensualId", remanenteMensualId);
        if (!query.getResultList().isEmpty()) {
            prorrogaRemanenteMensual = (ProrrogaRemanenteMensual) query.getResultList().get(query.getResultList().size() - 1);
        } else {
            prorrogaRemanenteMensual = null;
        }
        return prorrogaRemanenteMensual;
    }

    //DEPRECATED
//    @Override
//    public List<ProrrogaRemanenteGeneralDTO> getProrrogaGeneralListEstado(String estado) {
//        List<ProrrogaRemanenteMensual> prorrogaRemanenteMensualList = new ArrayList<ProrrogaRemanenteMensual>();
//        List<ProrrogaRemanenteGeneralDTO> prorrogaDtoList = new ArrayList<ProrrogaRemanenteGeneralDTO>();
//        Query query = em.createQuery("SELECT prm FROM ProrrogaRemanenteMensual prm WHERE prm.prorrogaRemanenteMensualId.estado=:estado");
//        query.setParameter("estado", estado);
//        if (!query.getResultList().isEmpty()) {
//            prorrogaRemanenteMensualList = query.getResultList();
//            ProrrogaRemanenteGeneralDTO generalDTO = new ProrrogaRemanenteGeneralDTO();
//            Integer id = 0;
//            for (ProrrogaRemanenteMensual prm : prorrogaRemanenteMensualList) {
//                Integer añoProrrogaMensual = prm.getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio();
//                Integer mesProrrogaMensual = prm.getRemanenteMensualId().getMes();
//                if (generalDTO.getProrrogaRemanenteMensualList().isEmpty()) {
//                    generalDTO.setId(id);
//                    generalDTO.setAño(añoProrrogaMensual);
//                    generalDTO.setMesInt(mesProrrogaMensual);
//                    generalDTO.getProrrogaRemanenteMensualList().add(prm);
//                    generalDTO.setComentarioApertura(prm.getComentarioApertura());
//                    generalDTO.setComentarioCierre(prm.getComentarioCierre());
//                    generalDTO.setEstado(prm.getEstado());
//                    id++;
//                } else {
//                    Integer añoUltimaProrrogaGeneral = generalDTO.getProrrogaRemanenteMensualList().get(generalDTO.getProrrogaRemanenteMensualList().size() - 1)
//                            .getRemanenteMensualId().getRemanenteCuatrimestral().getRemanenteAnual().getAnio();
//
//                    Integer mesUltimaProrrogaGeneral = generalDTO.getProrrogaRemanenteMensualList().get(generalDTO.getProrrogaRemanenteMensualList().size() - 1)
//                            .getRemanenteMensualId().getMes();
//
//                    if (añoProrrogaMensual.equals(añoUltimaProrrogaGeneral)
//                            && mesProrrogaMensual.equals(mesUltimaProrrogaGeneral)) {
//                        generalDTO.getProrrogaRemanenteMensualList().add(prm);
//                    } else {
//                        prorrogaDtoList.add(generalDTO);
//                        generalDTO = new ProrrogaRemanenteGeneralDTO();
//                        generalDTO.setId(id);
//                        generalDTO.setAño(añoProrrogaMensual);
//                        generalDTO.setMesInt(mesProrrogaMensual);
//                        generalDTO.getProrrogaRemanenteMensualList().add(prm);
//                        generalDTO.setComentarioApertura(prm.getComentarioApertura());
//                        generalDTO.setComentarioCierre(prm.getComentarioCierre());
//                        generalDTO.setEstado(prm.getEstado());
//                        id++;
//                    }
//                }
//            }
//            prorrogaDtoList.add(generalDTO);
//        }
//        return prorrogaDtoList;
//    }
}

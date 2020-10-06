package ec.gob.dinardap.remanente.dao;

import javax.ejb.Local;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.remanente.dto.ProrrogaRemanenteGeneralDTO;
import ec.gob.dinardap.remanente.dto.ProrrogaRemanenteMensualDTO;
import ec.gob.dinardap.remanente.dto.SolicitudCambioDTO;
import ec.gob.dinardap.remanente.modelo.ProrrogaRemanenteMensual;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import java.util.List;

@Local
public interface ProrrogaRemanenteMensualDao extends GenericDao<ProrrogaRemanenteMensual, Integer> {

    public List<ProrrogaRemanenteMensualDTO> getProrrogaListEstado(String estado);

    public List<ProrrogaRemanenteGeneralDTO> getProrrogaGeneralListEstado(String estado);

    public List<RemanenteMensual> getRemanenteMensualListAñoMes(Integer año, Integer mes);

    public List<SolicitudCambioDTO> getRemanenteMensualSolicitudCambioAprobada(Integer institucionId);
    
    public ProrrogaRemanenteMensual getProrrogaRemanenteMensual(Integer remanenteMensualId);

}

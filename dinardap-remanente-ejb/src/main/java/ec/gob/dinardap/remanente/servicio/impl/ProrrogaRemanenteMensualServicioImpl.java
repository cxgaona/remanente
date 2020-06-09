package ec.gob.dinardap.remanente.servicio.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.dao.ProrrogaRemanenteMensualDao;
import ec.gob.dinardap.remanente.dto.ProrrogaRemanenteGeneralDTO;
import ec.gob.dinardap.remanente.dto.ProrrogaRemanenteMensualDTO;
import ec.gob.dinardap.remanente.dto.SolicitudCambioDTO;
import ec.gob.dinardap.remanente.modelo.ProrrogaRemanenteMensual;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.servicio.ProrrogaRemanenteMensualServicio;
import java.util.ArrayList;

@Stateless(name = "ProrrogaRemanenteMensualServicio")
public class ProrrogaRemanenteMensualServicioImpl extends GenericServiceImpl<ProrrogaRemanenteMensual, Integer> implements ProrrogaRemanenteMensualServicio {

    @EJB
    private ProrrogaRemanenteMensualDao prorrogaRemanenteMensualDao;

    @Override
    public GenericDao<ProrrogaRemanenteMensual, Integer> getDao() {
        return prorrogaRemanenteMensualDao;
    }

    @Override
    public List<ProrrogaRemanenteMensualDTO> getListProrrogaRemanenteMensualEstado(String estado) {
        List<ProrrogaRemanenteMensualDTO> prorrogaRemanenteMensualDTOList = new ArrayList<ProrrogaRemanenteMensualDTO>();
        prorrogaRemanenteMensualDTOList = prorrogaRemanenteMensualDao.getProrrogaListEstado(estado);
        return prorrogaRemanenteMensualDTOList;
    }

    @Override
    public void update(List<ProrrogaRemanenteMensual> prorrogaList) {
        for (ProrrogaRemanenteMensual prm : prorrogaList) {
            this.update(prm);
        }
    }

    @Override
    public List<ProrrogaRemanenteGeneralDTO> getProrrogaGeneralListEstado(String estado) {
        List<ProrrogaRemanenteGeneralDTO> prorrogaRemanenteGeneralDTOList = new ArrayList<ProrrogaRemanenteGeneralDTO>();
        prorrogaRemanenteGeneralDTOList = prorrogaRemanenteMensualDao.getProrrogaGeneralListEstado(estado);
        return prorrogaRemanenteGeneralDTOList;
    }

    @Override
    public List<SolicitudCambioDTO> getRemanenteMensualSolicitudCambioAprobada(Integer institucionId) {        
        return prorrogaRemanenteMensualDao.getRemanenteMensualSolicitudCambioAprobada(institucionId);
    }

}

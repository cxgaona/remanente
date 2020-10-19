package ec.gob.dinardap.remanente.servicio.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.dao.BandejaDao;
import ec.gob.dinardap.remanente.dto.BandejaDTO;
import ec.gob.dinardap.remanente.mail.Email;
import ec.gob.dinardap.remanente.modelo.Bandeja;

import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestralPK;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;

import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "BandejaServicio")
public class BandejaServicioImpl extends GenericServiceImpl<Bandeja, Integer> implements BandejaServicio {

    @EJB
    private BandejaDao bandejaDao;

    @Override
    public GenericDao<Bandeja, Integer> getDao() {
        return bandejaDao;
    }

    @Override
    public List<BandejaDTO> getBandejaByUsuarioAñoMes(Integer usuarioId, Integer anio, Integer mes) {
        List<BandejaDTO> listBandejaDTO = new ArrayList<BandejaDTO>();
        List<Bandeja> listBandeja = new ArrayList<Bandeja>();
        listBandeja = bandejaDao.getBandejaByUsuarioAñoMes(usuarioId, anio, mes);
        listBandeja = listBandeja == null ? new ArrayList<Bandeja>() : listBandeja;
        for (Bandeja b : listBandeja) {
            BandejaDTO bandejaDTO = new BandejaDTO();
            bandejaDTO.setUsuarioAsignado(b.getUsuarioAsignado());
            bandejaDTO.setAño(anio);
            bandejaDTO.setUsuarioSolicitante(b.getUsuarioSolicitante());
            bandejaDTO.setUsuarioAsignado(b.getUsuarioAsignado());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(b.getFechaRegistro());
            bandejaDTO.setAño(calendar.get(Calendar.YEAR));
            bandejaDTO.setMesRegistro(calendar.get(Calendar.MONTH) + 1);
            bandejaDTO.setDiaRegistro(calendar.get(Calendar.DAY_OF_MONTH));
            bandejaDTO.setDescripcion(b.getDescripcion());
            bandejaDTO.setTipo(b.getTipo());
            bandejaDTO.setLeido(b.getLeido());
            bandejaDTO.setBandejaID(b.getBandejaId());
            bandejaDTO.setFechaLeido(b.getFechaLeido());
            bandejaDTO.setFechaRegistro(b.getFechaRegistro());
            bandejaDTO.setRemanenteCuatrimestral(b.getRemanenteCuatrimestral());
            bandejaDTO.setRemanenteMensual(b.getRemanenteMensual());
            listBandejaDTO.add(bandejaDTO);
        }
        return listBandejaDTO;
    }

    @Override
    public void crearBandeja(Bandeja bandeja) {
        this.create(bandeja);
    }

    @Override
    public void editBandeja(BandejaDTO bandejaDTO) {
        Bandeja bandeja = new Bandeja();
        bandeja.setBandejaId(bandejaDTO.getBandejaID());
        bandeja.setDescripcion(bandejaDTO.getDescripcion());
        bandeja.setTipo(bandejaDTO.getTipo());
        bandeja.setFechaLeido(bandejaDTO.getFechaLeido());
        bandeja.setFechaRegistro(bandejaDTO.getFechaRegistro());
        bandeja.setLeido(bandejaDTO.getLeido());
        bandeja.setRemanenteCuatrimestral(bandejaDTO.getRemanenteCuatrimestral());
        bandeja.setRemanenteMensual(bandejaDTO.getRemanenteMensual());
        bandeja.setUsuarioSolicitante(bandejaDTO.getUsuarioSolicitante());
        bandeja.setUsuarioAsignado(bandejaDTO.getUsuarioAsignado());
        this.update(bandeja);
    }

    @Override
    public void generarNotificacion(List<Usuario> usuarioAsignadoList, Integer usuarioSolicitanteId,
            Integer remanenteCuatrimestralId, Integer remanenteAnualId, Institucion institucion,
            Integer remanenteMensualId, String descripcion, String tipo) {
        Email email = new Email();
        for (Usuario userAsignado : usuarioAsignadoList) {
            Bandeja bandeja = new Bandeja();
            Usuario us = new Usuario();
            us.setUsuarioId(usuarioSolicitanteId);
            RemanenteCuatrimestral rc = new RemanenteCuatrimestral();
            rc.setRemanenteCuatrimestralPK(new RemanenteCuatrimestralPK(remanenteCuatrimestralId, remanenteAnualId, institucion.getInstitucionId()));
            RemanenteMensual rm = new RemanenteMensual();
            rm.setRemanenteMensualId(remanenteMensualId);
            bandeja.setUsuarioSolicitante(us);
            bandeja.setRemanenteCuatrimestral(rc);
            if (remanenteMensualId == 0) {
                bandeja.setRemanenteMensual(null);
            } else {
                bandeja.setRemanenteMensual(rm);
            }

            bandeja.setDescripcion(descripcion);
            bandeja.setTipo(tipo);
            bandeja.setLeido(Boolean.FALSE);
            bandeja.setFechaRegistro(new Date());
            bandeja.setUsuarioAsignado(userAsignado);
            create(bandeja);
            try {
                String mensajeMail = descripcion;
                email.sendMail(userAsignado.getCorreoElectronico(), "Notificación Remanentes", mensajeMail);
            } catch (Exception ex) {
                Logger.getLogger(BandejaServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void editBandeja(Bandeja bandeja) {
        this.update(bandeja);
    }

    @Override
    public void generarNotificacionInventario(List<Usuario> usuarioAsignadoList, Integer usuarioSolicitanteId,
            Institucion institucion, Integer inventarioAnualId, String descripcion, String tipo) {
        Email email = new Email();
        for (Usuario userAsignado : usuarioAsignadoList) {
            Bandeja bandeja = new Bandeja();
            Usuario us = new Usuario();
            us.setUsuarioId(usuarioSolicitanteId);
            bandeja.setUsuarioSolicitante(us);
            bandeja.setRemanenteCuatrimestral(null);
            bandeja.setRemanenteMensual(null); 
            bandeja.setDescripcion(descripcion);
            bandeja.setTipo(tipo);
            bandeja.setLeido(Boolean.FALSE);
            bandeja.setFechaRegistro(new Date());
            bandeja.setUsuarioAsignado(userAsignado);
            create(bandeja);
            try {
                String mensajeMail = descripcion;
                email.sendMail(userAsignado.getCorreoElectronico(), "Notificación Inventarios", mensajeMail);
            } catch (Exception ex) {
                Logger.getLogger(BandejaServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}

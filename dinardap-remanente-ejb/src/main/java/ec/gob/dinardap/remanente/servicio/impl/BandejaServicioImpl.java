package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.persistence.util.DateBetween;
import ec.gob.dinardap.remanente.dao.BandejaDao;
import ec.gob.dinardap.remanente.dto.BandejaDTO;
import ec.gob.dinardap.remanente.mail.Email;
import ec.gob.dinardap.remanente.modelo.Bandeja;
import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestralPK;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Usuario;
import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        for (Bandeja b : listBandeja) {
            BandejaDTO bandejaDTO = new BandejaDTO();
            bandejaDTO.setUsuario(b.getUsuarioSolicitanteId());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(b.getFechaRegistro());
            bandejaDTO.setAño(calendar.get(Calendar.YEAR));
            bandejaDTO.setMesRegistro(calendar.get(Calendar.MONTH));
            bandejaDTO.setDiaRegistro(calendar.get(Calendar.DAY_OF_MONTH));
            bandejaDTO.setDescripcion(b.getDescripcion());
            bandejaDTO.setEstado(b.getEstado());
            bandejaDTO.setLeido(b.getLeido());
            bandejaDTO.setBandejaID(b.getBandejaId());
            listBandejaDTO.add(bandejaDTO);
        }
        return listBandejaDTO;
    }

    @Override
    public void crearBandeja(Bandeja bandeja) {
        this.create(bandeja);
    }

    @Override
    public void editBandeja(Bandeja bandeja) {
        this.update(bandeja);
    }

    @Override
    public void generarNotificacion(List<Usuario> usuarioAsignadoList, Integer usuarioSolicitanteId,
            Integer remanenteCuatrimestralId, Integer remanenteAnualId, InstitucionRequerida institucion,
            Integer remanenteMensualId, String descripcion, String estado) {
        Email email = new Email();
        for (Usuario userAsignado : usuarioAsignadoList) {
            Bandeja bandeja = new Bandeja();
            Usuario us = new Usuario();
            us.setUsuarioId(usuarioSolicitanteId);
            RemanenteCuatrimestral rc = new RemanenteCuatrimestral();
            rc.setRemanenteCuatrimestralPK(new RemanenteCuatrimestralPK(remanenteCuatrimestralId, remanenteAnualId, institucion.getInstitucionId()));
            RemanenteMensual rm = new RemanenteMensual();
            rm.setRemanenteMensualId(remanenteMensualId);
            bandeja.setUsuarioSolicitanteId(us);
            bandeja.setRemanenteCuatrimestral(rc);
            if (remanenteMensualId == 0) {
                bandeja.setRemanenteMensualId(null);
            } else {
                bandeja.setRemanenteMensualId(rm);
            }

            bandeja.setDescripcion(descripcion);
            bandeja.setEstado(estado);
            bandeja.setLeido(Boolean.FALSE);
            bandeja.setFechaRegistro(new Date());
            bandeja.setUsuarioAsignadoId(userAsignado);
            create(bandeja);
            System.out.println("Mensaje enviado a : " + userAsignado.getNombre());
            try {
                String mensajeMail = descripcion;
                email.sendMail(userAsignado.getEmail(), "Mensaje de Remanentes", mensajeMail);
            } catch (Exception ex) {
                Logger.getLogger(BandejaServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}

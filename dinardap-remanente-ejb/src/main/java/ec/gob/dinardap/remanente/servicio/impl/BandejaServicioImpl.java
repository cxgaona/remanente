package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.correo.mdb.cliente.ClienteQueueMailServicio;
import ec.gob.dinardap.correo.util.MailMessage;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.remanente.constante.ParametroEnum;
import ec.gob.dinardap.remanente.dao.BandejaDao;
import ec.gob.dinardap.remanente.dto.BandejaDTO;
import ec.gob.dinardap.remanente.modelo.Bandeja;

import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral;
import ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestralPK;
import ec.gob.dinardap.remanente.modelo.RemanenteMensual;

import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Stateless(name = "BandejaServicio")
public class BandejaServicioImpl extends GenericServiceImpl<Bandeja, Integer> implements BandejaServicio {

    @EJB
    private BandejaDao bandejaDao;
    @EJB
    private ParametroServicio parametroServicio;
    @EJB
    private ClienteQueueMailServicio clienteQueueMailServicio;

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
        MailMessage mailMessage = new MailMessage();
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
                ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
                URI uri = new URI(ext.getRequestScheme(),
                        null, ext.getRequestServerName(), ext.getRequestServerPort(),
                        ext.getRequestContextPath(), null, null);
                //String uri = "https://remanentesrm.dinardap.gob.ec/remanente";
                String mensajeMail = descripcion;
                StringBuilder html = new StringBuilder(
                        "<FONT FACE=\"Arial, sans-serif\"><center><h1><B>Sistema de Remanentes</B></h1></center><br/><br/>");
                html.append("Estimado(a) " + userAsignado.getNombre() + ", <br /><br />");
                html.append(mensajeMail + "<br/ ><br />");
                html.append("<a href='" + uri.toASCIIString() + "'>Sistema de Remanentes</a><br/ >");
                //html.append("<a href='" + uri + "'>Sistema de Remanentes</a><br/ >");
                html.append("Gracias por usar nuestros servicios.<br /><br /></FONT>");
                html.append("<FONT FACE=\"Arial Narrow, sans-serif\"><B> ");
                html.append("Dirección Nacional de Registros de Datos Públicos");
                html.append("</B></FONT>");
                List<String> to = new ArrayList<String>();
                StringBuilder asunto = new StringBuilder(200);
                to.add(userAsignado.getCorreoElectronico());
                asunto.append("Notificación Remanentes");
                mailMessage = credencialesCorreo();
                mailMessage.setTo(to);
                mailMessage.setSubject(asunto.toString());
                mailMessage.setText(html.toString());
                clienteQueueMailServicio.encolarMail(mailMessage);
            } catch (URISyntaxException ex) {
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
        MailMessage mailMessage = new MailMessage();
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
                ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
                URI uri = new URI(ext.getRequestScheme(),
                        null, ext.getRequestServerName(), ext.getRequestServerPort(),
                        ext.getRequestContextPath(), null, null);
                //String uri = "https://remanentesrm.dinardap.gob.ec/remanente";
                String mensajeMail = descripcion;
                StringBuilder html = new StringBuilder(
                        "<FONT FACE=\"Arial, sans-serif\"><center><h1><B>Sistema de Remanentes e Inventario de Libros</B></h1></center><br/><br/>");
                html.append("Estimado(a) " + userAsignado.getNombre() + ", <br /><br />");
                html.append(mensajeMail + "<br/ ><br />");
                html.append("<a href='" + uri.toASCIIString() + "'>Sistema de Remanentes e Inventario de Libros</a><br/ >");
                //html.append("<a href='" + uri + "'>Sistema de Remanentes e Inventario de Libros</a><br/ >");
                html.append("Gracias por usar nuestros servicios.<br /><br /></FONT>");
                html.append("<FONT FACE=\"Arial Narrow, sans-serif\"><B> ");
                html.append("Dirección Nacional de Registros de Datos Públicos");
                html.append("</B></FONT>");
                List<String> to = new ArrayList<String>();
                StringBuilder asunto = new StringBuilder(200);
                to.add(userAsignado.getCorreoElectronico());
                asunto.append("Notificación Inventario de Libros Registrales");
                mailMessage = credencialesCorreo();
                mailMessage.setTo(to);
                mailMessage.setSubject(asunto.toString());
                mailMessage.setText(html.toString());
                clienteQueueMailServicio.encolarMail(mailMessage);
            } catch (URISyntaxException ex) {
                Logger.getLogger(BandejaServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public MailMessage credencialesCorreo() {
        MailMessage credenciales = new MailMessage();
        credenciales.setFrom(parametroServicio.findByPk(ParametroEnum.MAIL_REMANENTE.name()).getValor());
        credenciales.setUsername(parametroServicio.findByPk(ParametroEnum.MAIL_USUARIO_REMANENTE.name()).getValor());
        credenciales.setPassword(parametroServicio.findByPk(ParametroEnum.MAIL_CONTRASENA_REMANENTE.name()).getValor());
        return credenciales;
    }
}

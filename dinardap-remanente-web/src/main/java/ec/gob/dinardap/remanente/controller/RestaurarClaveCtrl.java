package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.autorizacion.constante.SemillaEnum;
import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.remanente.mail.Email;
import ec.gob.dinardap.remanente.modelo.Pregunta;
import ec.gob.dinardap.remanente.modelo.Respuesta;
import ec.gob.dinardap.remanente.modelo.Usuario;
import ec.gob.dinardap.remanente.servicio.PreguntaServicio;
import ec.gob.dinardap.remanente.servicio.RespuestaServicio;
import ec.gob.dinardap.remanente.servicio.UsuarioServicio;
import ec.gob.dinardap.remanente.utils.FacesUtils;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "restaurarClaveCtrl")
@ViewScoped
public class RestaurarClaveCtrl extends BaseCtrl implements Serializable {

    private String usuario;
    private String contraseña;
    private Usuario u;
    private Integer numero;
    private String str, claveGenerada, encriptada, mensaje, strPregunta, respuesta;
    private Boolean desactivarBtnRestaurar, displaybtnLogin;
    private Pregunta preguntaSeguridad;
    private List<Pregunta> preguntasActivas;
    private Respuesta respuestaUser;

    @EJB
    private UsuarioServicio usuarioServicio;
    @EJB
    private PreguntaServicio preguntaServicio;
    @EJB
    private RespuestaServicio respuestaServicio;

    @PostConstruct
    protected void init() {
        usuario = "";
        contraseña = "";
        u = new Usuario();
        mensaje = "";
        desactivarBtnRestaurar = Boolean.FALSE;
        displaybtnLogin = Boolean.FALSE;
        preguntasActivas = preguntaServicio.getPreguntasActivas();
        Integer nume = (int) (Math.random() * preguntasActivas.size());
        preguntaSeguridad = preguntasActivas.get(nume);
        strPregunta = preguntaSeguridad.getPregunta();
    }

    public void recuperarContrasena() {
        u = usuarioServicio.getUsuarioByUsername(usuario);
        if (u.getUsuarioId() != null) {
            respuestaUser = respuestaServicio.getRespuestaByUsuario(u.getUsuarioId(), preguntaSeguridad.getPreguntaId());
            if (respuesta.equals(respuestaUser.getRespuesta())) {
                if (u.getEmail() != null && !u.getEmail().isEmpty()) {
                    claveGenerada = FacesUtils.generarContraseña();
                    encriptada = EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_REMANENTE.getSemilla() + claveGenerada);
                    u.setContrasena(encriptada);
                    usuarioServicio.editUsuario(u);
                    mensaje = "Su nueva contraseña ha sido enviada al correo: " + u.getEmail();
                    Email email = new Email();
                    try {
                        String mensajeMail = "Su nueva contraseña es: <b>" + claveGenerada + "</b>";
                        email.sendMail(u.getEmail(), "Restaurar clave REMANENTES", mensajeMail);
                    } catch (Exception ex) {
                        Logger.getLogger(RestaurarClaveCtrl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    mensaje = "Su usuario no tiene registrado un correo electrónico. <br/>"
                            + "Por favor contáctese con el administrador de la plataforma";
                }
                desactivarBtnRestaurar = Boolean.TRUE;
                displaybtnLogin = Boolean.TRUE;
            } else {
                mensaje = "Datos incorrectos! Por favor verificar.";
                desactivarBtnRestaurar = Boolean.FALSE;
                displaybtnLogin = Boolean.FALSE;
            }

        } else {
            mensaje = "Datos incorrectos! Por favor verificar.";
            desactivarBtnRestaurar = Boolean.FALSE;
            displaybtnLogin = Boolean.FALSE;
        }
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Usuario getU() {
        return u;
    }

    public void setU(Usuario u) {
        this.u = u;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getClaveGenerada() {
        return claveGenerada;
    }

    public void setClaveGenerada(String claveGenerada) {
        this.claveGenerada = claveGenerada;
    }

    public String getEncriptada() {
        return encriptada;
    }

    public void setEncriptada(String encriptada) {
        this.encriptada = encriptada;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getDesactivarBtnRestaurar() {
        return desactivarBtnRestaurar;
    }

    public void setDesactivarBtnRestaurar(Boolean desactivarBtnRestaurar) {
        this.desactivarBtnRestaurar = desactivarBtnRestaurar;
    }

    public Boolean getDisplaybtnLogin() {
        return displaybtnLogin;
    }

    public void setDisplaybtnLogin(Boolean displaybtnLogin) {
        this.displaybtnLogin = displaybtnLogin;
    }

    public String getStrPregunta() {
        return strPregunta;
    }

    public void setStrPregunta(String strPregunta) {
        this.strPregunta = strPregunta;
    }

    public List<Pregunta> getPreguntasActivas() {
        return preguntasActivas;
    }

    public void setPreguntasActivas(List<Pregunta> preguntasActivas) {
        this.preguntasActivas = preguntasActivas;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Pregunta getPreguntaSeguridad() {
        return preguntaSeguridad;
    }

    public void setPreguntaSeguridad(Pregunta preguntaSeguridad) {
        this.preguntaSeguridad = preguntaSeguridad;
    }

    public Respuesta getRespuestaUser() {
        return respuestaUser;
    }

    public void setRespuestaUser(Respuesta respuestaUser) {
        this.respuestaUser = respuestaUser;
    }
}

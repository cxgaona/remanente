package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.autorizacion.constante.SemillaEnum;
import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.seguridad.modelo.Respuesta;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.servicio.RespuestaServicio;
import ec.gob.dinardap.seguridad.servicio.UsuarioServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "usuarioCtrl")
@ViewScoped
public class UsuarioCtrl extends BaseCtrl implements Serializable {

    private Integer usuarioId;
    private Usuario usuario;
    private String tituloPagina;

    private String contraseñaActual;
    private String contraseñaNueva1;
    private String contraseñaNueva2;

    private Boolean updateContraseña;

    private List<Respuesta> respuestaList;

    @EJB
    private UsuarioServicio usuarioServicio;

    @EJB
    private RespuestaServicio respuestaServicio;

    @PostConstruct
    protected void init() {
        updateContraseña = Boolean.FALSE;
        usuarioId = Integer.parseInt(this.getSessionVariable("usuarioId"));
        usuario = new Usuario();
        contraseñaActual = "";
        respuestaList = new ArrayList<Respuesta>();
        usuario = usuarioServicio.findByPk(usuarioId);
        respuestaList = respuestaServicio.getRespuestasActivas(usuarioId);
        tituloPagina = "Usuario: " + usuario.getNombre();
    }

    public void guardarCambios() {
        Boolean respuestasVacias = Boolean.FALSE;
        String contraseñaActualEncriptada = EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_REMANENTE.getSemilla() + contraseñaActual);
        if (usuario.getContrasena().equals(contraseñaActualEncriptada)) {
            for (Respuesta r : respuestaList) {
                respuestaServicio.update(r);
                if (r.getRespuesta().isEmpty()) {
                    respuestasVacias = Boolean.TRUE;
                }
            }
            if (!respuestasVacias) {
                if (updateContraseña) {
                    if (contraseñaNueva1.equals(contraseñaNueva2)) {
                        usuario.setContrasena(EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_REMANENTE.getSemilla() + contraseñaNueva1));
                        usuarioServicio.update(usuario);
                        addInfoMessage("Información actualizada satisfactoriamente", "");
                        addInfoMessage("Contraseña Actualizada satisfactoriamente", "");
                    } else {
                        addErrorMessage("1", "La nueva contraseña no coincide", "");
                    }
                } else {
                    addInfoMessage("Información actualizada satisfactoriamente", "");
                }
                usuario = new Usuario();
                respuestaList = new ArrayList<Respuesta>();
                usuario = usuarioServicio.findByPk(usuarioId);
                respuestaList = respuestaServicio.getRespuestasActivas(usuarioId);
                contraseñaActual = "";
            } else {
                addErrorMessage("1", "Es necesario responder todas las preguntas de seguridad", "");
            }
        } else {
            addErrorMessage("2", "Contraseña actual incorrecta", "");
        }
    }

    public void cancelar() {
        contraseñaActual = "";
        contraseñaNueva1 = "";
        contraseñaNueva2 = "";
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public String getContraseñaActual() {
        return contraseñaActual;
    }

    public void setContraseñaActual(String contraseñaActual) {
        this.contraseñaActual = contraseñaActual;
    }

    public String getContraseñaNueva1() {
        return contraseñaNueva1;
    }

    public void setContraseñaNueva1(String contraseñaNueva1) {
        this.contraseñaNueva1 = contraseñaNueva1;
    }

    public String getContraseñaNueva2() {
        return contraseñaNueva2;
    }

    public void setContraseñaNueva2(String contraseñaNueva2) {
        this.contraseñaNueva2 = contraseñaNueva2;
    }

    public Boolean getUpdateContraseña() {
        return updateContraseña;
    }

    public void setUpdateContraseña(Boolean updateContraseña) {
        this.updateContraseña = updateContraseña;
    }

    public List<Respuesta> getRespuestaList() {
        return respuestaList;
    }

    public void setRespuestaList(List<Respuesta> respuestaList) {
        this.respuestaList = respuestaList;
    }

}

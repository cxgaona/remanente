package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.autorizacion.constante.SemillaEnum;
import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.remanente.modelo.Usuario;
import ec.gob.dinardap.remanente.servicio.UsuarioServicio;
import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "loginCtrl")
@ViewScoped
public class LoginCtrl extends BaseCtrl implements Serializable {

    private String usuario;
    private String contraseña;
    private Usuario u;

    @EJB
    private UsuarioServicio usuarioServicio;

    @PostConstruct
    protected void init() {
        usuario = "";
        contraseña = "";
        u = new Usuario();
    }

    public void ingresar() throws IOException {
        String cadena = SemillaEnum.SEMILLA_REMANENTE.getSemilla() + contraseña;
        u = usuarioServicio.login(usuario, EncriptarCadenas.encriptarCadenaSha1(cadena));
        if (u != null) {
            String variableSesionPerfil = "";
            if (u.getRegistrador()) {
                variableSesionPerfil += "REM-Registrador";
            }
            if (u.getVerificador()) {
                variableSesionPerfil += "REM-Verificador";
            }
            if (u.getValidador()) {
                    variableSesionPerfil += "REM-Validador";
            }
            if (u.getAdministrador()) {
                variableSesionPerfil += "REM-Administrador";
            }
            this.setSessionVariable("perfil", variableSesionPerfil);
            this.setSessionVariable("usuarioId", u.getUsuarioId().toString());
            this.setSessionVariable("institucionId", u.getInstitucionId().getInstitucionId().toString());
            System.out.println(this.getSessionVariable("perfil"));
            FacesContext.getCurrentInstance().getExternalContext().redirect("paginas/brand.jsf");
        } else {
            u = new Usuario();
            usuario = "";
            contraseña = "";
            this.addInfoMessage("Usuario o contraseña Incorrecto", "asd");
        }
    }

    public void cancelar() {
        usuario = "";
        contraseña = "";
    }

    public void recuperarContrasena() {
        System.out.println("===Función Recuperar contraseña===");
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
}

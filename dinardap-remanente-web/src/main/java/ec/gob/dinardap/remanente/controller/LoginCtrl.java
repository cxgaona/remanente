package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.autorizacion.constante.SemillaEnum;
import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.remanente.dto.UsuarioDTO;
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
    private UsuarioDTO u;
    private Integer numero;
    private String str, claveGenerada, encriptada;

    @EJB
    private UsuarioServicio usuarioServicio;

    @PostConstruct
    protected void init() {
        usuario = "";
        contraseña = "";
        u = new UsuarioDTO();
    }

    public void ingresar() throws IOException {
        u = new UsuarioDTO();
        String cadena = SemillaEnum.SEMILLA_REMANENTE.getSemilla() + contraseña;
        u = usuarioServicio.login(usuario, EncriptarCadenas.encriptarCadenaSha1(cadena));
        System.out.println("Despues del Servicio");
        if (u != null) {
            String variableSesionPerfil = "";
            if (u.getRegistrador()) {
                variableSesionPerfil += "REM-Registrador, ";
            }
            if (u.getVerificador()) {
                variableSesionPerfil += "REM-Verificador, ";
            }
            if (u.getValidador()) {
                variableSesionPerfil += "REM-Validador, ";
            }
            if (u.getAdministrador()) {
                variableSesionPerfil += "REM-Administrador, ";
            }
            this.setSessionVariable("perfil", variableSesionPerfil);
            this.setSessionVariable("usuarioId", u.getUsuarioID().toString());
            this.setSessionVariable("institucionId", u.getInstitucionID().toString());
            this.setSessionVariable("institucionTipo", u.getTipo());
            this.setSessionVariable("gadId", u.getGadID().toString());            

            System.out.println(this.getSessionVariable("perfil"));
            System.out.println(this.getSessionVariable("usuarioId"));
            System.out.println(this.getSessionVariable("institucionId"));
            System.out.println(this.getSessionVariable("institucionTipo"));
            System.out.println(this.getSessionVariable("gadId"));

            FacesContext.getCurrentInstance().getExternalContext().redirect("paginas/brand.jsf");
        } else {
            u = new UsuarioDTO();
            usuario = "";
            contraseña = "";
            this.addInfoMessage("Usuario o contraseña Incorrecta", "asd");
        }
    }

    public void cancelar() {
        usuario = "";
        contraseña = "";
    }

    public void recuperarContrasena() {
        str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        claveGenerada = "";
        for (Integer i = 0; i < 8; i++) {
            numero = (int) (Math.random() * 36);
            claveGenerada = claveGenerada + str.substring(numero, numero + 1);
        }
        System.out.println("clave generada:" + claveGenerada);
        encriptada = EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_REMANENTE.getSemilla() + claveGenerada);

        System.out.println("clave generada:" + encriptada);
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

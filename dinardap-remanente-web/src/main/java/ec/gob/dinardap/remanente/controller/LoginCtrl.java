package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.autorizacion.constante.SemillaEnum;
import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.remanente.constante.PerfilEnum;
import ec.gob.dinardap.seguridad.dao.UsuarioDao;
import ec.gob.dinardap.seguridad.dto.ValidacionDto;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.UsuarioServicio;
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
    //Declaración de variables
    //Variables de control visual

    //Variables de negocio
    private String usuario;
    private String contraseña;
    private ValidacionDto validacionDto;

    //Listas
    private Integer numero;
    private String str, claveGenerada, encriptada;

    @EJB
    private UsuarioServicio usuarioServicio;
    @EJB
    private UsuarioDao usuarioDao;
    @EJB
    private InstitucionServicio institucionServicio;

    @PostConstruct
    protected void init() {
        this.logout();
        usuario = "";
        contraseña = "";
        validacionDto = new ValidacionDto();
    }

    public void ingresar() throws IOException {
        validacionDto = new ValidacionDto();
        String cadena = SemillaEnum.SEMILLA_REMANENTE.getSemilla() + contraseña;

        validacionDto = usuarioDao.validarUsuarioArreglo(usuario, EncriptarCadenas.encriptarCadenaSha1(cadena), 2);
        if (validacionDto != null) {
            setSessionVariable("perfil", validacionDto.getPerfil());
            setSessionVariable("usuarioId", validacionDto.getUsuarioId().toString());
            setSessionVariable("institucionId", validacionDto.getInstitucion());            
            setSessionVariable("institucionTipo", institucionServicio.findByPk(Integer.parseInt(validacionDto.getInstitucion())).getTipoInstitucion().getTipoInstitucionId().toString());
//            setSessionVariable("gadId", u.getGadID().toString());

            if (validacionDto.getPerfil().contains(PerfilEnum.SEGURIDAD_ADMINISTRADOR.getPerfilId().toString())) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("paginas/gestionUsuarios.jsf");
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("paginas/brand.jsf");
            }
        } else {
            validacionDto = new ValidacionDto();
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
        encriptada = EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_REMANENTE.getSemilla() + claveGenerada);
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

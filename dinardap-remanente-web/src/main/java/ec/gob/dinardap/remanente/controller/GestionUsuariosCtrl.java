package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.InstitucionRequerida_;
import ec.gob.dinardap.remanente.modelo.Usuario;
import ec.gob.dinardap.remanente.servicio.UsuarioServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "gestionUsuariosCtrl")
@ViewScoped
public class GestionUsuariosCtrl extends BaseCtrl implements Serializable {

    private List<Usuario> usuarioActivoList;
    private String tituloPagina;
    private Usuario usuarioSelected;

    @EJB
    private UsuarioServicio usuarioServicio;

    @PostConstruct
    protected void init() {
        tituloPagina = "Gestión de Usuarios";
        usuarioActivoList = new ArrayList<Usuario>();
        usuarioActivoList = usuarioServicio.getUsuariosActivos();
        for (Usuario u : usuarioActivoList) {
            System.out.println("u = " + u.getNombre());
            System.out.println("u = " + u.getUsuario());
            System.out.println("u = " + u.getUsuarioId());
            System.out.println("u = " + u.getContrasena());
            System.out.println("u = " + u.getEstado());
        }
    }

    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public List<Usuario> getUsuarioActivoList() {
        return usuarioActivoList;
    }

    public void setUsuarioActivoList(List<Usuario> usuarioActivoList) {
        this.usuarioActivoList = usuarioActivoList;
    }

    public Usuario getUsuarioSelected() {
        return usuarioSelected;
    }

    public void setUsuarioSelected(Usuario usuarioSelected) {
        this.usuarioSelected = usuarioSelected;
    }

}

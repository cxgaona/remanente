package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.Usuario;
import ec.gob.dinardap.remanente.servicio.UsuarioServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "gestionUsuariosCtrl")
@ViewScoped
public class GestionUsuariosCtrl extends BaseCtrl implements Serializable {

    private List<Usuario> usuarioActivoList;
    private String tituloPagina;
    private Usuario usuarioSelected;

    private Boolean formUsuarioSelectedActivated;
    private String btnGuardar;
    private Boolean onAdd;
    private Boolean onEdit;

    @EJB
    private UsuarioServicio usuarioServicio;

    @PostConstruct
    protected void init() {
        tituloPagina = "Gestión de Usuarios";
        btnGuardar = "";
        formUsuarioSelectedActivated = Boolean.FALSE;
        usuarioActivoList = new ArrayList<Usuario>();
        usuarioActivoList = usuarioServicio.getUsuariosActivos();
    }

    public void change() {
//        System.out.println("Aqui: " + usuarioSelected.getInstitucionId().getTipo());
    }

    public void cancelar() {
        usuarioSelected = new Usuario();
        formUsuarioSelectedActivated = Boolean.FALSE;
    }

    public void nuevoUsuario() {
        usuarioSelected = new Usuario();
        btnGuardar = "Guardar";
        formUsuarioSelectedActivated = Boolean.TRUE;
    }

    public void onRowSelectUsuario() {
        formUsuarioSelectedActivated = Boolean.TRUE;
        btnGuardar = "Actualizar";
    }

    public void selectTipoInstitucion(ValueChangeEvent even) {
        
    }

    public List<String> completeNombreInstitucion(String query) {
        List<String> results = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            results.add(query + i);
        }
        return results;
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

    public Boolean getFormUsuarioSelectedActivated() {
        return formUsuarioSelectedActivated;
    }

    public void setFormUsuarioSelectedActivated(Boolean formUsuarioSelectedActivated) {
        this.formUsuarioSelectedActivated = formUsuarioSelectedActivated;
    }

    public String getBtnGuardar() {
        return btnGuardar;
    }

    public void setBtnGuardar(String btnGuardar) {
        this.btnGuardar = btnGuardar;
    }

}

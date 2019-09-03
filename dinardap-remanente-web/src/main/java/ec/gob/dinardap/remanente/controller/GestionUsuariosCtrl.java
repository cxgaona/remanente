package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.Usuario;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
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
    private List<InstitucionRequerida> institucionRequeridaList;
    private String tituloPagina;
    private Usuario usuarioSelected;

    private String nombre;

    private Boolean formUsuarioSelectedActivated;
    private String btnGuardar;

    private String tipoInstitucion;

    @EJB
    private UsuarioServicio usuarioServicio;

    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;

    @PostConstruct
    protected void init() {
        tituloPagina = "Gestión de Usuarios";
        nombre = "";
        btnGuardar = "";
        tipoInstitucion = "";
        institucionRequeridaList = new ArrayList<InstitucionRequerida>();
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
        if (usuarioSelected.getInstitucionId().getTipo().equals("SIN GAD") || usuarioSelected.getInstitucionId().getTipo().equals("CON GAD")) {
            tipoInstitucion = "Registro Propiedad / Mercantil";
            institucionRequeridaList = institucionRequeridaServicio.getRegistroMixtoList();
        } else if (usuarioSelected.getInstitucionId().getTipo().equals("GAD")) {
            tipoInstitucion = "GAD";
            institucionRequeridaList = institucionRequeridaServicio.getGADList();
        } else if (usuarioSelected.getInstitucionId().getTipo().equals("REGIONAL")) {
            tipoInstitucion = "Dirección Regional";
            institucionRequeridaList = institucionRequeridaServicio.getDireccionRegionalList();
        }
    }

    public void seleccionarTipoInstitucion() {
        System.out.println("===En selección de tipo de Institución===");
        System.out.println("Seleccion: " + tipoInstitucion);
        if (tipoInstitucion.equals("Registro Propiedad / Mercantil")) {
            tipoInstitucion = "Registro Propiedad / Mercantil";
            institucionRequeridaList = institucionRequeridaServicio.getRegistroMixtoList();
        } else if (tipoInstitucion.equals("GAD")) {
            tipoInstitucion = "GAD";
            institucionRequeridaList = institucionRequeridaServicio.getGADList();
        } else if (tipoInstitucion.equals("Dirección Regional")) {
            tipoInstitucion = "Dirección Regional";
            institucionRequeridaList = institucionRequeridaServicio.getDireccionRegionalList();
        }

    }

    public List<InstitucionRequerida> completeNombreInstitucion(String query) {
        List<InstitucionRequerida> filteredInstituciones = new ArrayList<InstitucionRequerida>();
        for (InstitucionRequerida ir : institucionRequeridaList) {
            if (ir.getNombre().toLowerCase().contains(query)) {
                filteredInstituciones.add(ir);
            }
        }
        return filteredInstituciones;
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

    public String getTipoInstitucion() {
        return tipoInstitucion;
    }

    public void setTipoInstitucion(String tipoInstitucion) {
        this.tipoInstitucion = tipoInstitucion;
    }

    public List<InstitucionRequerida> getInstitucionRequeridaList() {
        return institucionRequeridaList;
    }

    public void setInstitucionRequeridaList(List<InstitucionRequerida> institucionRequeridaList) {
        this.institucionRequeridaList = institucionRequeridaList;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}

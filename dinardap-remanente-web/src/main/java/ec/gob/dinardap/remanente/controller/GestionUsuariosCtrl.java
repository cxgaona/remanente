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
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "gestionUsuariosCtrl")
@ViewScoped
public class GestionUsuariosCtrl extends BaseCtrl implements Serializable {

    private Boolean onEdit;
    private Boolean onCreate;
    private Boolean renderEdition;

    private List<Usuario> usuarioActivoList;
    private List<InstitucionRequerida> institucionRequeridaList;
    private String tituloPagina;
    private Usuario usuarioSelected;

    private String nombre;

//    private Boolean formUsuarioSelectedActivated;
    private String btnGuardar;

    private String tipoInstitucion;

    @EJB
    private UsuarioServicio usuarioServicio;

    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;

    @PostConstruct
    protected void init() {
        onCreate = Boolean.FALSE;
        onEdit = Boolean.FALSE;
        renderEdition = Boolean.FALSE;
        //===
        tituloPagina = "Gestión de Usuarios";
        nombre = "";
        btnGuardar = "";
        tipoInstitucion = "";
        //===
        institucionRequeridaList = new ArrayList<InstitucionRequerida>();
        usuarioActivoList = new ArrayList<Usuario>();
        usuarioActivoList = usuarioServicio.getUsuariosActivos();
    }

    public void nuevoUsuario() {
        renderEdition = Boolean.TRUE;
        onCreate = Boolean.TRUE;
        onEdit = Boolean.FALSE;
        usuarioSelected = new Usuario();
        btnGuardar = "Guardar";
        tipoInstitucion = "Dirección Regional";
        institucionRequeridaList = institucionRequeridaServicio.getDireccionRegionalList();
    }

    public void onRowSelectUsuario() {
        renderEdition = Boolean.TRUE;
        onCreate = Boolean.FALSE;
        onEdit = Boolean.TRUE;
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

    public void cancelar() {
        usuarioActivoList = new ArrayList<Usuario>();
        usuarioSelected = new Usuario();
        usuarioActivoList = usuarioServicio.getUsuariosActivos();
        onEdit = Boolean.FALSE;
        onCreate = Boolean.FALSE;
        renderEdition = Boolean.FALSE;
    }

    public void guardar() {
        if (onCreate) {
            usuarioSelected.setEstado("A");
            usuarioServicio.createUsuario(usuarioSelected);
            usuarioActivoList = new ArrayList<Usuario>();
            usuarioSelected = new Usuario();
            usuarioActivoList = usuarioServicio.getUsuariosActivos();
            onEdit = Boolean.FALSE;
            onCreate = Boolean.FALSE;
            renderEdition = Boolean.FALSE;
        } else if (onEdit) {
            usuarioServicio.editUsuario(usuarioSelected);
            usuarioActivoList = new ArrayList<Usuario>();
            usuarioSelected = new Usuario();
            usuarioActivoList = usuarioServicio.getUsuariosActivos();
            onEdit = Boolean.FALSE;
            onCreate = Boolean.FALSE;
            renderEdition = Boolean.FALSE;
        }
    }

    public void seleccionarTipoInstitucion() {
        InstitucionRequerida ir = new InstitucionRequerida();
        usuarioSelected.setInstitucionId(ir);
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

    //Getters & Setters
    public Boolean getRenderEdition() {
        return renderEdition;
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

    public Boolean getOnCreate() {
        return onCreate;
    }

}

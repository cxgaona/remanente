package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.autorizacion.constante.SemillaEnum;
import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
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

    private Boolean disabledRegistrador;
    private Boolean disabledVerificador;
    private Boolean disabledValidador;
    private Boolean disabledAdministrador;

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

        disabledRegistrador = Boolean.TRUE;
        disabledVerificador = Boolean.TRUE;
        disabledValidador = Boolean.TRUE;
        disabledAdministrador = Boolean.TRUE;

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
        disabledRegistrador = Boolean.TRUE;
        disabledVerificador = Boolean.TRUE;
        disabledValidador = Boolean.TRUE;
        disabledAdministrador = Boolean.TRUE;
        usuarioSelected = new Usuario();
        usuarioSelected.setValidador(true);
        btnGuardar = "Guardar";
        tipoInstitucion = "Dirección Regional";
        institucionRequeridaList = institucionRequeridaServicio.getDireccionRegionalList();
    }

    public void cambioRolReg() {
        if (usuarioSelected.getRegistrador()) {
            usuarioSelected.setVerificador(Boolean.FALSE);
        } else {
            usuarioSelected.setVerificador(Boolean.TRUE);
        }

    }

    public void cambioRolVer() {
        if (usuarioSelected.getVerificador()) {
            usuarioSelected.setRegistrador(Boolean.FALSE);
        } else {
            usuarioSelected.setRegistrador(Boolean.TRUE);
        }
    }

    public void onRowSelectUsuario() {
        renderEdition = Boolean.TRUE;
        onCreate = Boolean.FALSE;
        onEdit = Boolean.TRUE;
        btnGuardar = "Actualizar";
        disabledRegistrador = Boolean.TRUE;
        disabledVerificador = Boolean.TRUE;
        disabledValidador = Boolean.TRUE;
        disabledAdministrador = Boolean.TRUE;
        if (usuarioSelected.getInstitucionId().getTipo().equals("SIN GAD") || usuarioSelected.getInstitucionId().getTipo().equals("CON GAD")) {
            tipoInstitucion = "Registro Propiedad / Mercantil";
            institucionRequeridaList = institucionRequeridaServicio.getRegistroMixtoList();
            disabledRegistrador = Boolean.FALSE;
            disabledVerificador = Boolean.FALSE;
            disabledValidador = Boolean.TRUE;
            disabledAdministrador = Boolean.TRUE;

        } else if (usuarioSelected.getInstitucionId().getTipo().equals("GAD")) {
            tipoInstitucion = "GAD";
            institucionRequeridaList = institucionRequeridaServicio.getGADList();
            disabledRegistrador = Boolean.TRUE;
            disabledVerificador = Boolean.TRUE;
            disabledValidador = Boolean.TRUE;
            disabledAdministrador = Boolean.TRUE;
        } else if (usuarioSelected.getInstitucionId().getTipo().equals("REGIONAL")) {
            tipoInstitucion = "Dirección Regional";
            institucionRequeridaList = institucionRequeridaServicio.getDireccionRegionalList();
            disabledRegistrador = Boolean.TRUE;
            disabledVerificador = Boolean.TRUE;
            disabledValidador = Boolean.TRUE;
            disabledAdministrador = Boolean.TRUE;
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
            String contraseñaActualEncriptada = EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_REMANENTE.getSemilla() + usuarioSelected.getContrasena());
            usuarioSelected.setEstado("A");
            usuarioSelected.setContrasena(contraseñaActualEncriptada);
            usuarioServicio.createUsuario(usuarioSelected);
            usuarioActivoList = new ArrayList<Usuario>();
            usuarioSelected = new Usuario();
            usuarioActivoList = usuarioServicio.getUsuariosActivos();
            onEdit = Boolean.FALSE;
            onCreate = Boolean.FALSE;
            renderEdition = Boolean.FALSE;
        } else if (onEdit) {
            String contraseñaActualEncriptada = EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_REMANENTE.getSemilla() + usuarioSelected.getContrasena());
            usuarioSelected.setContrasena(contraseñaActualEncriptada);
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
            usuarioSelected.setValidador(false);
            usuarioSelected.setAdministrador(false);
            usuarioSelected.setVerificador(false);
            usuarioSelected.setRegistrador(true);

            disabledRegistrador = Boolean.FALSE;
            disabledValidador = Boolean.TRUE;
            disabledVerificador = Boolean.FALSE;
            disabledAdministrador = Boolean.TRUE;
        } else if (tipoInstitucion.equals("GAD")) {
            tipoInstitucion = "GAD";
            institucionRequeridaList = institucionRequeridaServicio.getGADList();
            usuarioSelected.setValidador(false);
            usuarioSelected.setAdministrador(false);
            usuarioSelected.setVerificador(true);
            usuarioSelected.setRegistrador(false);

            disabledRegistrador = Boolean.TRUE;
            disabledValidador = Boolean.TRUE;
            disabledVerificador = Boolean.TRUE;
            disabledAdministrador = Boolean.TRUE;
        } else if (tipoInstitucion.equals("Dirección Regional")) {
            tipoInstitucion = "Dirección Regional";
            institucionRequeridaList = institucionRequeridaServicio.getDireccionRegionalList();
            usuarioSelected.setValidador(true);
            usuarioSelected.setAdministrador(false);
            usuarioSelected.setVerificador(false);
            usuarioSelected.setRegistrador(false);

            disabledRegistrador = Boolean.TRUE;
            disabledValidador = Boolean.TRUE;
            disabledVerificador = Boolean.TRUE;
            disabledAdministrador = Boolean.TRUE;
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

    public Boolean getDisabledRegistrador() {
        return disabledRegistrador;
    }

    public void setDisabledRegistrador(Boolean disabledRegistrador) {
        this.disabledRegistrador = disabledRegistrador;
    }

    public Boolean getDisabledVerificador() {
        return disabledVerificador;
    }

    public void setDisabledVerificador(Boolean disabledVerificador) {
        this.disabledVerificador = disabledVerificador;
    }

    public Boolean getDisabledValidador() {
        return disabledValidador;
    }

    public void setDisabledValidador(Boolean disabledValidador) {
        this.disabledValidador = disabledValidador;
    }

    public Boolean getDisabledAdministrador() {
        return disabledAdministrador;
    }

    public void setDisabledAdministrador(Boolean disabledAdministrador) {
        this.disabledAdministrador = disabledAdministrador;
    }

}

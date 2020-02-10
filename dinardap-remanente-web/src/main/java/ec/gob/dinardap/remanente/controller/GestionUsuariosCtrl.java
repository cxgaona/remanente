package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.autorizacion.constante.SemillaEnum;
import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.remanente.mail.Email;
import ec.gob.dinardap.remanente.modelo.InstitucionRequerida;
import ec.gob.dinardap.remanente.modelo.Pregunta;
import ec.gob.dinardap.remanente.modelo.Respuesta;
import ec.gob.dinardap.remanente.modelo.Usuario;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
import ec.gob.dinardap.remanente.servicio.PreguntaServicio;
import ec.gob.dinardap.remanente.servicio.RespuestaServicio;
import ec.gob.dinardap.remanente.servicio.UsuarioServicio;
import ec.gob.dinardap.remanente.utils.FacesUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named(value = "gestionUsuariosCtrl")
@ViewScoped
public class GestionUsuariosCtrl extends BaseCtrl implements Serializable {

    //****Declaración de variables****//
    //Variables de control visual
    private Boolean onEdit;
    private Boolean onCreate;
    private Boolean renderEdition;

    private Boolean disabledRegistrador;
    private Boolean disabledVerificador;
    private Boolean disabledValidador;
    private Boolean disabledAdministrador;
    private Boolean disabledRestablecer;

    //Listas
    private List<Usuario> usuarioActivoList;
    private List<InstitucionRequerida> institucionRequeridaList;
    private List<Pregunta> preguntaList;
    private Boolean restablecer;
    private String tituloPagina;
    private Usuario usuarioSelected;

    private String nombre;
    private String btnGuardar;
    private String tipoInstitucion;

    @EJB
    private UsuarioServicio usuarioServicio;

    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;

    @EJB
    private PreguntaServicio preguntaServicio;

    @EJB
    private RespuestaServicio respuestaServicio;

    @PostConstruct
    protected void init() {
        onCreate = Boolean.FALSE;
        onEdit = Boolean.FALSE;
        renderEdition = Boolean.FALSE;

        disabledRegistrador = Boolean.TRUE;
        disabledVerificador = Boolean.TRUE;
        disabledValidador = Boolean.TRUE;
        disabledAdministrador = Boolean.TRUE;
        disabledRestablecer = Boolean.TRUE;

        restablecer = Boolean.FALSE;

        tituloPagina = "Gestión de Usuarios";
        nombre = "";
        btnGuardar = "";
        tipoInstitucion = "";

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
        disabledRestablecer = Boolean.TRUE;
        restablecer = Boolean.TRUE;

        institucionRequeridaList = institucionRequeridaServicio.getDireccionNacionalList();
        usuarioSelected = new Usuario();
        usuarioSelected.setInstitucionId(institucionRequeridaList.get(institucionRequeridaList.size() - 1));
        usuarioSelected.setAdministrador(Boolean.TRUE);
        usuarioSelected.setValidador(Boolean.FALSE);
        usuarioSelected.setRegistrador(Boolean.FALSE);
        usuarioSelected.setVerificador(Boolean.FALSE);

        btnGuardar = "Guardar";
        tipoInstitucion = "Dirección Nacional";

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
        disabledRestablecer = Boolean.FALSE;
        restablecer = Boolean.FALSE;
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
        String contraseña = "";
        Usuario userExistente = new Usuario();
        userExistente = usuarioServicio.getUsuarioByUsername(usuarioSelected.getUsuario());
        if (usuarioSelected.getEmail() != null && !usuarioSelected.getEmail().isEmpty()) {
            if (restablecer) {
                contraseña = FacesUtils.generarContraseña();
                usuarioSelected.setContrasena(EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_REMANENTE.getSemilla() + contraseña));
            }
            if (onCreate) {
                if (userExistente.getUsuarioId() == null) {
                    usuarioSelected.setEstado("A");
                    usuarioServicio.createUsuario(usuarioSelected);
                    preguntaList = new ArrayList<Pregunta>();
                    preguntaList = preguntaServicio.getPreguntasActivas();
                    for (Pregunta p : preguntaList) {
                        Respuesta respuesta = new Respuesta();
                        respuesta.setUsuarioId(usuarioSelected);
                        respuesta.setPreguntaId(p);
                        respuesta.setRespuesta("");
                        respuesta.setEstado("A");
                        respuestaServicio.create(respuesta);
                    }
                    if (restablecer) {
                        correoRestablecerContraseña(contraseña);
                        this.addInfoMessage("El usuario se creó satisfactoriamente. El usuario y contraseña se ha enviado a " + usuarioSelected.getEmail(), "");
                    }
                    usuarioActivoList = new ArrayList<Usuario>();
                    usuarioSelected = new Usuario();
                    usuarioActivoList = usuarioServicio.getUsuariosActivos();

                    onEdit = Boolean.FALSE;
                    onCreate = Boolean.FALSE;
                    renderEdition = Boolean.FALSE;
                } else {
                    this.addErrorMessage("1", "El usuario ingresado ya existe", "");
                }
            } else if (onEdit) {
                if (userExistente.getUsuarioId() == null || userExistente.getUsuarioId().equals(usuarioSelected.getUsuarioId())) {
                    usuarioServicio.editUsuario(usuarioSelected);
                    this.addInfoMessage("El usuario se actualizó satisfactoriamente.", "");
                    if (restablecer) {
                        correoRestablecerContraseña(contraseña);
                        this.addInfoMessage("La contraseña actualizada se ha enviado a " + usuarioSelected.getEmail(), "");
                    }

                    usuarioActivoList = new ArrayList<Usuario>();
                    usuarioSelected = new Usuario();
                    usuarioActivoList = usuarioServicio.getUsuariosActivos();

                    onEdit = Boolean.FALSE;
                    onCreate = Boolean.FALSE;
                    renderEdition = Boolean.FALSE;
                } else {
                    this.addErrorMessage("1", "El usuario ingresado ya existe", "");
                }
            }
        } else {
            this.addErrorMessage("1", "Debe ingresar un correo válido", "");
        }
    }

    public void eliminarUsuario() {
        usuarioSelected.setEstado("I");
        usuarioServicio.editUsuario(usuarioSelected);
        usuarioActivoList = new ArrayList<Usuario>();
        usuarioSelected = new Usuario();
        usuarioActivoList = usuarioServicio.getUsuariosActivos();
    }

    public void crearUsuariosBloque(FileUploadEvent event) {
        try {
            UploadedFile uploadedFile = event.getFile();
            InputStream in = uploadedFile.getInputstream();
            XSSFWorkbook worbook = new XSSFWorkbook(in);
            XSSFSheet sheet = worbook.getSheetAt(0);

            XSSFRow row;
            XSSFCell cell;

            List<Usuario> usuarioNuevoList = new ArrayList<Usuario>();

            for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
                row = sheet.getRow(r);
                Usuario usuarioNuevo = new Usuario();
                if (row.getRowNum() != 0) {
                    for (int c = 0; c < (int) row.getLastCellNum(); c++) {
                        String dato = "";
                        cell = row.getCell(c);
                        if (cell == null) {
                            dato = null;
                        } else {
                            switch (cell.getCellType()) {
                                case NUMERIC:
                                    int val = (int) cell.getNumericCellValue();
                                    dato = val + "";
                                    break;
                                case FORMULA:
                                    FormulaEvaluator evaluator = worbook.getCreationHelper().createFormulaEvaluator();
                                    CellValue cellValue = evaluator.evaluate(cell);
                                    switch (cellValue.getCellType()) {
                                        case STRING:
                                            System.out.print(cellValue.getStringValue());
                                            dato = cellValue.getStringValue();
                                            break;
                                        case NUMERIC:
                                            System.out.print(cellValue.getNumberValue());
                                            dato = (int) cellValue.getNumberValue() + "";
                                            break;
                                    }
                                    break;
                                case STRING:
                                    dato = cell.getStringCellValue();
                                    break;
                                case BLANK:
                                    dato = "";
                                    break;
                                default:
                                    dato = cell.getStringCellValue();
                                    break;
                            }
                        }
                        switch (cell.getColumnIndex()) {
                            case 0:
                                InstitucionRequerida ir = new InstitucionRequerida();
                                if (dato == null) {
                                    ir.setInstitucionId(null);
                                } else {
                                    ir.setInstitucionId(Integer.parseInt(dato));
                                }
                                usuarioNuevo.setInstitucionId(ir);
                                break;
                            case 1:
                                usuarioNuevo.setNombre(dato);
                                break;
                            case 2:
                                usuarioNuevo.setEmail(dato);
                                break;
                            case 3:
                                usuarioNuevo.setUsuario(dato);
                                usuarioNuevo.setContrasena(EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_REMANENTE.getSemilla() + "R" + dato + "R"));
                                break;
                            case 4:
                                usuarioNuevo.setRegistrador(Boolean.FALSE);
                                usuarioNuevo.setVerificador(Boolean.FALSE);
                                usuarioNuevo.setValidador(Boolean.FALSE);
                                usuarioNuevo.setAdministrador(Boolean.FALSE);
                                switch (dato) {
                                    case "REGISTRADOR":
                                        usuarioNuevo.setRegistrador(Boolean.TRUE);
                                        break;
                                    case "VERIFICADOR":
                                        usuarioNuevo.setVerificador(Boolean.TRUE);
                                        break;
                                    case "VALIDADOR":
                                        usuarioNuevo.setValidador(Boolean.TRUE);
                                        break;
                                    case "ADMINISTRADOR":
                                        usuarioNuevo.setAdministrador(Boolean.TRUE);
                                        break;
                                }
                                break;
                        }
                    }
                    usuarioNuevo.setEstado("A");
                    Boolean flagUsuarioRepetido = Boolean.FALSE;
                    for (Usuario u : usuarioNuevoList) {
                        if (usuarioNuevo.getUsuario().equals(u.getUsuario())) {
                            flagUsuarioRepetido = Boolean.TRUE;
                            break;
                        }
                    }
                    if (!flagUsuarioRepetido) {
                        usuarioNuevoList.add(usuarioNuevo);
                    }
                }
            }
            Boolean errorUsuarios = Boolean.FALSE;
            String mensajeError = "";
            for (Usuario u : usuarioNuevoList) {

                if (usuarioServicio.getUsuarioByUsername(u.getUsuario()).getUsuarioId() != null) {
                    errorUsuarios = Boolean.TRUE;
                    mensajeError = "El usuario: " + u.getUsuario() + " ya se encuentra registrado, favor verificar su archivo de carga";
                    break;
                }
                if (u.getUsuario() == null || u.getUsuario().equals("")) {
                    errorUsuarios = Boolean.TRUE;
                    mensajeError = "Error:Favor verificar su archivo de carga. Usuario no definido.";
                    break;
                }
                if (u.getInstitucionId().getInstitucionId() == null
                        || u.getInstitucionId().getInstitucionId().toString().equals("")) {
                    errorUsuarios = Boolean.TRUE;
                    mensajeError = "Error: Favor verificar su archivo de carga. Institución no definida.";
                    break;
                }
                if (u.getEmail() == null || u.getEmail().equals("")) {
                    errorUsuarios = Boolean.TRUE;
                    mensajeError = "Error: Favor verificar su archivo de carga. Email no definido";
                    break;
                }
            }

            if (errorUsuarios) {
                this.addErrorMessage("0", mensajeError, "No funcionó");
            } else {
                for (Usuario u : usuarioNuevoList) {
                    usuarioServicio.createUsuario(u);
                }
                usuarioActivoList = new ArrayList<Usuario>();
                usuarioActivoList = usuarioServicio.getUsuariosActivos();
                this.addInfoMessage("Se ha creado el bloque de usuarios satisfactoriamente", "Info");
            }

        } catch (IOException ex) {
            Logger.getLogger(GestionUsuariosCtrl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            this.addErrorMessage("0", "Error: El Excel que se pretende subir tiene errores, favor verificar su archivo de carga", "No funcionó");
        }
    }

    private void correoRestablecerContraseña(String contraseña) {
        Email email = new Email();
        try {
            String mensajeMail = "Su Usuario es: <b>" + usuarioSelected.getUsuario() + "</b><br/>"
                    + "Su Contraseña es: <b>" + contraseña + "</b>";
            email.sendMail(usuarioSelected.getEmail(), "Plataforma REMANENTES", mensajeMail);
            System.out.println("Correo Enviado");
        } catch (Exception ex) {
            Logger.getLogger(RestaurarClaveCtrl.class.getName()).log(Level.SEVERE, null, ex);
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
        } else if (tipoInstitucion.equals("Dirección Nacional")) {
            tipoInstitucion = "Dirección Nacional";
            institucionRequeridaList = institucionRequeridaServicio.getDireccionNacionalList();
            usuarioSelected.setInstitucionId(institucionRequeridaList.get(institucionRequeridaList.size() - 1));
            usuarioSelected.setValidador(Boolean.FALSE);
            usuarioSelected.setAdministrador(Boolean.TRUE);
            usuarioSelected.setVerificador(Boolean.FALSE);
            usuarioSelected.setRegistrador(Boolean.FALSE);

            disabledRegistrador = Boolean.TRUE;
            disabledValidador = Boolean.TRUE;
            disabledVerificador = Boolean.TRUE;
            disabledAdministrador = Boolean.TRUE;
        }
    }

    public List<InstitucionRequerida> completeNombreInstitucion(String query) {
        List<InstitucionRequerida> filteredInstituciones = new ArrayList<InstitucionRequerida>();
        for (InstitucionRequerida ir : institucionRequeridaList) {
            if (ir.getNombre().toLowerCase().contains(query)
                    || ir.getNombre().toUpperCase().contains(query)) {
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

    public Boolean getDisabledRestablecer() {
        return disabledRestablecer;
    }

    public void setDisabledRestablecer(Boolean disabledRestablecer) {
        this.disabledRestablecer = disabledRestablecer;
    }

    public Boolean getRestablecer() {
        return restablecer;
    }

    public void setRestablecer(Boolean restablecer) {
        this.restablecer = restablecer;
    }
}

package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.autorizacion.constante.SemillaEnum;
import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.correo.mdb.cliente.ClienteQueueMailServicio;
import ec.gob.dinardap.correo.util.MailMessage;
import ec.gob.dinardap.remanente.constante.SistemaIdEnum;
import ec.gob.dinardap.remanente.constante.TipoInstitucionEnum;
import ec.gob.dinardap.remanente.dao.UsuarioDao;
import ec.gob.dinardap.remanente.dao.UsuarioPerfilDao;
import ec.gob.dinardap.remanente.dataModel.UsuarioDataModel;
import ec.gob.dinardap.remanente.dto.UsuarioDTO;
import ec.gob.dinardap.remanente.servicio.AsignacionInstitucionServicio;
import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import ec.gob.dinardap.remanente.servicio.UsuarioPerfilServicio;
import ec.gob.dinardap.remanente.servicio.impl.BandejaServicioImpl;
import ec.gob.dinardap.remanente.utils.FacesUtils;
import ec.gob.dinardap.seguridad.modelo.AsignacionInstitucion;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.Perfil;
import ec.gob.dinardap.seguridad.modelo.Pregunta;
import ec.gob.dinardap.seguridad.modelo.Respuesta;
import ec.gob.dinardap.seguridad.modelo.TipoInstitucion;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.modelo.UsuarioPerfil;

import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.PerfilServicio;
import ec.gob.dinardap.seguridad.servicio.PreguntaServicio;
import ec.gob.dinardap.seguridad.servicio.RespuestaServicio;
import ec.gob.dinardap.seguridad.servicio.TipoInstitucionServicio;

import ec.gob.dinardap.seguridad.servicio.UsuarioServicio;
import ec.gob.dinardap.util.constante.EstadoEnum;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named(value = "gestionUsuariosCtrl_Old")
@ViewScoped
public class GestionUsuariosCtrl_old extends BaseCtrl implements Serializable {

    //****Declaración de variables****//
    //Variables de control visual
    private Boolean onEdit;
    private Boolean onCreate;
    private Boolean renderEdition;
    private String tituloPagina;
    private String btnGuardar;

    private Boolean disableRestablecerContraseñaBtn;

    //Variables de Negocio
    private UsuarioDTO usuarioDtoSelected;
    private TipoInstitucion tipoInstitucionSelected;
    private Institucion institucionSelected;
//    private LazyUsuarioDataModel lazyModel;
    private UsuarioDataModel usuarioDataModel;

//    private Perfil perfilSelected;
    private Boolean restablecerContraseña;

    //Listas
    private List<TipoInstitucion> tipoInstitucionList;
    private List<Institucion> institucionList;
    private List<Perfil> perfilListActivos;
    private List<Pregunta> preguntaList;

    private List<Usuario> usuarioActivoList;
    private List<UsuarioDTO> usuarioDtoList;

    private List<Perfil> perfilSelectedList;

    

    @EJB
    private UsuarioDao usuarioDao;

    @EJB
    private UsuarioServicio usuarioServicio;

    @EJB
    private InstitucionServicio institucionServicio;

    @EJB
    private PreguntaServicio preguntaServicio;

    @EJB
    private RespuestaServicio respuestaServicio;

    @EJB
    private AsignacionInstitucionServicio asignacionInstitucionServicio;

    @EJB
    private UsuarioPerfilServicio usuarioPerfilServicio;

    @EJB
    private UsuarioPerfilDao usuarioPerfilDao;

    @EJB
    private TipoInstitucionServicio tipoInstitucionServicio;

    @EJB
    private PerfilServicio perfilServicio;

    @EJB
    private BandejaServicio bandejaServicio;

    @EJB
    private ClienteQueueMailServicio clienteQueueMailServicio;

    @PostConstruct
    protected void init() {
        restablecerVista();
        tituloPagina = "Gestión de Usuarios";

        tipoInstitucionList = new ArrayList<TipoInstitucion>();
        tipoInstitucionList = tipoInstitucionServicio.tipoInstitucionActivas();

        institucionList = new ArrayList<Institucion>();
        institucionList = institucionServicio.buscarInstitucionPorTipo(TipoInstitucionEnum.DINARDAP.getTipoInstitucion());

        perfilListActivos = new ArrayList<Perfil>();
        perfilListActivos = getPerfilesPorTipoInstitucion(TipoInstitucionEnum.DINARDAP.getTipoInstitucion());

        preguntaList = new ArrayList<Pregunta>();
        preguntaList = preguntaServicio.getPreguntasActivas();

        cargarDatosUsuario();

    }

    private void cargarDatosUsuario() {
        perfilSelectedList = new ArrayList<Perfil>();
        usuarioDtoList = new ArrayList<UsuarioDTO>();

        usuarioDtoSelected = new UsuarioDTO();

        usuarioActivoList = new ArrayList<Usuario>();
        usuarioActivoList = usuarioDao.getUsuarioActivos(SistemaIdEnum.REMANENTES_SISTEMA_ID.getSistemaId());

        for (Usuario usuario : usuarioActivoList) {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setUsuario(usuario);
            //Controlar si no se tiene ningún registro sobre asignación Institución
            usuarioDTO.setInstitucion(usuario.getAsignacionInstitucions().get(usuario.getAsignacionInstitucions().size() - 1).getInstitucion());
            List<String> strPerfilList = new ArrayList<String>();
            for (UsuarioPerfil up : usuario.getUsuarioPerfilList()) {
                strPerfilList.add(up.getPerfil().getNombre());
            }
            usuarioDTO.setPerfil(StringUtils.join(strPerfilList, " / "));
            usuarioDtoList.add(usuarioDTO);
        }
//        usuarioDataModel = new UsuarioDataModel(usuarioDtoList);

//        lazyModel= new LazyUsuarioDataModel(usuarioDtoList);
    }

    public void nuevoUsuario() {
        renderEdition = Boolean.TRUE;
        onCreate = Boolean.TRUE;
        onEdit = Boolean.FALSE;
        disableRestablecerContraseñaBtn = Boolean.TRUE;
        restablecerContraseña = Boolean.TRUE;

        usuarioDtoSelected = new UsuarioDTO();
        usuarioDtoSelected.setUsuario(new Usuario());
        usuarioDtoSelected.setInstitucion(new Institucion());
        tipoInstitucionSelected = new TipoInstitucion();
        institucionSelected = new Institucion();

        perfilListActivos = new ArrayList<Perfil>();
        perfilListActivos = getPerfilesPorTipoInstitucion(TipoInstitucionEnum.DINARDAP.getTipoInstitucion());

        btnGuardar = "Guardar";
    }

    public void onRowSelectUsuario() {
        renderEdition = Boolean.TRUE;
        onCreate = Boolean.FALSE;
        onEdit = Boolean.TRUE;
        disableRestablecerContraseñaBtn = Boolean.FALSE;
        restablecerContraseña = Boolean.FALSE;

        btnGuardar = "Actualizar";

        perfilSelectedList = new ArrayList<Perfil>();

        tipoInstitucionSelected = new TipoInstitucion();
        tipoInstitucionSelected = usuarioDtoSelected.getInstitucion().getTipoInstitucion();

        institucionList = institucionServicio.buscarInstitucionPorTipo(tipoInstitucionSelected.getTipoInstitucionId());

        institucionSelected = usuarioDtoSelected.getInstitucion();

        perfilListActivos = getPerfilesPorTipoInstitucion(tipoInstitucionSelected.getTipoInstitucionId());

        for (UsuarioPerfil up : usuarioDtoSelected.getUsuario().getUsuarioPerfilList()) {
            perfilSelectedList.add(up.getPerfil());
        }
    }

    public void onChangeTipoInstitucion() {
        institucionList = institucionServicio.buscarInstitucionPorTipo(tipoInstitucionSelected.getTipoInstitucionId());
        institucionSelected = new Institucion();

        perfilListActivos = getPerfilesPorTipoInstitucion(tipoInstitucionSelected.getTipoInstitucionId());
        perfilSelectedList = new ArrayList<Perfil>();
    }

    public void guardar() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente = usuarioServicio.obtenerUsuarioPorIdentificacion(usuarioDtoSelected.getUsuario().getCedula());
        //Definición de usuario
        usuarioDtoSelected.setInstitucion(institucionSelected);
        usuarioDtoSelected.getUsuario().setFechaCreacion(new Date());
        usuarioDtoSelected.getUsuario().setEstado(EstadoEnum.ACTIVO.getEstado());

        if (onCreate) {
            if (usuarioExistente == null) {
                //Creación de nueva contraseña
                String contraseña = FacesUtils.generarContraseña();
                usuarioDtoSelected.getUsuario().setContrasena(
                        EncriptarCadenas.encriptarCadenaSha1(
                                SemillaEnum.SEMILLA_REMANENTE.getSemilla() + contraseña));
                Usuario usuarioAux = new Usuario();
                usuarioAux = usuarioServicio.crearUsuario(usuarioDtoSelected.getUsuario());
                usuarioDtoSelected.setUsuario(usuarioAux);
                for (Pregunta p : preguntaList) {
                    Respuesta respuesta = new Respuesta();
                    respuesta.setPregunta(p);
                    respuesta.setUsuario(usuarioDtoSelected.getUsuario());
                    respuesta.setRespuesta("");
                    respuesta.setFechaCreacion(new Date());
                    respuesta.setEstado(EstadoEnum.ACTIVO.getEstado());
                    respuestaServicio.create(respuesta);
                }
                //Asignación Institución
                AsignacionInstitucion asignacionInstitucion = new AsignacionInstitucion();
                asignacionInstitucion.setInstitucion(usuarioDtoSelected.getInstitucion());
                asignacionInstitucion.setUsuario(usuarioDtoSelected.getUsuario());
                asignacionInstitucion.setFechaCreacion(new Date());
                asignacionInstitucion.setEstado(EstadoEnum.ACTIVO.getEstado());
                asignacionInstitucionServicio.create(asignacionInstitucion);
                //Usuario Perfil
                for (Perfil perfil : perfilSelectedList) {
                    UsuarioPerfil usuarioPerfil = new UsuarioPerfil();
                    usuarioPerfil.setUsuario(usuarioDtoSelected.getUsuario());
                    usuarioPerfil.setPerfil(perfil);
                    usuarioPerfil.setFechaAsignacion(new Date());
                    usuarioPerfil.setEstado(EstadoEnum.ACTIVO.getEstado());
                    usuarioPerfilServicio.create(usuarioPerfil);
                }
                correoRestablecerContraseña(contraseña, "Creación de Usuario");
                cargarDatosUsuario();
                restablecerVista();
            } else {
                this.addErrorMessage("1", "Error", "El usuario ingresado ya existe");
            }
        } else if (onEdit) {
            String contraseña = "";
            if (restablecerContraseña) {
                contraseña = FacesUtils.generarContraseña();
                usuarioDtoSelected.getUsuario().setContrasena(
                        EncriptarCadenas.encriptarCadenaSha1(
                                SemillaEnum.SEMILLA_REMANENTE.getSemilla() + contraseña));
            }
            usuarioDtoSelected.getUsuario().setFechaModificacion(new Date());
            usuarioServicio.update(usuarioDtoSelected.getUsuario());
            //Asignación Institución
            AsignacionInstitucion asignacionInstitucion = new AsignacionInstitucion();
            asignacionInstitucion = asignacionInstitucionServicio.getAsignacionPorUsuarioActivo(usuarioDtoSelected.getUsuario().getUsuarioId());
            if (!usuarioDtoSelected.getInstitucion().getInstitucionId().equals(asignacionInstitucion.getInstitucion().getInstitucionId())) {
                asignacionInstitucion.setEstado(EstadoEnum.INACTIVO.getEstado());
                asignacionInstitucion.setFechaModificacion(new Date());
                asignacionInstitucionServicio.update(asignacionInstitucion);
                AsignacionInstitucion asignacionInstitucion1 = new AsignacionInstitucion();
                asignacionInstitucion1 = asignacionInstitucionServicio.getAsignacionPorUsuarioInactivo(usuarioDtoSelected.getUsuario().getUsuarioId(), usuarioDtoSelected.getInstitucion().getInstitucionId());
                if (asignacionInstitucion1.getInstitucion() != null) {
                    asignacionInstitucion1.setEstado(EstadoEnum.ACTIVO.getEstado());
                    asignacionInstitucion1.setFechaModificacion(new Date());
                    asignacionInstitucionServicio.update(asignacionInstitucion1);
                } else {
                    AsignacionInstitucion asignacionInstitucion2 = new AsignacionInstitucion();
                    asignacionInstitucion2.setInstitucion(usuarioDtoSelected.getInstitucion());
                    asignacionInstitucion2.setUsuario(usuarioDtoSelected.getUsuario());
                    asignacionInstitucion2.setFechaCreacion(new Date());
                    asignacionInstitucion2.setEstado(EstadoEnum.ACTIVO.getEstado());
                    asignacionInstitucionServicio.create(asignacionInstitucion2);
                }
            }
            //Usuario Perfil
            List<UsuarioPerfil> usuarioPerfilList = new ArrayList<UsuarioPerfil>();
            usuarioPerfilList = usuarioPerfilDao.getUsuarioPerfilListPorUsuarioActivo(usuarioDtoSelected.getUsuario().getUsuarioId());//traido desde base
            for (UsuarioPerfil up : usuarioPerfilList) {
                if (!perfilSelectedList.contains(up.getPerfil())) {
                    up.setEstado(EstadoEnum.INACTIVO.getEstado());
                    up.setFechaModificacion(new Date());
                    usuarioPerfilServicio.update(up);
                }
            }
            for (Perfil p : perfilSelectedList) {
                Boolean flagExist = Boolean.FALSE;
                for (UsuarioPerfil up : usuarioPerfilList) {
                    if (p.equals(up.getPerfil())) {
                        flagExist = Boolean.TRUE;
                        break;
                    }
                }
                if (!flagExist) {
                    List<UsuarioPerfil> usuarioPerfil1List = new ArrayList<UsuarioPerfil>();
                    usuarioPerfil1List = usuarioPerfilDao.getUsuarioPerfilListPorUsuarioInactivo(usuarioDtoSelected.getUsuario().getUsuarioId());//traido desde base
                    Boolean flagExistInactivo = Boolean.FALSE;
                    for (UsuarioPerfil up : usuarioPerfil1List) {
                        if (p.equals(up.getPerfil())) {
                            up.setEstado(EstadoEnum.ACTIVO.getEstado());
                            up.setFechaModificacion(new Date());
                            usuarioPerfilServicio.update(up);
                            flagExistInactivo = Boolean.TRUE;
                            break;
                        }
                    }
                    if (!flagExistInactivo) {
                        UsuarioPerfil usuarioPerfil = new UsuarioPerfil();
                        usuarioPerfil.setUsuario(usuarioDtoSelected.getUsuario());
                        usuarioPerfil.setPerfil(p);
                        usuarioPerfil.setFechaAsignacion(new Date());
                        usuarioPerfil.setEstado(EstadoEnum.ACTIVO.getEstado());
                        usuarioPerfilServicio.create(usuarioPerfil);
                    }
                }
            }
            List<String> strPerfilList = new ArrayList<String>();
            for (Perfil perfil : perfilSelectedList) {
                strPerfilList.add(perfil.getNombre());
            }
            usuarioDtoSelected.setPerfil(StringUtils.join(strPerfilList, " / "));
            if (restablecerContraseña) {
                correoRestablecerContraseña(contraseña, "Restaurar Contraseña");
            }
            cargarDatosUsuario();
            restablecerVista();
        }
    }

    public void eliminarUsuario() throws IOException {
        usuarioDtoSelected.getUsuario().setFechaModificacion(new Date());
        usuarioDtoSelected.getUsuario().setEstado(EstadoEnum.INACTIVO.getEstado());
        usuarioServicio.update(usuarioDtoSelected.getUsuario());
        FacesContext.getCurrentInstance().getExternalContext().redirect("gestionUsuarios.jsf");
//        cargarDatosUsuario();
//        restablecerVista();
    }

    private void restablecerVista() {
        onCreate = Boolean.FALSE;
        onEdit = Boolean.FALSE;
        renderEdition = Boolean.FALSE;
        disableRestablecerContraseñaBtn = Boolean.TRUE;
        restablecerContraseña = Boolean.FALSE;
    }

    public void cancelar() {
        usuarioDtoSelected = new UsuarioDTO();
        perfilSelectedList = new ArrayList<Perfil>();

        restablecerVista();
    }

    private List<Perfil> getPerfilesPorTipoInstitucion(Integer tipoInstitucion) {
        List<Perfil> perfilListAux = new ArrayList<Perfil>();
        List<Perfil> perfilList = new ArrayList<Perfil>();
        perfilListAux = perfilServicio.obtenerPerfilesPorSistema(SistemaIdEnum.REMANENTES_SISTEMA_ID.getSistemaId());
        switch (tipoInstitucion) {
            case 1:
                perfilList.add(perfilListAux.get(0));
                perfilList.add(perfilListAux.get(4));
                perfilList.add(perfilListAux.get(6));
                break;
            case 2:
                perfilList.add(perfilListAux.get(3));
                perfilList.add(perfilListAux.get(7));
                break;
            case 3:
                perfilList.add(perfilListAux.get(8));
                break;
            case 4:
                perfilList.add(perfilListAux.get(1));
                perfilList.add(perfilListAux.get(2));
                perfilList.add(perfilListAux.get(5));
                perfilList.add(perfilListAux.get(8));
                break;
            case 5:
                perfilList.add(perfilListAux.get(1));
                perfilList.add(perfilListAux.get(2));
                perfilList.add(perfilListAux.get(5));
                break;
            case 6:
                perfilList.add(perfilListAux.get(1));
                break;
            case 7:
                perfilList.add(perfilListAux.get(2));
                break;

        }
        return perfilList;
    }

    //aqui    
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
                                Institucion ir = new Institucion();
                                if (dato == null) {
                                    ir.setInstitucionId(null);
                                } else {
                                    ir.setInstitucionId(Integer.parseInt(dato));
                                }
//                                usuarioNuevo.setInstitucionId(ir);
                                break;
                            case 1:
                                usuarioNuevo.setNombre(dato);
                                break;
                            case 2:
                                usuarioNuevo.setCorreoElectronico(dato);
                                break;
                            case 3:
                                usuarioNuevo.setCedula(dato);
                                usuarioNuevo.setContrasena(EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_REMANENTE.getSemilla() + "R" + dato + "R"));
                                break;
                            case 4:
//                                usuarioNuevo.setRegistrador(Boolean.FALSE);
//                                usuarioNuevo.setVerificador(Boolean.FALSE);
//                                usuarioNuevo.setValidador(Boolean.FALSE);
//                                usuarioNuevo.setAdministrador(Boolean.FALSE);
                                switch (dato) {
                                    case "REGISTRADOR":
//                                        usuarioNuevo.setRegistrador(Boolean.TRUE);
                                        break;
                                    case "VERIFICADOR":
//                                        usuarioNuevo.setVerificador(Boolean.TRUE);
                                        break;
                                    case "VALIDADOR":
//                                        usuarioNuevo.setValidador(Boolean.TRUE);
                                        break;
                                    case "ADMINISTRADOR":
//                                        usuarioNuevo.setAdministrador(Boolean.TRUE);
                                        break;
                                }
                                break;
                        }
                    }
                    usuarioNuevo.setEstado((short) 1);
//                    usuarioNuevo.setSuperAdministrador(Boolean.FALSE);
                    Boolean flagUsuarioRepetido = Boolean.FALSE;
                    for (Usuario u : usuarioNuevoList) {
                        if (usuarioNuevo.getCedula().equals(u.getCedula())) {
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

                if (usuarioServicio.obtenerUsuarioPorIdentificacion(u.getCedula()).getUsuarioId() != null) {
                    errorUsuarios = Boolean.TRUE;
                    mensajeError = "El usuario: " + u.getCedula() + " ya se encuentra registrado, favor verificar su archivo de carga";
                    break;
                }
                if (u.getCedula() == null || u.getCedula().equals("")) {
                    errorUsuarios = Boolean.TRUE;
                    mensajeError = "Error:Favor verificar su archivo de carga. Usuario no definido.";
                    break;
                }
//                if (u.getInstitucionId().getInstitucionId() == null
//                        || u.getInstitucionId().getInstitucionId().toString().equals("")) {
//                    errorUsuarios = Boolean.TRUE;
//                    mensajeError = "Error: Favor verificar su archivo de carga. Institución no definida.";
//                    break;
//                }
                if (u.getCorreoElectronico() == null || u.getCorreoElectronico().equals("")) {
                    errorUsuarios = Boolean.TRUE;
                    mensajeError = "Error: Favor verificar su archivo de carga. Email no definido";
                    break;
                }
            }

            if (errorUsuarios) {
                this.addErrorMessage("0", "Error", mensajeError);
            } else {
                for (Usuario u : usuarioNuevoList) {
//                    usuarioServicio.createUsuario(u);
                }
                usuarioActivoList = new ArrayList<Usuario>();
//                usuarioActivoList = usuarioDao.obtenerUsuariosActivosSistema(SistemaIdEnum.REMANENTES_SISTEMA_ID.getSistemaId());
                this.addInfoMessage("Información", "Se ha creado el bloque de usuarios satisfactoriamente");
            }

        } catch (IOException ex) {
            Logger.getLogger(GestionUsuariosCtrl_old.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            this.addErrorMessage("0", "Error", "El Excel que se pretende subir tiene errores, favor verificar su archivo de carga");
        }
    }

    private void correoRestablecerContraseña(String contraseña, String asuntoUser) {
        MailMessage mailMessage = new MailMessage();
        String mensajeMail = "Su Usuario es: <b>" + usuarioDtoSelected.getUsuario().getCedula() + "</b><br/>"
                + "Su Contraseña es: <b>" + contraseña + "</b>";
        try {
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            URI uri = new URI(ext.getRequestScheme(),
                    null, ext.getRequestServerName(), ext.getRequestServerPort(),
                    ext.getRequestContextPath(), null, null);

            StringBuilder html = new StringBuilder(
                    "<FONT FACE=\"Arial, sans-serif\"><center><h1><B>Sistema de Remanentes e Inventario de Libros</B></h1></center><br/><br/>");
            html.append("Estimado(a) " + usuarioDtoSelected.getUsuario().getNombre() + ", <br /><br />");
            html.append(mensajeMail + "<br/ ><br />");
            html.append("<a href='" + uri.toASCIIString() + "'>Sistema de Remanentes e Inventario de Libros</a><br/ >");
            html.append("Gracias por usar nuestros servicios.<br /><br /></FONT>");
            html.append("<FONT FACE=\"Arial Narrow, sans-serif\"><B> ");
            html.append("Dirección Nacional de Registros de Datos Públicos");
            html.append("</B></FONT>");
            List<String> to = new ArrayList<String>();
            StringBuilder asunto = new StringBuilder(200);
            to.add(usuarioDtoSelected.getUsuario().getCorreoElectronico());
            asunto.append("Sistema de Remanentes e Inventario de Libros - " + asuntoUser);
            mailMessage = bandejaServicio.credencialesCorreo();            
            mailMessage.setTo(to);
            mailMessage.setSubject(asunto.toString());
            mailMessage.setText(html.toString());
            clienteQueueMailServicio.encolarMail(mailMessage);
        } catch (URISyntaxException ex) {
            Logger.getLogger(BandejaServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<Institucion> completeNombreInstitucion(String query) {
        List<Institucion> filteredInstituciones = new ArrayList<Institucion>();
        for (Institucion ir : institucionList) {
            if (ir.getNombre().toLowerCase().contains(query)
                    || ir.getNombre().toUpperCase().contains(query)) {
                filteredInstituciones.add(ir);
            }
        }
        return filteredInstituciones;
    }

    //Getters & Setters
    public Institucion getInstitucionSelected() {
        return institucionSelected;
    }

    public void setInstitucionSelected(Institucion institucionSelected) {
        this.institucionSelected = institucionSelected;
    }

    public Boolean getDisableRestablecerContraseñaBtn() {
        return disableRestablecerContraseñaBtn;
    }

    public void setDisableRestablecerContraseñaBtn(Boolean disableRestablecerContraseñaBtn) {
        this.disableRestablecerContraseñaBtn = disableRestablecerContraseñaBtn;
    }

    public Boolean getRestablecerContraseña() {
        return restablecerContraseña;
    }

    public void setRestablecerContraseña(Boolean restablecerContraseña) {
        this.restablecerContraseña = restablecerContraseña;
    }

    public List<Perfil> getPerfilListActivos() {
        return perfilListActivos;
    }

    public void setPerfilListActivos(List<Perfil> perfilListActivos) {
        this.perfilListActivos = perfilListActivos;
    }

    public List<Perfil> getPerfilSelectedList() {
        return perfilSelectedList;
    }

    public void setPerfilSelectedList(List<Perfil> perfilSelectedList) {
        this.perfilSelectedList = perfilSelectedList;
    }

    public List<TipoInstitucion> getTipoInstitucionList() {
        return tipoInstitucionList;
    }

    public void setTipoInstitucionList(List<TipoInstitucion> tipoInstitucionList) {
        this.tipoInstitucionList = tipoInstitucionList;
    }

    public TipoInstitucion getTipoInstitucionSelected() {
        return tipoInstitucionSelected;
    }

    public void setTipoInstitucionSelected(TipoInstitucion tipoInstitucionSelected) {
        this.tipoInstitucionSelected = tipoInstitucionSelected;
    }

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

    public UsuarioDTO getUsuarioDtoSelected() {
        return usuarioDtoSelected;
    }

    public void setUsuarioDtoSelected(UsuarioDTO usuarioDtoSelected) {
        this.usuarioDtoSelected = usuarioDtoSelected;
    }

    public String getBtnGuardar() {
        return btnGuardar;
    }

    public void setBtnGuardar(String btnGuardar) {
        this.btnGuardar = btnGuardar;
    }

    public List<Institucion> getInstitucionList() {
        return institucionList;
    }

    public void setInstitucionList(List<Institucion> institucionList) {
        this.institucionList = institucionList;
    }

    public Boolean getOnCreate() {
        return onCreate;
    }

    public List<UsuarioDTO> getUsuarioDtoList() {
        return usuarioDtoList;
    }

    public void setUsuarioDtoList(List<UsuarioDTO> usuarioDtoList) {
        this.usuarioDtoList = usuarioDtoList;
    }

    public Boolean getOnEdit() {
        return onEdit;
    }

    public void setOnEdit(Boolean onEdit) {
        this.onEdit = onEdit;
    }

//    public LazyUsuarioDataModel getLazyModel() {
//        return lazyModel;
//    }
//
//    public void setLazyModel(LazyUsuarioDataModel lazyModel) {
//        this.lazyModel = lazyModel;
//    }
    public UsuarioDataModel getUsuarioDataModel() {
        return usuarioDataModel;
    }

    public void setUsuarioDataModel(UsuarioDataModel usuarioDataModel) {
        this.usuarioDataModel = usuarioDataModel;
    }
}

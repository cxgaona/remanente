package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.autorizacion.constante.SemillaEnum;
import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.correo.mdb.cliente.ClienteQueueMailServicio;
import ec.gob.dinardap.remanente.constante.SistemaIdEnum;
import ec.gob.dinardap.remanente.constante.TipoInstitucionEnum;
import ec.gob.dinardap.remanente.dao.UsuarioDao;
import ec.gob.dinardap.remanente.dao.UsuarioPerfilDao;
import ec.gob.dinardap.remanente.dto.UsuarioDTO;
import ec.gob.dinardap.remanente.servicio.AsignacionInstitucionServicio;
import ec.gob.dinardap.remanente.servicio.BandejaServicio;
import ec.gob.dinardap.remanente.servicio.UsuarioPerfilServicio;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;

@Named(value = "gestionUsuariosCtrl")
@ViewScoped
public class GestionUsuariosCtrl extends BaseCtrl implements Serializable {

    //****Declaración de variables****//
    //Variables de control visual    
    private Boolean renderEdition;
    private Boolean onEdit;
    private Boolean onCreate;
    private String btnGuardar;

    private String tituloPagina;
    private Boolean disableRestablecerContraseñaBtn;

    //Variables de Negocio
    private UsuarioDTO usuarioDtoGestion;

    private TipoInstitucion tipoInstitucionSelected;
    private Institucion institucionSelected;
    private Boolean restablecerContraseña;
    private String cedulaBusqueda;

    //Listas
    private List<UsuarioDTO> usuarioDtoList;

    private List<TipoInstitucion> tipoInstitucionList;
    private List<Institucion> institucionList;
    private List<Perfil> perfilListActivos;

    private List<Pregunta> preguntaList;
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
        tituloPagina = "Listado de Usuarios Activos";
        renderEdition = Boolean.FALSE;

        //Carga del tipo de instituciones activas para seleccionar
        tipoInstitucionList = new ArrayList<TipoInstitucion>();
        tipoInstitucionList = tipoInstitucionServicio.tipoInstitucionActivas();

        //Carga de las instituciones activas de acuerdo al tipo (tipo DINARDAP)
        institucionList = new ArrayList<Institucion>();
        institucionList = institucionServicio.buscarInstitucionPorTipo(TipoInstitucionEnum.DINARDAP.getTipoInstitucion());

        //Carga de los perfiles de acuerdo al tipo de institición (tipo DINARDAP)
        perfilListActivos = new ArrayList<Perfil>();
        perfilListActivos = getPerfilesPorTipoInstitucion(TipoInstitucionEnum.DINARDAP.getTipoInstitucion());

        //Carga de todas las preguntas
        preguntaList = new ArrayList<Pregunta>();
        preguntaList = preguntaServicio.getPreguntasActivas();

        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        List<Usuario> usuarioActivoList = new ArrayList<Usuario>();
        usuarioActivoList = usuarioDao.getUsuarioActivos(SistemaIdEnum.REMANENTES_SISTEMA_ID.getSistemaId());

        usuarioDtoList = new ArrayList<UsuarioDTO>();

        for (Usuario usuario : usuarioActivoList) {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setUsuario(usuario);
            
            if (!usuario.getAsignacionInstitucions().isEmpty()) {
                usuarioDTO.setInstitucion(usuario.getAsignacionInstitucions().get(usuario.getAsignacionInstitucions().size() - 1).getInstitucion());
            } else {
                usuarioDTO.setInstitucion(null);
            }
            
            List<String> strPerfilList = new ArrayList<String>();
            for (UsuarioPerfil up : usuario.getUsuarioPerfilList()) {
                strPerfilList.add(up.getPerfil().getNombre());
            }
            usuarioDTO.setPerfil(StringUtils.join(strPerfilList, " / "));
            usuarioDtoList.add(usuarioDTO);
        }
    }

    public void actualizar() {
        cargarDatosUsuario();
    }

    public void onRowSelectUsuario() {
//        renderEdition = Boolean.TRUE;
//        onCreate = Boolean.FALSE;
//        onEdit = Boolean.TRUE;
//        disableRestablecerContraseñaBtn = Boolean.FALSE;
//        restablecerContraseña = Boolean.FALSE;
//
//        btnGuardar = "Actualizar";
//
//        perfilSelectedList = new ArrayList<Perfil>();
//
//        tipoInstitucionSelected = new TipoInstitucion();
//        tipoInstitucionSelected = usuarioDtoSelected.getInstitucion().getTipoInstitucion();
//
//        institucionList = institucionServicio.buscarInstitucionPorTipo(tipoInstitucionSelected.getTipoInstitucionId());
//
//        institucionSelected = usuarioDtoSelected.getInstitucion();
//
//        perfilListActivos = getPerfilesPorTipoInstitucion(tipoInstitucionSelected.getTipoInstitucionId());
//
//        for (UsuarioPerfil up : usuarioDtoSelected.getUsuario().getUsuarioPerfilList()) {
//            perfilSelectedList.add(up.getPerfil());
//        }
    }

    // Nueva manera de manejar la gestión de usuarios
    public void buscarUsuario() {
        renderEdition = Boolean.FALSE;
        
        restablecerVista();       

        tipoInstitucionSelected = new TipoInstitucion();
        institucionSelected = new Institucion();
        perfilSelectedList = new ArrayList<Perfil>();
        usuarioDtoGestion = new UsuarioDTO();

        usuarioDtoGestion = usuarioDao.getUsuarioByCedula(cedulaBusqueda);
        if (usuarioDtoGestion.getUsuario() != null) {
            renderEdition = Boolean.TRUE;
            onEdit = Boolean.TRUE;
            disableRestablecerContraseñaBtn = Boolean.FALSE;
            restablecerContraseña = Boolean.FALSE;
            btnGuardar = "Actualizar";

            tipoInstitucionSelected = usuarioDtoGestion.getInstitucion().getTipoInstitucion();
            institucionSelected = usuarioDtoGestion.getInstitucion();
            
            institucionList = institucionServicio.buscarInstitucionPorTipo(tipoInstitucionSelected.getTipoInstitucionId());
            perfilListActivos = getPerfilesPorTipoInstitucion(tipoInstitucionSelected.getTipoInstitucionId());

            for (UsuarioPerfil up : usuarioDtoGestion.getUsuario().getUsuarioPerfilList()) {
                perfilSelectedList.add(up.getPerfil());
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "No se encontró el usuario con la Cédula: " + cedulaBusqueda));
        }
    }

    public void nuevoUsuario() {
        renderEdition = Boolean.TRUE;
        onCreate = Boolean.TRUE;
        onEdit = Boolean.FALSE;
        disableRestablecerContraseñaBtn = Boolean.TRUE;
        restablecerContraseña = Boolean.TRUE;

        usuarioDtoGestion = new UsuarioDTO();
        usuarioDtoGestion.setUsuario(new Usuario());
        usuarioDtoGestion.setInstitucion(new Institucion());
        tipoInstitucionSelected = new TipoInstitucion();
        institucionSelected = new Institucion();

        perfilListActivos = new ArrayList<Perfil>();
        perfilListActivos = getPerfilesPorTipoInstitucion(TipoInstitucionEnum.DINARDAP.getTipoInstitucion());

        btnGuardar = "Guardar";
    }

    public void eliminarUsuario() throws IOException {
        usuarioDtoGestion.getUsuario().setFechaModificacion(new Date());
        usuarioDtoGestion.getUsuario().setEstado(EstadoEnum.INACTIVO.getEstado());
        usuarioServicio.update(usuarioDtoGestion.getUsuario());
        cargarDatosUsuario();
        PrimeFaces.current().ajax().update("formUsuario");
        restablecerVista();
    }

    public void guardar() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente = usuarioServicio.obtenerUsuarioPorIdentificacion(usuarioDtoGestion.getUsuario().getCedula());
        //Definición de usuario
        usuarioDtoGestion.setInstitucion(institucionSelected);
        usuarioDtoGestion.getUsuario().setFechaCreacion(new Date());
        usuarioDtoGestion.getUsuario().setEstado(EstadoEnum.ACTIVO.getEstado());

        if (onCreate) {
            if (usuarioExistente == null) {
                //Creación de nueva contraseña
                String contraseña = FacesUtils.generarContraseña();
                usuarioDtoGestion.getUsuario().setContrasena(
                        EncriptarCadenas.encriptarCadenaSha1(
                                SemillaEnum.SEMILLA_REMANENTE.getSemilla() + contraseña));
                Usuario usuarioAux = new Usuario();
                usuarioAux = usuarioServicio.crearUsuario(usuarioDtoGestion.getUsuario());
                usuarioDtoGestion.setUsuario(usuarioAux);
                for (Pregunta p : preguntaList) {
                    Respuesta respuesta = new Respuesta();
                    respuesta.setPregunta(p);
                    respuesta.setUsuario(usuarioDtoGestion.getUsuario());
                    respuesta.setRespuesta("");
                    respuesta.setFechaCreacion(new Date());
                    respuesta.setEstado(EstadoEnum.ACTIVO.getEstado());
                    respuestaServicio.create(respuesta);
                }
                //Asignación Institución
                AsignacionInstitucion asignacionInstitucion = new AsignacionInstitucion();
                asignacionInstitucion.setInstitucion(usuarioDtoGestion.getInstitucion());
                asignacionInstitucion.setUsuario(usuarioDtoGestion.getUsuario());
                asignacionInstitucion.setFechaCreacion(new Date());
                asignacionInstitucion.setEstado(EstadoEnum.ACTIVO.getEstado());
                asignacionInstitucionServicio.create(asignacionInstitucion);
                //Usuario Perfil
                for (Perfil perfil : perfilSelectedList) {
                    UsuarioPerfil usuarioPerfil = new UsuarioPerfil();
                    usuarioPerfil.setUsuario(usuarioDtoGestion.getUsuario());
                    usuarioPerfil.setPerfil(perfil);
                    usuarioPerfil.setFechaAsignacion(new Date());
                    usuarioPerfil.setEstado(EstadoEnum.ACTIVO.getEstado());
                    usuarioPerfilServicio.create(usuarioPerfil);
                }
                correoRestablecerContraseña(contraseña, "Creación de Usuario");
                cargarDatosUsuario();
                PrimeFaces.current().ajax().update("formUsuario");
                restablecerVista();
            } else {
                this.addErrorMessage("1", "Error", "El usuario ingresado ya existe");
            }
        } else if (onEdit) {
            String contraseña = "";
            if (restablecerContraseña) {
                contraseña = FacesUtils.generarContraseña();
                usuarioDtoGestion.getUsuario().setContrasena(
                        EncriptarCadenas.encriptarCadenaSha1(
                                SemillaEnum.SEMILLA_REMANENTE.getSemilla() + contraseña));
            }
            usuarioDtoGestion.getUsuario().setFechaModificacion(new Date());
            usuarioServicio.update(usuarioDtoGestion.getUsuario());
            //Asignación Institución
            AsignacionInstitucion asignacionInstitucion = new AsignacionInstitucion();
            asignacionInstitucion = asignacionInstitucionServicio.getAsignacionPorUsuarioActivo(usuarioDtoGestion.getUsuario().getUsuarioId());
            if (!usuarioDtoGestion.getInstitucion().getInstitucionId().equals(asignacionInstitucion.getInstitucion().getInstitucionId())) {
                asignacionInstitucion.setEstado(EstadoEnum.INACTIVO.getEstado());
                asignacionInstitucion.setFechaModificacion(new Date());
                asignacionInstitucionServicio.update(asignacionInstitucion);
                AsignacionInstitucion asignacionInstitucion1 = new AsignacionInstitucion();
                asignacionInstitucion1 = asignacionInstitucionServicio.getAsignacionPorUsuarioInactivo(usuarioDtoGestion.getUsuario().getUsuarioId(), usuarioDtoGestion.getInstitucion().getInstitucionId());
                if (asignacionInstitucion1.getInstitucion() != null) {
                    asignacionInstitucion1.setEstado(EstadoEnum.ACTIVO.getEstado());
                    asignacionInstitucion1.setFechaModificacion(new Date());
                    asignacionInstitucionServicio.update(asignacionInstitucion1);
                } else {
                    AsignacionInstitucion asignacionInstitucion2 = new AsignacionInstitucion();
                    asignacionInstitucion2.setInstitucion(usuarioDtoGestion.getInstitucion());
                    asignacionInstitucion2.setUsuario(usuarioDtoGestion.getUsuario());
                    asignacionInstitucion2.setFechaCreacion(new Date());
                    asignacionInstitucion2.setEstado(EstadoEnum.ACTIVO.getEstado());
                    asignacionInstitucionServicio.create(asignacionInstitucion2);
                }
            }
            //Usuario Perfil
            List<UsuarioPerfil> usuarioPerfilList = new ArrayList<UsuarioPerfil>();
            usuarioPerfilList = usuarioPerfilDao.getUsuarioPerfilListPorUsuarioActivo(usuarioDtoGestion.getUsuario().getUsuarioId());//traido desde base
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
                    usuarioPerfil1List = usuarioPerfilDao.getUsuarioPerfilListPorUsuarioInactivo(usuarioDtoGestion.getUsuario().getUsuarioId());//traido desde base
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
                        usuarioPerfil.setUsuario(usuarioDtoGestion.getUsuario());
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
            usuarioDtoGestion.setPerfil(StringUtils.join(strPerfilList, " / "));
            if (restablecerContraseña) {
                correoRestablecerContraseña(contraseña, "Restaurar Contraseña");
            }
            cargarDatosUsuario();
            restablecerVista();
        }
    }

    public void cancelar() {
        usuarioDtoGestion = new UsuarioDTO();
        perfilSelectedList = new ArrayList<Perfil>();

        restablecerVista();
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

    public void onChangeTipoInstitucion() {
        institucionList = institucionServicio.buscarInstitucionPorTipo(tipoInstitucionSelected.getTipoInstitucionId());
        institucionSelected = new Institucion();

        perfilListActivos = getPerfilesPorTipoInstitucion(tipoInstitucionSelected.getTipoInstitucionId());
        perfilSelectedList = new ArrayList<Perfil>();
    }

    //Metodos internos
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

    private void restablecerVista() {
        onCreate = Boolean.FALSE;
        onEdit = Boolean.FALSE;
        renderEdition = Boolean.FALSE;
        disableRestablecerContraseñaBtn = Boolean.TRUE;
        restablecerContraseña = Boolean.FALSE;
    }

    private void correoRestablecerContraseña(String contraseña, String asuntoUser) {
        System.out.println("se esta enviando el correo XD");
//        MailMessage mailMessage = new MailMessage();
//        String mensajeMail = "Su Usuario es: <b>" + usuarioDtoGestion.getUsuario().getCedula() + "</b><br/>"
//                + "Su Contraseña es: <b>" + contraseña + "</b>";
//        try {
//            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
//            URI uri = new URI(ext.getRequestScheme(),
//                    null, ext.getRequestServerName(), ext.getRequestServerPort(),
//                    ext.getRequestContextPath(), null, null);
//
//            StringBuilder html = new StringBuilder(
//                    "<FONT FACE=\"Arial, sans-serif\"><center><h1><B>Sistema de Remanentes e Inventario de Libros</B></h1></center><br/><br/>");
//            html.append("Estimado(a) " + usuarioDtoGestion.getUsuario().getNombre() + ", <br /><br />");
//            html.append(mensajeMail + "<br/ ><br />");
//            html.append("<a href='" + uri.toASCIIString() + "'>Sistema de Remanentes e Inventario de Libros</a><br/ >");
//            html.append("Gracias por usar nuestros servicios.<br /><br /></FONT>");
//            html.append("<FONT FACE=\"Arial Narrow, sans-serif\"><B> ");
//            html.append("Dirección Nacional de Registros de Datos Públicos");
//            html.append("</B></FONT>");
//            List<String> to = new ArrayList<String>();
//            StringBuilder asunto = new StringBuilder(200);
//            to.add(usuarioDtoGestion.getUsuario().getCorreoElectronico());
//            asunto.append("Sistema de Remanentes e Inventario de Libros - " + asuntoUser);
//            mailMessage = bandejaServicio.credencialesCorreo();            
//            mailMessage.setTo(to);
//            mailMessage.setSubject(asunto.toString());
//            mailMessage.setText(html.toString());
//            clienteQueueMailServicio.encolarMail(mailMessage);
//        } catch (URISyntaxException ex) {
//            Logger.getLogger(BandejaServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

    //Getters & Setters
    public List<UsuarioDTO> getUsuarioDtoList() {
        return usuarioDtoList;
    }

    public void setUsuarioDtoList(List<UsuarioDTO> usuarioDtoList) {
        this.usuarioDtoList = usuarioDtoList;
    }

    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public String getCedulaBusqueda() {
        return cedulaBusqueda;
    }

    public void setCedulaBusqueda(String cedulaBusqueda) {
        this.cedulaBusqueda = cedulaBusqueda;
    }

    public Boolean getOnEdit() {
        return onEdit;
    }

    public void setOnEdit(Boolean onEdit) {
        this.onEdit = onEdit;
    }

    public Boolean getOnCreate() {
        return onCreate;
    }

    public void setOnCreate(Boolean onCreate) {
        this.onCreate = onCreate;
    }

    public Boolean getRenderEdition() {
        return renderEdition;
    }

    public void setRenderEdition(Boolean renderEdition) {
        this.renderEdition = renderEdition;
    }

    public Boolean getRestablecerContraseña() {
        return restablecerContraseña;
    }

    public void setRestablecerContraseña(Boolean restablecerContraseña) {
        this.restablecerContraseña = restablecerContraseña;
    }

    public String getBtnGuardar() {
        return btnGuardar;
    }

    public void setBtnGuardar(String btnGuardar) {
        this.btnGuardar = btnGuardar;
    }

    public Boolean getDisableRestablecerContraseñaBtn() {
        return disableRestablecerContraseñaBtn;
    }

    public void setDisableRestablecerContraseñaBtn(Boolean disableRestablecerContraseñaBtn) {
        this.disableRestablecerContraseñaBtn = disableRestablecerContraseñaBtn;
    }

    public TipoInstitucion getTipoInstitucionSelected() {
        return tipoInstitucionSelected;
    }

    public void setTipoInstitucionSelected(TipoInstitucion tipoInstitucionSelected) {
        this.tipoInstitucionSelected = tipoInstitucionSelected;
    }

    public List<TipoInstitucion> getTipoInstitucionList() {
        return tipoInstitucionList;
    }

    public void setTipoInstitucionList(List<TipoInstitucion> tipoInstitucionList) {
        this.tipoInstitucionList = tipoInstitucionList;
    }

    public Institucion getInstitucionSelected() {
        return institucionSelected;
    }

    public void setInstitucionSelected(Institucion institucionSelected) {
        this.institucionSelected = institucionSelected;
    }

    public UsuarioDTO getUsuarioDtoGestion() {
        return usuarioDtoGestion;
    }

    public void setUsuarioDtoGestion(UsuarioDTO usuarioDtoGestion) {
        this.usuarioDtoGestion = usuarioDtoGestion;
    }

    public List<Perfil> getPerfilSelectedList() {
        return perfilSelectedList;
    }

    public void setPerfilSelectedList(List<Perfil> perfilSelectedList) {
        this.perfilSelectedList = perfilSelectedList;
    }

    public List<Perfil> getPerfilListActivos() {
        return perfilListActivos;
    }

    public void setPerfilListActivos(List<Perfil> perfilListActivos) {
        this.perfilListActivos = perfilListActivos;
    }

}

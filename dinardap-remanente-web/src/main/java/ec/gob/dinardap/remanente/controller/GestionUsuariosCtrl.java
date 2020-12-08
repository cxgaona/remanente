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

@Named(value = "gestionUsuariosCtrl")
@ViewScoped
public class GestionUsuariosCtrl extends BaseCtrl implements Serializable {

    //****Declaración de variables****//
    //Variables de control visual    
    private String tituloPagina;
    //Variables de Negocio
    private UsuarioDTO usuarioDtoSelected;
    private TipoInstitucion tipoInstitucionSelected;
    private Institucion institucionSelected;
    
    //Listas
    private List<Usuario> usuarioActivoList;
    private List<UsuarioDTO> usuarioDtoList;    

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
        tituloPagina = "Listdao de Usuarios Activos";
        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {        
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
    
    //Getters & Setters

    public List<UsuarioDTO> getUsuarioDtoList() {
        return usuarioDtoList;
    }

    public void setUsuarioDtoList(List<UsuarioDTO> usuarioDtoList) {
        this.usuarioDtoList = usuarioDtoList;
    }    
}

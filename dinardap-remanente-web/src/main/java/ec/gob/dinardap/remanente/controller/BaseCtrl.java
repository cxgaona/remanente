package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.constante.PerfilEnum;
import ec.gob.dinardap.remanente.constante.TipoInstitucionEnum;
import ec.gob.dinardap.seguridad.dao.InstitucionDao;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseCtrl implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final Locale DEFAULT_LOCALE = new Locale("es", "EC");
    @EJB
    private InstitucionServicio institucionServicio;
    @EJB
    private InstitucionDao institucionDao;
    /**
     * Returns Jsf actual instance
     *
     * @return
     */
    protected FacesContext getFacesContext() {
        return (FacesContext.getCurrentInstance());
    }

    /**
     * Returns External Context from actual Faces context
     *
     * @return
     */
    protected ExternalContext getExternalContext() {
        return getFacesContext().getExternalContext();
    }

    /**
     *
     * @param defaultObject
     *
     * @return
     *
     */
    protected static ClassLoader getCurrentClassLoader(Object defaultObject) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        if (loader == null) {
            loader = defaultObject.getClass().getClassLoader();
        }

        return loader;
    }

    /**
     * Params se envia null si no hay parametros
     *
     * @param key
     * @param params
     * @return
     */
    protected String getBundleMensaje(String key, Object[] params) {
        Locale locale = getFacesContext().getViewRoot().getLocale();
        String ubicacion = "ec.gob.dinardap.remanente.recursos.mensajes";

        ResourceBundle bundle = ResourceBundle.getBundle(
                ubicacion, locale, getCurrentClassLoader(params));

        String mensaje = bundle.getString(key);

        if (params != null && params.length > 0) {
            MessageFormat mf = new MessageFormat(mensaje, locale);
            mensaje = mf.format(params, new StringBuffer(), null).toString();
        }

        return mensaje;
    }

    /**
     * Params se envia null si no hay parametros
     *
     * @param key
     * @param params
     * @return
     */
    protected String getBundleEtiquetas(String key, Object params[]) {
        Locale locale = DEFAULT_LOCALE;
        String ubicacion = "ec.gob.dinardap.remanente.recursos.etiquetas";

        if (getFacesContext().getViewRoot() != null) {
            locale = getFacesContext().getViewRoot().getLocale();
        }

        ResourceBundle bundle = ResourceBundle.getBundle(
                ubicacion, locale, getCurrentClassLoader(params));

        String mensaje = bundle.getString(key);

        if (params != null && params.length > 0) {
            MessageFormat mf = new MessageFormat(mensaje, locale);
            mensaje = mf.format(params, new StringBuffer(), null).toString();
        }

        return mensaje;
    }

    /**
     * Agrega un mensaje de error para mostrarlo en pantalla.
     *
     * @param componentId - null si se quiere mensaje global
     * @param summary
     * @param detail
     */
    protected void addErrorMessage(String componentId, String summary,
            String detail) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                summary, detail);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(componentId, facesMsg);
    }

    /**
     * Agrega el mensaje de informacion para mostrarlo en pantalla.
     *
     * @param summary the summary
     * @param detail the detail
     */
    protected void addInfoMessage(String summary, String detail) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                summary, detail);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, facesMsg);
    }

    /**
     * Gets the default locale.
     *
     * @return the default locale
     */
    protected Locale getDefaultLocale() {
        return DEFAULT_LOCALE;
    }

    /**
     * Obtiene locate
     *
     * @return
     */
    protected Locale getLocaleObj() {
        return DEFAULT_LOCALE;
    }

    protected static String getLoggedUser() {
        Map<String, Object> sesion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        return sesion.get("usuario").toString();
    }

    protected static void setSessionVariable(String variableName, String value) {
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSession.setAttribute(variableName, value);
    }

    protected static String getSessionVariable(String variableName) {
        Map<String, Object> sesion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        return sesion.get(variableName).toString();
    }

    protected void logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    protected Integer getInstitucionID(String perfil) {
        Integer institucionID = 0;
        if (perfil.contains(PerfilEnum.REGISTRADOR.getPerfilId().toString())) {
            institucionID = Integer.parseInt(BaseCtrl.getSessionVariable("institucionId"));
        } else if (perfil.contains(PerfilEnum.VERIFICADOR.getPerfilId().toString())) {
            institucionID = Integer.parseInt(BaseCtrl.getSessionVariable("institucionId"));
            if (getSessionVariable("institucionTipo").equals(TipoInstitucionEnum.GAD.getTipoInstitucion().toString())) {
                List<Institucion> institucionList = new ArrayList<Institucion>();
                List<Integer> institucionIdList = new ArrayList<Integer>();
                institucionIdList.add(institucionID);
                institucionList=institucionDao.obtenerHijosPorInstitucion(institucionIdList, TipoInstitucionEnum.RMX_SIN_AUTONOMIA_FINANCIERA.getTipoInstitucion());
                if(!institucionList.isEmpty()){
                    institucionID=institucionList.get(institucionList.size()-1).getInstitucionId();
                }
            }
        }
        return institucionID;
    }

    /**
     *
     * @param archivo
     * @param contentType
     * @param name
     */
    public void downloadFile(byte[] archivo, String contentType, String name) {
        FacesContext ctx = getFacesContext();

        if (!ctx.getResponseComplete()) {
            HttpServletResponse response = getHttpServletResponse();
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment;filename=\""
                    + name + "\"");

            try {
                ServletOutputStream out = response.getOutputStream();

                response.setContentLength(archivo.length);

                out = response.getOutputStream();
                out.write(archivo, 0, archivo.length);
                out.flush();
                out.close();

                ctx.responseComplete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @return
     */
    protected HttpServletResponse getHttpServletResponse() {

        return (HttpServletResponse) getExternalContext().getResponse();

    }
}

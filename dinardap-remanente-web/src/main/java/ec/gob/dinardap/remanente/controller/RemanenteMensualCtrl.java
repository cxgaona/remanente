package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.RemanenteMensual;
import ec.gob.dinardap.remanente.modelo.Transaccion;
import ec.gob.dinardap.remanente.servicio.InstitucionRequeridaServicio;
import ec.gob.dinardap.remanente.servicio.RemanenteMensualServicio;
import ec.gob.dinardap.remanente.servicio.TransaccionServicio;
import ec.gob.dinardap.remanente.utils.Constantes;
import ec.gob.dinardap.remanente.utils.FacesUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.poi.util.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

@Named(value = "remanenteMensualCtrl")
@ViewScoped
public class RemanenteMensualCtrl extends BaseCtrl implements Serializable {

    private String tituloPagina;
    private String nombreInstitucion;
    private Integer año;
    private RemanenteMensual remanenteMensualSelected;

    //===
    private Integer institucionId;

    //===Listas===//
    private List<RemanenteMensual> remanenteMensualList;

    private String mesRemanenteMensualSelected;

    private BigDecimal totalIngRPropiedad;
    private BigDecimal totalIngRMercantil;
    private BigDecimal totalEgresos;
    private Transaccion transaccionSelected;
    private Boolean showDialog;

    private List<Transaccion> transaccionRPropiedadList;
    private List<Transaccion> transaccionRMercantilList;
    private List<Transaccion> transaccionEgresosList;

    private Boolean renderedEdicionTransaccion;
    private Boolean habilitarSeleccion;

    private UploadedFile fileA;
    private String destination;

    @EJB
    private RemanenteMensualServicio remanenteMensualServicio;

    @EJB
    private InstitucionRequeridaServicio institucionRequeridaServicio;

    @EJB
    private TransaccionServicio transaccionServicio;

    @PostConstruct
    protected void init() {
        showDialog = Boolean.FALSE;
        habilitarSeleccion = Boolean.TRUE;
        tituloPagina = "Gestión Remanente Mensual";
        año = 0;
        mesRemanenteMensualSelected = "SinSelección";
        remanenteMensualSelected = new RemanenteMensual();
        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        renderedEdicionTransaccion = Boolean.FALSE;
        transaccionSelected = new Transaccion();
        institucionId = Integer.parseInt(this.getSessionVariable("institucionId"));
        nombreInstitucion = institucionRequeridaServicio.getInstitucionById(institucionId).getNombre();
        remanenteMensualList = new ArrayList<RemanenteMensual>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        remanenteMensualList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, calendar.get(Calendar.YEAR));
    }

    public void reloadRemanenteMensual() {
        remanenteMensualList = remanenteMensualServicio.getRemanenteMensualByInstitucion(institucionId, año);
    }

    public void seleccionarArchivo() {
        System.out.println("Transaeccion Seleccionada: " + transaccionSelected.getTransaccionId());
        System.out.println("Transaeccion Seleccionada: " + transaccionSelected.getCatalogoTransaccionId().getNombre());

    }

    public void onRowSelectRemanenteMensual() {
        switch (remanenteMensualSelected.getMes()) {
            case 1:
                mesRemanenteMensualSelected = "Enero";
                break;
            case 2:
                mesRemanenteMensualSelected = "Febrero";
                break;
            case 3:
                mesRemanenteMensualSelected = "Marzo";
                break;
            case 4:
                mesRemanenteMensualSelected = "Abril";
                break;
            case 5:
                mesRemanenteMensualSelected = "Mayo";
                break;
            case 6:
                mesRemanenteMensualSelected = "Junio";
                break;
            case 7:
                mesRemanenteMensualSelected = "Julio";
                break;
            case 8:
                mesRemanenteMensualSelected = "Agosto";
                break;
            case 9:
                mesRemanenteMensualSelected = "Septiembre";
                break;
            case 10:
                mesRemanenteMensualSelected = "Octubre";
                break;
            case 11:
                mesRemanenteMensualSelected = "Noviembre";
                break;
            case 12:
                mesRemanenteMensualSelected = "Diciembre";
                break;
        }
        transaccionRPropiedadList = new ArrayList<Transaccion>();
        transaccionRMercantilList = new ArrayList<Transaccion>();
        transaccionEgresosList = new ArrayList<Transaccion>();
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        for (Transaccion t : remanenteMensualSelected.getTransaccionList()) {
            if (t.getCatalogoTransaccionId().getTipo().equals("Ingreso-Propiedad")) {
                transaccionRPropiedadList.add(t);
                totalIngRPropiedad = totalIngRPropiedad.add(t.getValorTotal());
            } else if (t.getCatalogoTransaccionId().getTipo().equals("Ingreso-Mercantil")) {
                transaccionRMercantilList.add(t);
                totalIngRMercantil = totalIngRMercantil.add(t.getValorTotal());
            } else if (t.getCatalogoTransaccionId().getTipo().equals("Egreso")) {
                transaccionEgresosList.add(t);
                totalEgresos = totalEgresos.add(t.getValorTotal());
            }
        }
    }

    //Funciones
    public void onRowSelectRPropiedad() {
        habilitarSeleccion = Boolean.FALSE;
    }

    public void handleFileUploadRPropiedad(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        try {
            byte[] fileByte = IOUtils.toByteArray(file.getInputstream());
            //Se debe crear un archivo .propierties para parametrizar la dirección en donde se guardarán los archivos subidos
            String path = FacesUtils.getPath() + "/archivos/transacciones/";
            String realPath = path + transaccionSelected.getTransaccionId() + ".pdf";
            System.out.println("realPath = " + realPath);
            FileOutputStream fos = new FileOutputStream(realPath);
            fos.write(fileByte);
            fos.close();
            transaccionSelected.setRespaldoUrl("/archivos/transacciones/" + transaccionSelected.getTransaccionId() + ".pdf");
            transaccionServicio.editTransaccion(transaccionSelected);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RemanenteMensualCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('transaccionRPropiedadDlg').hide()");
        showDialog = Boolean.FALSE;

    }

    public void rowEdit() {
    }

    public void mostrarDialog() {
        showDialog = Boolean.TRUE;
    }

    public void rowEditCancel() {
    }

    public void rowTransaccionEdit(RowEditEvent event) {
        Transaccion transaccion = new Transaccion();
        transaccion = (Transaccion) event.getObject();
        transaccionServicio.editTransaccion(transaccion);
        totalIngRPropiedad = new BigDecimal(0);
        totalIngRMercantil = new BigDecimal(0);
        totalEgresos = new BigDecimal(0);
        System.out.println("Transaccion Seleccionada: " + remanenteMensualSelected.getFechaRegistro());
        for (Transaccion t : transaccionRPropiedadList) {
            totalIngRPropiedad = totalIngRPropiedad.add(t.getValorTotal());
        }
        for (Transaccion t : transaccionRMercantilList) {
            totalIngRMercantil = totalIngRMercantil.add(t.getValorTotal());
        }
        for (Transaccion t : transaccionEgresosList) {
            totalEgresos = totalEgresos.add(t.getValorTotal());
        }
    }

    public void rowTransaccionEditCancel() {
        System.out.println("Cancelado");
    }

    public void nuevoDocumento() {
        if (upload()) {

        }
    }

    public Boolean upload() {
        try {
            destination = Constantes.URL + "transaccionXXX";
            System.out.println("destination = " + destination);
            File folder = new File(FacesUtils.getPath() + destination);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            copyFile("nombrePruebaXXX.pdf", getFileA().getInputstream());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void copyFile(String fileName, InputStream in) {
        try {
            destination = destination + Constantes.SEPARADOR + fileName;
            System.out.println("destination = " + destination);
            OutputStream out = new FileOutputStream(new File(FacesUtils.getPath() + destination));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getTituloPagina() {
        return tituloPagina;
    }

    public void setTituloPagina(String tituloPagina) {
        this.tituloPagina = tituloPagina;
    }

    public List<RemanenteMensual> getRemanenteMensualList() {
        return remanenteMensualList;
    }

    public void setRemanenteMensualList(List<RemanenteMensual> remanenteMensualList) {
        this.remanenteMensualList = remanenteMensualList;
    }

    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public void setNombreInstitucion(String nombreInstitucion) {
        this.nombreInstitucion = nombreInstitucion;
    }

    public RemanenteMensual getRemanenteMensualSelected() {
        return remanenteMensualSelected;
    }

    public void setRemanenteMensualSelected(RemanenteMensual remanenteMensualSelected) {
        this.remanenteMensualSelected = remanenteMensualSelected;
    }

    public String getMesRemanenteMensualSelected() {
        return mesRemanenteMensualSelected;
    }

    public void setMesRemanenteMensualSelected(String mesRemanenteMensualSelected) {
        this.mesRemanenteMensualSelected = mesRemanenteMensualSelected;
    }

    public List<Transaccion> getTransaccionRPropiedadList() {
        return transaccionRPropiedadList;
    }

    public void setTransaccionRPropiedadList(List<Transaccion> transaccionRPropiedadList) {
        this.transaccionRPropiedadList = transaccionRPropiedadList;
    }

    public List<Transaccion> getTransaccionRMercantilList() {
        return transaccionRMercantilList;
    }

    public void setTransaccionRMercantilList(List<Transaccion> transaccionRMercantilList) {
        this.transaccionRMercantilList = transaccionRMercantilList;
    }

    public List<Transaccion> getTransaccionEgresosList() {
        return transaccionEgresosList;
    }

    public void setTransaccionEgresosList(List<Transaccion> transaccionEgresosList) {
        this.transaccionEgresosList = transaccionEgresosList;
    }

    public BigDecimal getTotalIngRPropiedad() {
        return totalIngRPropiedad;
    }

    public void setTotalIngRPropiedad(BigDecimal totalIngRPropiedad) {
        this.totalIngRPropiedad = totalIngRPropiedad;
    }

    public BigDecimal getTotalIngRMercantil() {
        return totalIngRMercantil;
    }

    public void setTotalIngRMercantil(BigDecimal totalIngRMercantil) {
        this.totalIngRMercantil = totalIngRMercantil;
    }

    public BigDecimal getTotalEgresos() {
        return totalEgresos;
    }

    public void setTotalEgresos(BigDecimal totalEgresos) {
        this.totalEgresos = totalEgresos;
    }

    public Boolean getRenderedEdicionTransaccion() {
        return renderedEdicionTransaccion;
    }

    public void setRenderedEdicionTransaccion(Boolean renderedEdicionTransaccion) {
        this.renderedEdicionTransaccion = renderedEdicionTransaccion;
    }

    public Transaccion getTransaccionSelected() {
        return transaccionSelected;
    }

    public void setTransaccionSelected(Transaccion transaccionSelected) {
        this.transaccionSelected = transaccionSelected;
    }

    public Integer getAño() {
        return año;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

    public Boolean getHabilitarSeleccion() {
        return habilitarSeleccion;
    }

    public void setHabilitarSeleccion(Boolean habilitarSeleccion) {
        this.habilitarSeleccion = habilitarSeleccion;
    }

    public UploadedFile getFileA() {
        return fileA;
    }

    public void setFileA(UploadedFile fileA) {
        this.fileA = fileA;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Boolean getShowDialog() {
        return showDialog;
    }

    public void setShowDialog(Boolean showDialog) {
        this.showDialog = showDialog;
    }

}

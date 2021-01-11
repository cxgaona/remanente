/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.dto;

import ec.gob.dinardap.remanente.modelo.Tomo;
import java.text.SimpleDateFormat;

/**
 *
 * @author christian.gaona
 */
public class DetalleLibroDTO {

    private String denominacionLibro;
    private Integer añoLibro;
    private String fechaAperturaLibro;
    private String fechaCierreLibro;
    private Integer numeroTomo;
    private Integer numeroInscripcionInicioTomo;
    private Integer numeroInscripcionFinTomo;
    private Integer totalInscripcionesContenidasTomo;
    private Integer fojaInicioTomo;
    private Integer fojaFinTomo;
    private Integer numeroTotalHojasTomo;
    private Integer numeroHojasOtroFormatoTomo;
    private String observacionesTomo;

    public DetalleLibroDTO() {
    }

    public DetalleLibroDTO(Tomo tomo) {
        this.denominacionLibro = tomo.getLibro().getDenominacion();
        this.añoLibro = tomo.getLibro().getInventarioAnual().getAnio();
        if (tomo.getLibro().getFechaApertura() != null) {
            this.fechaAperturaLibro = new SimpleDateFormat("yyyy/MM/dd").format(tomo.getLibro().getFechaApertura());
        } else {
            this.fechaAperturaLibro = "S/F";
        }
        if (tomo.getLibro().getFechaCierre() != null) {
            this.fechaCierreLibro = new SimpleDateFormat("yyyy/MM/dd").format(tomo.getLibro().getFechaCierre());
        } else {
            this.fechaCierreLibro = "S/F";
        }
        this.numeroTomo = tomo.getNumero();
        this.numeroInscripcionInicioTomo = tomo.getNumeroInscripcionInicio() == null ? (-1) : tomo.getNumeroInscripcionInicio();
        this.numeroInscripcionFinTomo = tomo.getNumeroInscripcionFin() == null ? (-1) : tomo.getNumeroInscripcionFin();
        this.totalInscripcionesContenidasTomo = tomo.getTotalInscripcionesContenidas();
        this.fojaInicioTomo = tomo.getFojaInicio();
        this.fojaFinTomo = tomo.getFojaFin();
        this.numeroTotalHojasTomo = tomo.getNumeroTotalHojas();
        this.numeroHojasOtroFormatoTomo = tomo.getNumeroHojasOtroFormato() == null ? -1 : tomo.getNumeroHojasOtroFormato();
        this.observacionesTomo = tomo.getObservaciones();
    }

    public String getDenominacionLibro() {
        return denominacionLibro;
    }

    public void setDenominacionLibro(String denominacionLibro) {
        this.denominacionLibro = denominacionLibro;
    }

    public Integer getAñoLibro() {
        return añoLibro;
    }

    public void setAñoLibro(Integer añoLibro) {
        this.añoLibro = añoLibro;
    }

    public String getFechaAperturaLibro() {
        return fechaAperturaLibro;
    }

    public void setFechaAperturaLibro(String fechaAperturaLibro) {
        this.fechaAperturaLibro = fechaAperturaLibro;
    }

    public String getFechaCierreLibro() {
        return fechaCierreLibro;
    }

    public void setFechaCierreLibro(String fechaCierreLibro) {
        this.fechaCierreLibro = fechaCierreLibro;
    }

    public Integer getNumeroTomo() {
        return numeroTomo;
    }

    public void setNumeroTomo(Integer numeroTomo) {
        this.numeroTomo = numeroTomo;
    }

    public Integer getNumeroInscripcionInicioTomo() {
        return numeroInscripcionInicioTomo;
    }

    public void setNumeroInscripcionInicioTomo(Integer numeroInscripcionInicioTomo) {
        this.numeroInscripcionInicioTomo = numeroInscripcionInicioTomo;
    }

    public Integer getNumeroInscripcionFinTomo() {
        return numeroInscripcionFinTomo;
    }

    public void setNumeroInscripcionFinTomo(Integer numeroInscripcionFinTomo) {
        this.numeroInscripcionFinTomo = numeroInscripcionFinTomo;
    }

    public Integer getTotalInscripcionesContenidasTomo() {
        return totalInscripcionesContenidasTomo;
    }

    public void setTotalInscripcionesContenidasTomo(Integer totalInscripcionesContenidasTomo) {
        this.totalInscripcionesContenidasTomo = totalInscripcionesContenidasTomo;
    }

    public Integer getFojaInicioTomo() {
        return fojaInicioTomo;
    }

    public void setFojaInicioTomo(Integer fojaInicioTomo) {
        this.fojaInicioTomo = fojaInicioTomo;
    }

    public Integer getFojaFinTomo() {
        return fojaFinTomo;
    }

    public void setFojaFinTomo(Integer fojaFinTomo) {
        this.fojaFinTomo = fojaFinTomo;
    }

    public Integer getNumeroTotalHojasTomo() {
        return numeroTotalHojasTomo;
    }

    public void setNumeroTotalHojasTomo(Integer numeroTotalHojasTomo) {
        this.numeroTotalHojasTomo = numeroTotalHojasTomo;
    }

    public Integer getNumeroHojasOtroFormatoTomo() {
        return numeroHojasOtroFormatoTomo;
    }

    public void setNumeroHojasOtroFormatoTomo(Integer numeroHojasOtroFormatoTomo) {
        this.numeroHojasOtroFormatoTomo = numeroHojasOtroFormatoTomo;
    }

    public String getObservacionesTomo() {
        return observacionesTomo;
    }

    public void setObservacionesTomo(String observacionesTomo) {
        this.observacionesTomo = observacionesTomo;
    }

}

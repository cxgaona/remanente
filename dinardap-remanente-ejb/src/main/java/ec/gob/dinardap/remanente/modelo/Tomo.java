/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Cristian
 */
@Entity
@Table(name = "tomo", schema = "ec_dinardap_inventario")
@NamedQueries({
    @NamedQuery(name = "Tomo.findAll", query = "SELECT t FROM Tomo t"),
    @NamedQuery(name = "Tomo.findByTomoId", query = "SELECT t FROM Tomo t WHERE t.tomoId = :tomoId"),
    @NamedQuery(name = "Tomo.findByNumero", query = "SELECT t FROM Tomo t WHERE t.numero = :numero"),
    @NamedQuery(name = "Tomo.findByNumeroInscripcionInicio", query = "SELECT t FROM Tomo t WHERE t.numeroInscripcionInicio = :numeroInscripcionInicio"),
    @NamedQuery(name = "Tomo.findByNumeroInscripcionFin", query = "SELECT t FROM Tomo t WHERE t.numeroInscripcionFin = :numeroInscripcionFin"),
    @NamedQuery(name = "Tomo.findByTotalInscripcionesContenidas", query = "SELECT t FROM Tomo t WHERE t.totalInscripcionesContenidas = :totalInscripcionesContenidas"),
    @NamedQuery(name = "Tomo.findByFojaInicio", query = "SELECT t FROM Tomo t WHERE t.fojaInicio = :fojaInicio"),
    @NamedQuery(name = "Tomo.findByFojaFin", query = "SELECT t FROM Tomo t WHERE t.fojaFin = :fojaFin"),
    @NamedQuery(name = "Tomo.findByNumeroTotalHojas", query = "SELECT t FROM Tomo t WHERE t.numeroTotalHojas = :numeroTotalHojas"),
    @NamedQuery(name = "Tomo.findByNumeroHojasOtroFormato", query = "SELECT t FROM Tomo t WHERE t.numeroHojasOtroFormato = :numeroHojasOtroFormato"),
    @NamedQuery(name = "Tomo.findByObservaciones", query = "SELECT t FROM Tomo t WHERE t.observaciones = :observaciones"),
    @NamedQuery(name = "Tomo.findByFechaRegistro", query = "SELECT t FROM Tomo t WHERE t.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "Tomo.findByFechaModificacionRegistro", query = "SELECT t FROM Tomo t WHERE t.fechaModificacionRegistro = :fechaModificacionRegistro")})
public class Tomo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "TOMO_GENERATOR", sequenceName = "tomo_tomo_id_seq", allocationSize = 1, schema = "ec_dinardap_inventario")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOMO_GENERATOR")
    @Column(name = "tomo_id")
    private Integer tomoId;
    
    @Column(name = "numero")
    private Integer numero;
    
    @Column(name = "numero_inscripcion_inicio")
    private Integer numeroInscripcionInicio;
    
    @Column(name = "numero_inscripcion_fin")
    private Integer numeroInscripcionFin;
    
    @Column(name = "total_inscripciones_contenidas")
    private Integer totalInscripcionesContenidas;
    
    @Column(name = "foja_inicio")
    private Integer fojaInicio;
    
    @Column(name = "foja_fin")
    private Integer fojaFin;
    
    @Column(name = "numero_total_hojas")
    private Integer numeroTotalHojas;
    
    @Column(name = "numero_hojas_otro_formato")
    private Integer numeroHojasOtroFormato;
    
    @Size(max = 2147483647)
    @Column(name = "observaciones")
    private String observaciones;
    
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    
    @Column(name = "fecha_modificacion_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacionRegistro;
    
    @NotNull
    @Column(name = "estado")
    private short estado;
    
    @JoinColumn(name = "libro_id", referencedColumnName = "libro_id")
    @ManyToOne
    private Libro libro;

    public Tomo() {
    }

    public Tomo(Integer tomoId) {
        this.tomoId = tomoId;
    }

    public Integer getTomoId() {
        return tomoId;
    }

    public void setTomoId(Integer tomoId) {
        this.tomoId = tomoId;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getNumeroInscripcionInicio() {
        return numeroInscripcionInicio;
    }

    public void setNumeroInscripcionInicio(Integer numeroInscripcionInicio) {
        this.numeroInscripcionInicio = numeroInscripcionInicio;
    }

    public Integer getNumeroInscripcionFin() {
        return numeroInscripcionFin;
    }

    public void setNumeroInscripcionFin(Integer numeroInscripcionFin) {
        this.numeroInscripcionFin = numeroInscripcionFin;
    }

    public Integer getTotalInscripcionesContenidas() {
        return totalInscripcionesContenidas;
    }

    public void setTotalInscripcionesContenidas(Integer totalInscripcionesContenidas) {
        this.totalInscripcionesContenidas = totalInscripcionesContenidas;
    }

    public Integer getFojaInicio() {
        return fojaInicio;
    }

    public void setFojaInicio(Integer fojaInicio) {
        this.fojaInicio = fojaInicio;
    }

    public Integer getFojaFin() {
        return fojaFin;
    }

    public void setFojaFin(Integer fojaFin) {
        this.fojaFin = fojaFin;
    }

    public Integer getNumeroTotalHojas() {
        return numeroTotalHojas;
    }

    public void setNumeroTotalHojas(Integer numeroTotalHojas) {
        this.numeroTotalHojas = numeroTotalHojas;
    }

    public Integer getNumeroHojasOtroFormato() {
        return numeroHojasOtroFormato;
    }

    public void setNumeroHojasOtroFormato(Integer numeroHojasOtroFormato) {
        this.numeroHojasOtroFormato = numeroHojasOtroFormato;
    }

    

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaModificacionRegistro() {
        return fechaModificacionRegistro;
    }

    public void setFechaModificacionRegistro(Date fechaModificacionRegistro) {
        this.fechaModificacionRegistro = fechaModificacionRegistro;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
       
}

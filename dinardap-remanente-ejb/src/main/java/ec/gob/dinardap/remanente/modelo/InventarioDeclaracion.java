/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import ec.gob.dinardap.seguridad.modelo.Institucion;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Cristian
 */
@Entity
@Table(name = "inventario_declaracion", schema = "ec_dinardap_inventario")
@NamedQueries({
    @NamedQuery(name = "InventarioDeclaracion.findAll", query = "SELECT i FROM InventarioDeclaracion i"),
    @NamedQuery(name = "InventarioDeclaracion.findByInventarioDeclaracionId", query = "SELECT i FROM InventarioDeclaracion i WHERE i.inventarioDeclaracionId = :inventarioDeclaracionId"),
    @NamedQuery(name = "InventarioDeclaracion.findByInstitucion", query = "SELECT i FROM InventarioDeclaracion i WHERE i.institucion = :institucion"),
    @NamedQuery(name = "InventarioDeclaracion.findByAnio", query = "SELECT i FROM InventarioDeclaracion i WHERE i.anio = :anio"),
    @NamedQuery(name = "InventarioDeclaracion.findByTipoSoporte", query = "SELECT i FROM InventarioDeclaracion i WHERE i.tipoSoporte = :tipoSoporte"),
    @NamedQuery(name = "InventarioDeclaracion.findByDenominacion", query = "SELECT i FROM InventarioDeclaracion i WHERE i.denominacion = :denominacion"),
    @NamedQuery(name = "InventarioDeclaracion.findByFechaApertura", query = "SELECT i FROM InventarioDeclaracion i WHERE i.fechaApertura = :fechaApertura"),
    @NamedQuery(name = "InventarioDeclaracion.findByFechaCierre", query = "SELECT i FROM InventarioDeclaracion i WHERE i.fechaCierre = :fechaCierre"),
    @NamedQuery(name = "InventarioDeclaracion.findByTotalAnotaciones", query = "SELECT i FROM InventarioDeclaracion i WHERE i.totalAnotaciones = :totalAnotaciones"),
    @NamedQuery(name = "InventarioDeclaracion.findByUrlArchivo", query = "SELECT i FROM InventarioDeclaracion i WHERE i.urlArchivo = :urlArchivo"),
    @NamedQuery(name = "InventarioDeclaracion.findByComentarios", query = "SELECT i FROM InventarioDeclaracion i WHERE i.comentarios = :comentarios"),
    @NamedQuery(name = "InventarioDeclaracion.findByFechaRegistro", query = "SELECT i FROM InventarioDeclaracion i WHERE i.fechaRegistro = :fechaRegistro")})
public class InventarioDeclaracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "INVENTARIO_DECLARACION_GENERATOR", sequenceName = "inventario_declaracion_inventario_declaracion_id_seq", allocationSize = 1, schema = "ec_dinardap_inventario")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVENTARIO_DECLARACION_GENERATOR")
    @Column(name = "inventario_declaracion_id")
    private Integer inventarioDeclaracionId;
    
    @ManyToOne
    @JoinColumn(name = "institucion_id", referencedColumnName = "institucion_id")
    private Institucion institucion;
    
    @Column(name = "anio")
    private Integer anio;
    
    @Size(max = 200)
    @Column(name = "tipo_soporte")
    private String tipoSoporte;
    
    @Size(max = 200)
    @Column(name = "denominacion")
    private String denominacion;
    
    @Column(name = "fecha_apertura")
    @Temporal(TemporalType.DATE)
    private Date fechaApertura;
    
    @Column(name = "fecha_cierre")
    @Temporal(TemporalType.DATE)
    private Date fechaCierre;
    
    @Column(name = "total_anotaciones")
    private Integer totalAnotaciones;
    
    @Column(name = "nombre_registrador")
    private String nombreRegistrador;
    
    @Size(max = 256)
    @Column(name = "url_archivo")
    private String urlArchivo;
    
    @Size(max = 500)
    @Column(name = "comentarios")
    private String comentarios;
    
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    
    @OneToMany(mappedBy = "inventarioDeclaracion")
    private List<EstadoInventarioDeclaracion> estadoInventarioDeclaracionList;

    public InventarioDeclaracion() {
    }

    public InventarioDeclaracion(Integer inventarioDeclaracionId) {
        this.inventarioDeclaracionId = inventarioDeclaracionId;
    }

    public Integer getInventarioDeclaracionId() {
        return inventarioDeclaracionId;
    }

    public void setInventarioDeclaracionId(Integer inventarioDeclaracionId) {
        this.inventarioDeclaracionId = inventarioDeclaracionId;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getTipoSoporte() {
        return tipoSoporte;
    }

    public void setTipoSoporte(String tipoSoporte) {
        this.tipoSoporte = tipoSoporte;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Integer getTotalAnotaciones() {
        return totalAnotaciones;
    }

    public void setTotalAnotaciones(Integer totalAnotaciones) {
        this.totalAnotaciones = totalAnotaciones;
    }

    public String getNombreRegistrador() {
        return nombreRegistrador;
    }

    public void setNombreRegistrador(String nombreRegistrador) {
        this.nombreRegistrador = nombreRegistrador;
    }

    public String getUrlArchivo() {
        return urlArchivo;
    }

    public void setUrlArchivo(String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public List<EstadoInventarioDeclaracion> getEstadoInventarioDeclaracionList() {
        return estadoInventarioDeclaracionList;
    }

    public void setEstadoInventarioDeclaracionList(List<EstadoInventarioDeclaracion> estadoInventarioDeclaracionList) {
        this.estadoInventarioDeclaracionList = estadoInventarioDeclaracionList;
    }
    
}

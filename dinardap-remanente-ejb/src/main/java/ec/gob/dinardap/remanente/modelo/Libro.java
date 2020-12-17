/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Cristian
 */
@Entity
@Table(name = "libro", schema = "ec_dinardap_inventario" )
@NamedQueries({
    @NamedQuery(name = "Libro.findAll", query = "SELECT l FROM Libro l"),
    @NamedQuery(name = "Libro.findByLibroId", query = "SELECT l FROM Libro l WHERE l.libroId = :libroId"),
    @NamedQuery(name = "Libro.findByInventarioAnualId", query = "SELECT l FROM Libro l WHERE l.inventarioAnual.inventarioAnualId = :inventarioAnualId"),
    @NamedQuery(name = "Libro.findByDenominacion", query = "SELECT l FROM Libro l WHERE l.denominacion = :denominacion"),
    @NamedQuery(name = "Libro.findByFechaApertura", query = "SELECT l FROM Libro l WHERE l.fechaApertura = :fechaApertura"),
    @NamedQuery(name = "Libro.findByFechaCierre", query = "SELECT l FROM Libro l WHERE l.fechaCierre = :fechaCierre"),
    @NamedQuery(name = "Libro.findByFechaRegistro", query = "SELECT l FROM Libro l WHERE l.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "Libro.findByFechaModificacionRegistro", query = "SELECT l FROM Libro l WHERE l.fechaModificacionRegistro = :fechaModificacionRegistro")})
public class Libro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "LIBRO_GENERATOR", sequenceName = "libro_libro_id_seq", allocationSize = 1, schema = "ec_dinardap_inventario")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIBRO_GENERATOR")
    @Column(name = "libro_id")
    private Integer libroId;
    
    @JoinColumn(name = "inventario_anual_id", referencedColumnName = "inventario_anual_id")
    @ManyToOne
    private InventarioAnual inventarioAnual;
    
    @Size(max = 50)
    @Column(name = "denominacion")
    private String denominacion;
    
    @Column(name = "fecha_apertura")
    @Temporal(TemporalType.DATE)
    private Date fechaApertura;
    
    @Column(name = "fecha_cierre")
    @Temporal(TemporalType.DATE)
    private Date fechaCierre;
    
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    
    @Column(name = "fecha_modificacion_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacionRegistro;
    
    @NotNull
    @Column(name = "estado")
    private short estado;
    
    @JoinColumn(name = "tipo_libro_id", referencedColumnName = "tipo_libro_id")
    @ManyToOne(optional = false)
    private TipoLibro tipoLibro;
    
    @OneToMany(mappedBy = "libro")
    private List<Tomo> tomoList;

    public Libro() {
    }

    public Libro(Integer libroId) {
        this.libroId = libroId;
    }

    public Integer getLibroId() {
        return libroId;
    }

    public void setLibroId(Integer libroId) {
        this.libroId = libroId;
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
    

    public TipoLibro getTipoLibro() {
        return tipoLibro;
    }

    public void setTipoLibro(TipoLibro tipoLibro) {
        this.tipoLibro = tipoLibro;
    }

    public List<Tomo> getTomoList() {
        return tomoList;
    }

    public void setTomoList(List<Tomo> tomoList) {
        this.tomoList = tomoList;
    } 

    public InventarioAnual getInventarioAnual() {
        return inventarioAnual;
    }

    public void setInventarioAnual(InventarioAnual inventarioAnual) {
        this.inventarioAnual = inventarioAnual;
    }
   
}

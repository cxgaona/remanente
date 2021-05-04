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

/**
 *
 * @author Cristian
 */
@Entity
@Table(name = "estado_inventario_declaracion", schema = "ec_dinardap_inventario")
@NamedQueries({
    @NamedQuery(name = "EstadoInventarioDeclaracion.findAll", query = "SELECT e FROM EstadoInventarioDeclaracion e"),
    @NamedQuery(name = "EstadoInventarioDeclaracion.findByEstadoInventarioDeclaracionId", query = "SELECT e FROM EstadoInventarioDeclaracion e WHERE e.estadoInventarioDeclaracionId = :estadoInventarioDeclaracionId"),
    @NamedQuery(name = "EstadoInventarioDeclaracion.findByFechaRegistro", query = "SELECT e FROM EstadoInventarioDeclaracion e WHERE e.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "EstadoInventarioDeclaracion.findByEstado", query = "SELECT e FROM EstadoInventarioDeclaracion e WHERE e.estado = :estado")})
public class EstadoInventarioDeclaracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "ESTADO_INVENTARIO_DECLARACION_GENERATOR", sequenceName = "estado_inventario_declaracion_estado_inventario_declaracion_seq", allocationSize = 1, schema = "ec_dinardap_inventario")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTADO_INVENTARIO_DECLARACION_GENERATOR")
    @Column(name = "estado_inventario_declaracion_id")
    private Integer estadoInventarioDeclaracionId;
    
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    
    @Column(name = "estado")
    private Short estado;
    
    @JoinColumn(name = "inventario_declaracion_id", referencedColumnName = "inventario_declaracion_id")
    @ManyToOne
    private InventarioDeclaracion inventarioDeclaracion;

    public EstadoInventarioDeclaracion() {
    }

    public EstadoInventarioDeclaracion(Integer estadoInventarioDeclaracionId) {
        this.estadoInventarioDeclaracionId = estadoInventarioDeclaracionId;
    }

    public Integer getEstadoInventarioDeclaracionId() {
        return estadoInventarioDeclaracionId;
    }

    public void setEstadoInventarioDeclaracionId(Integer estadoInventarioDeclaracionId) {
        this.estadoInventarioDeclaracionId = estadoInventarioDeclaracionId;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }

    public InventarioDeclaracion getInventarioDeclaracion() {
        return inventarioDeclaracion;
    }

    public void setInventarioDeclaracion(InventarioDeclaracion inventarioDeclaracion) {
        this.inventarioDeclaracion = inventarioDeclaracion;
    }

}

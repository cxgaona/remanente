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
@Table(name = "estado_inventario_anual", schema = "ec_dinardap_inventario")
@NamedQueries({
    @NamedQuery(name = "EstadoInventarioAnual.findAll", query = "SELECT e FROM EstadoInventarioAnual e"),
    @NamedQuery(name = "EstadoInventarioAnual.findByEstadoInventarioAnualId", query = "SELECT e FROM EstadoInventarioAnual e WHERE e.estadoInventarioAnualId = :estadoInventarioAnualId"),
    @NamedQuery(name = "EstadoInventarioAnual.findByFechaRegistro", query = "SELECT e FROM EstadoInventarioAnual e WHERE e.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "EstadoInventarioAnual.findByEstado", query = "SELECT e FROM EstadoInventarioAnual e WHERE e.estado = :estado")})
public class EstadoInventarioAnual implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name = "ESTADO_INVENTARIO_ANUAL_GENERATOR", sequenceName = "estado_inventario_anual_estado_inventario_anual_id_seq", allocationSize = 1, schema = "ec_dinardap_inventario")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTADO_INVENTARIO_ANUAL_GENERATOR")
    @Column(name = "estado_inventario_anual_id")
    private Integer estadoInventarioAnualId;
    
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    
    @Column(name = "estado")
    private Short estado;
    
    @JoinColumn(name = "inventario_anual_id", referencedColumnName = "inventario_anual_id")
    @ManyToOne(optional = false)
    private InventarioAnual inventarioAnual;

    public EstadoInventarioAnual() {
    }

    public EstadoInventarioAnual(Integer estadoInventarioAnualId) {
        this.estadoInventarioAnualId = estadoInventarioAnualId;
    }

    public Integer getEstadoInventarioAnualId() {
        return estadoInventarioAnualId;
    }

    public void setEstadoInventarioAnualId(Integer estadoInventarioAnualId) {
        this.estadoInventarioAnualId = estadoInventarioAnualId;
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

    public InventarioAnual getInventarioAnual() {
        return inventarioAnual;
    }

    public void setInventarioAnual(InventarioAnual inventarioAnual) {
        this.inventarioAnual = inventarioAnual;
    }

}

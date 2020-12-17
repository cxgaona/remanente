/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import ec.gob.dinardap.seguridad.modelo.Usuario;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author christian.gaona
 */
@Entity
@Table(name = "estado_remanente_cuatrimestral")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoRemanenteCuatrimestral.findAll", query = "SELECT e FROM EstadoRemanenteCuatrimestral e"),
    @NamedQuery(name = "EstadoRemanenteCuatrimestral.findByEstadoRemanenteCuatrimestralId", query = "SELECT e FROM EstadoRemanenteCuatrimestral e WHERE e.estadoRemanenteCuatrimestralId = :estadoRemanenteCuatrimestralId"),
    @NamedQuery(name = "EstadoRemanenteCuatrimestral.findByUsuarioId", query = "SELECT e FROM EstadoRemanenteCuatrimestral e WHERE e.usuario = :usuario"),
    @NamedQuery(name = "EstadoRemanenteCuatrimestral.findByFechaRegistro", query = "SELECT e FROM EstadoRemanenteCuatrimestral e WHERE e.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "EstadoRemanenteCuatrimestral.findByDescripcion", query = "SELECT e FROM EstadoRemanenteCuatrimestral e WHERE e.descripcion = :descripcion")})
public class EstadoRemanenteCuatrimestral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "ESTADO_REMANENTE_CUATRIMESTRAL_GENERATOR", sequenceName = "estado_remanente_cuatrimestra_estado_remanente_cuatrimestra_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTADO_REMANENTE_CUATRIMESTRAL_GENERATOR")
    @Column(name = "estado_remanente_cuatrimestral_id")
    private Integer estadoRemanenteCuatrimestralId;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Size(max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumns({
        @JoinColumn(name = "remanente_cuatrimestral_id", referencedColumnName = "remanente_cuatrimestral_id"),
        @JoinColumn(name = "remanente_anual_id", referencedColumnName = "remanente_anual_id"),
        @JoinColumn(name = "institucion_id", referencedColumnName = "institucion_id")})
    @ManyToOne
    private RemanenteCuatrimestral remanenteCuatrimestral;
    
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id")
    @ManyToOne
    private Usuario usuario;

    public EstadoRemanenteCuatrimestral() {
    }

    public EstadoRemanenteCuatrimestral(Integer estadoRemanenteCuatrimestralId) {
        this.estadoRemanenteCuatrimestralId = estadoRemanenteCuatrimestralId;
    }

    public Integer getEstadoRemanenteCuatrimestralId() {
        return estadoRemanenteCuatrimestralId;
    }

    public void setEstadoRemanenteCuatrimestralId(Integer estadoRemanenteCuatrimestralId) {
        this.estadoRemanenteCuatrimestralId = estadoRemanenteCuatrimestralId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }  
    
    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public RemanenteCuatrimestral getRemanenteCuatrimestral() {
        return remanenteCuatrimestral;
    }

    public void setRemanenteCuatrimestral(RemanenteCuatrimestral remanenteCuatrimestral) {
        this.remanenteCuatrimestral = remanenteCuatrimestral;
    }
    
}

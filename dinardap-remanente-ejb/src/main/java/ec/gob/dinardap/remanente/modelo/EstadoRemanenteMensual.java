/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import ec.gob.dinardap.seguridad.modelo.Usuario;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author christian.gaona
 */
@Entity
@Table(name = "estado_remanente_mensual")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoRemanenteMensual.findAll", query = "SELECT e FROM EstadoRemanenteMensual e"),
    @NamedQuery(name = "EstadoRemanenteMensual.findByEstadoRemanenteMensualId", query = "SELECT e FROM EstadoRemanenteMensual e WHERE e.estadoRemanenteMensualId = :estadoRemanenteMensualId"),
    @NamedQuery(name = "EstadoRemanenteMensual.findByUsuarioId", query = "SELECT e FROM EstadoRemanenteMensual e WHERE e.usuario = :usuario"),
    @NamedQuery(name = "EstadoRemanenteMensual.findByFechaRegistro", query = "SELECT e FROM EstadoRemanenteMensual e WHERE e.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "EstadoRemanenteMensual.findByDescripcion", query = "SELECT e FROM EstadoRemanenteMensual e WHERE e.descripcion = :descripcion")})
public class EstadoRemanenteMensual implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Basic(optional = false)
    
    @Id
    @SequenceGenerator(name = "ESTADO_REMANENTE_MENSUAL_GENERATOR", sequenceName = "estado_remanente_mensual_estado_remanente_mensual_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTADO_REMANENTE_MENSUAL_GENERATOR")
    @Column(name = "estado_remanente_mensual_id")
    private Integer estadoRemanenteMensualId;
    
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Size(max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    
    @JoinColumn(name = "remanente_mensual_id", referencedColumnName = "remanente_mensual_id")
    @ManyToOne
    private RemanenteMensual remanenteMensual;
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id")
    @ManyToOne
    private Usuario usuario;

    public EstadoRemanenteMensual() {
    }

    public EstadoRemanenteMensual(Integer estadoRemanenteMensualId) {
        this.estadoRemanenteMensualId = estadoRemanenteMensualId;
    }

    public Integer getEstadoRemanenteMensualId() {
        return estadoRemanenteMensualId;
    }

    public void setEstadoRemanenteMensualId(Integer estadoRemanenteMensualId) {
        this.estadoRemanenteMensualId = estadoRemanenteMensualId;
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

    public RemanenteMensual getRemanenteMensual() {
        return remanenteMensual;
    }

    public void setRemanenteMensual(RemanenteMensual remanenteMensual) {
        this.remanenteMensual = remanenteMensual;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
}

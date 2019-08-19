/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author christian.gaona
 */
@Entity
@Table(name = "estado_remanente_mensual")
@NamedQueries({
    @NamedQuery(name = "EstadoRemanenteMensual.findAll", query = "SELECT e FROM EstadoRemanenteMensual e")
    , @NamedQuery(name = "EstadoRemanenteMensual.findByEstadoRemanenteMensualId", query = "SELECT e FROM EstadoRemanenteMensual e WHERE e.estadoRemanenteMensualId = :estadoRemanenteMensualId")
    , @NamedQuery(name = "EstadoRemanenteMensual.findByFechaRegistro", query = "SELECT e FROM EstadoRemanenteMensual e WHERE e.fechaRegistro = :fechaRegistro")
    , @NamedQuery(name = "EstadoRemanenteMensual.findByDescripcion", query = "SELECT e FROM EstadoRemanenteMensual e WHERE e.descripcion = :descripcion")})
public class EstadoRemanenteMensual implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
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
    private RemanenteMensual remanenteMensualId;
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id")
    @ManyToOne
    private Usuario usuarioId;

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

    public RemanenteMensual getRemanenteMensualId() {
        return remanenteMensualId;
    }

    public void setRemanenteMensualId(RemanenteMensual remanenteMensualId) {
        this.remanenteMensualId = remanenteMensualId;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estadoRemanenteMensualId != null ? estadoRemanenteMensualId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoRemanenteMensual)) {
            return false;
        }
        EstadoRemanenteMensual other = (EstadoRemanenteMensual) object;
        if ((this.estadoRemanenteMensualId == null && other.estadoRemanenteMensualId != null) || (this.estadoRemanenteMensualId != null && !this.estadoRemanenteMensualId.equals(other.estadoRemanenteMensualId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.dinardap.remanente.modelo.EstadoRemanenteMensual[ estadoRemanenteMensualId=" + estadoRemanenteMensualId + " ]";
    }
    
}

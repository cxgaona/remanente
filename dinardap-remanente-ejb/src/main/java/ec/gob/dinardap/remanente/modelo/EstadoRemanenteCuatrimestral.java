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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
    @NamedQuery(name = "EstadoRemanenteCuatrimestral.findAll", query = "SELECT e FROM EstadoRemanenteCuatrimestral e")
    , @NamedQuery(name = "EstadoRemanenteCuatrimestral.findByEstadoRemanenteCuatrimestral", query = "SELECT e FROM EstadoRemanenteCuatrimestral e WHERE e.estadoRemanenteCuatrimestral = :estadoRemanenteCuatrimestral")
    , @NamedQuery(name = "EstadoRemanenteCuatrimestral.findByFechaRegistro", query = "SELECT e FROM EstadoRemanenteCuatrimestral e WHERE e.fechaRegistro = :fechaRegistro")
    , @NamedQuery(name = "EstadoRemanenteCuatrimestral.findByDescripcion", query = "SELECT e FROM EstadoRemanenteCuatrimestral e WHERE e.descripcion = :descripcion")})
public class EstadoRemanenteCuatrimestral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "estado_remanente_cuatrimestral_")
    private Integer estadoRemanenteCuatrimestral;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
    @Size(max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumns({
        @JoinColumn(name = "remanente_cuatrimestral_id", referencedColumnName = "remanente_cuatrimestral_id")
        , @JoinColumn(name = "remanente_anual_id", referencedColumnName = "remanente_anual_id")
        , @JoinColumn(name = "institucion_id", referencedColumnName = "institucion_id")})
    @ManyToOne
    private RemanenteCuatrimestral remanenteCuatrimestral;

    public EstadoRemanenteCuatrimestral() {
    }

    public EstadoRemanenteCuatrimestral(Integer estadoRemanenteCuatrimestral) {
        this.estadoRemanenteCuatrimestral = estadoRemanenteCuatrimestral;
    }

    public Integer getEstadoRemanenteCuatrimestral() {
        return estadoRemanenteCuatrimestral;
    }

    public void setEstadoRemanenteCuatrimestral(Integer estadoRemanenteCuatrimestral) {
        this.estadoRemanenteCuatrimestral = estadoRemanenteCuatrimestral;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estadoRemanenteCuatrimestral != null ? estadoRemanenteCuatrimestral.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoRemanenteCuatrimestral)) {
            return false;
        }
        EstadoRemanenteCuatrimestral other = (EstadoRemanenteCuatrimestral) object;
        if ((this.estadoRemanenteCuatrimestral == null && other.estadoRemanenteCuatrimestral != null) || (this.estadoRemanenteCuatrimestral != null && !this.estadoRemanenteCuatrimestral.equals(other.estadoRemanenteCuatrimestral))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.dinardap.remanente.modelo.EstadoRemanenteCuatrimestral[ estadoRemanenteCuatrimestral=" + estadoRemanenteCuatrimestral + " ]";
    }
    
}

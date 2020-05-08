/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import java.io.Serializable;
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
import javax.validation.constraints.Size;

/**
 *
 * @author christian.gaona
 */
@Entity
@Table(name = "prorroga_remanente_mensual")
@NamedQueries({
    @NamedQuery(name = "ProrrogaRemanenteMensual.findAll", query = "SELECT p FROM ProrrogaRemanenteMensual p")
    , @NamedQuery(name = "ProrrogaRemanenteMensual.findByProrrogaRemanenteMensualId", query = "SELECT p FROM ProrrogaRemanenteMensual p WHERE p.prorrogaRemanenteMensualId = :prorrogaRemanenteMensualId")
    , @NamedQuery(name = "ProrrogaRemanenteMensual.findByComentarioApertura", query = "SELECT p FROM ProrrogaRemanenteMensual p WHERE p.comentarioApertura = :comentarioApertura")
    , @NamedQuery(name = "ProrrogaRemanenteMensual.findByComentarioCierre", query = "SELECT p FROM ProrrogaRemanenteMensual p WHERE p.comentarioCierre = :comentarioCierre")
    , @NamedQuery(name = "ProrrogaRemanenteMensual.findByEstado", query = "SELECT p FROM ProrrogaRemanenteMensual p WHERE p.estado = :estado")})
public class ProrrogaRemanenteMensual implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "PRORROGA_REMANENTE_MENSUAL_GENERATOR", sequenceName = "prorroga_remanente_mensual_prorroga_remanente_mensual_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRORROGA_REMANENTE_MENSUAL_GENERATOR")
    @Basic(optional = false)
    @Column(name = "prorroga_remanente_mensual_id")
    private Integer prorrogaRemanenteMensualId;

    @Size(max = 2147483647)
    @Column(name = "comentario_apertura")
    private String comentarioApertura;
    
    @Size(max = 2147483647)
    @Column(name = "comentario_cierre")
    private String comentarioCierre;
    
    @Size(max = 2)
    @Column(name = "estado")
    private String estado;

    @JoinColumn(name = "remanente_mensual_id", referencedColumnName = "remanente_mensual_id")
    @ManyToOne
    private RemanenteMensual remanenteMensualId;

    public ProrrogaRemanenteMensual() {
    }

    public ProrrogaRemanenteMensual(Integer prorrogaRemanenteMensualId) {
        this.prorrogaRemanenteMensualId = prorrogaRemanenteMensualId;
    }

    public Integer getProrrogaRemanenteMensualId() {
        return prorrogaRemanenteMensualId;
    }

    public void setProrrogaRemanenteMensualId(Integer prorrogaRemanenteMensualId) {
        this.prorrogaRemanenteMensualId = prorrogaRemanenteMensualId;
    }

    public String getComentarioApertura() {
        return comentarioApertura;
    }

    public void setComentarioApertura(String comentarioApertura) {
        this.comentarioApertura = comentarioApertura;
    }

    public String getComentarioCierre() {
        return comentarioCierre;
    }

    public void setComentarioCierre(String comentarioCierre) {
        this.comentarioCierre = comentarioCierre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prorrogaRemanenteMensualId != null ? prorrogaRemanenteMensualId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProrrogaRemanenteMensual)) {
            return false;
        }
        ProrrogaRemanenteMensual other = (ProrrogaRemanenteMensual) object;
        if ((this.prorrogaRemanenteMensualId == null && other.prorrogaRemanenteMensualId != null) || (this.prorrogaRemanenteMensualId != null && !this.prorrogaRemanenteMensualId.equals(other.prorrogaRemanenteMensualId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.dinardap.remanente.modelo.ProrrogaRemanenteMensual[ prorrogaRemanenteMensualId=" + prorrogaRemanenteMensualId + " ]";
    }

    public RemanenteMensual getRemanenteMensualId() {
        return remanenteMensualId;
    }

    public void setRemanenteMensualId(RemanenteMensual remanenteMensualId) {
        this.remanenteMensualId = remanenteMensualId;
    }

}

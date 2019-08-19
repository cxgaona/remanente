/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author christian.gaona
 */
@Entity
@Table(name = "remanente_anual")
@NamedQueries({
    @NamedQuery(name = "RemanenteAnual.findAll", query = "SELECT r FROM RemanenteAnual r")
    , @NamedQuery(name = "RemanenteAnual.findByRemanenteAnualId", query = "SELECT r FROM RemanenteAnual r WHERE r.remanenteAnualPK.remanenteAnualId = :remanenteAnualId")
    , @NamedQuery(name = "RemanenteAnual.findByInstitucionId", query = "SELECT r FROM RemanenteAnual r WHERE r.remanenteAnualPK.institucionId = :institucionId")
    , @NamedQuery(name = "RemanenteAnual.findByAnio", query = "SELECT r FROM RemanenteAnual r WHERE r.anio = :anio")})
public class RemanenteAnual implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RemanenteAnualPK remanenteAnualPK;
    @Column(name = "anio")
    private Integer anio;
    @JoinColumn(name = "institucion_id", referencedColumnName = "institucion_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private InstitucionRequerida institucionRequerida;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "remanenteAnual")
    private List<RemanenteCuatrimestral> remanenteCuatrimestralList;

    public RemanenteAnual() {
    }

    public RemanenteAnual(RemanenteAnualPK remanenteAnualPK) {
        this.remanenteAnualPK = remanenteAnualPK;
    }

    public RemanenteAnual(int remanenteAnualId, int institucionId) {
        this.remanenteAnualPK = new RemanenteAnualPK(remanenteAnualId, institucionId);
    }

    public RemanenteAnualPK getRemanenteAnualPK() {
        return remanenteAnualPK;
    }

    public void setRemanenteAnualPK(RemanenteAnualPK remanenteAnualPK) {
        this.remanenteAnualPK = remanenteAnualPK;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public InstitucionRequerida getInstitucionRequerida() {
        return institucionRequerida;
    }

    public void setInstitucionRequerida(InstitucionRequerida institucionRequerida) {
        this.institucionRequerida = institucionRequerida;
    }

    public List<RemanenteCuatrimestral> getRemanenteCuatrimestralList() {
        return remanenteCuatrimestralList;
    }

    public void setRemanenteCuatrimestralList(List<RemanenteCuatrimestral> remanenteCuatrimestralList) {
        this.remanenteCuatrimestralList = remanenteCuatrimestralList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (remanenteAnualPK != null ? remanenteAnualPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RemanenteAnual)) {
            return false;
        }
        RemanenteAnual other = (RemanenteAnual) object;
        if ((this.remanenteAnualPK == null && other.remanenteAnualPK != null) || (this.remanenteAnualPK != null && !this.remanenteAnualPK.equals(other.remanenteAnualPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.dinardap.remanente.modelo.RemanenteAnual[ remanenteAnualPK=" + remanenteAnualPK + " ]";
    }
    
}

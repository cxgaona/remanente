/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author christian.gaona
 */
@Embeddable
public class RemanenteCuatrimestralPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "remanente_cuatrimestral_id")
    private int remanenteCuatrimestralId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "remanente_anual_id")
    private int remanenteAnualId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "institucion_id")
    private int institucionId;

    public RemanenteCuatrimestralPK() {
    }

    public RemanenteCuatrimestralPK(int remanenteCuatrimestralId, int remanenteAnualId, int institucionId) {
        this.remanenteCuatrimestralId = remanenteCuatrimestralId;
        this.remanenteAnualId = remanenteAnualId;
        this.institucionId = institucionId;
    }

    public int getRemanenteCuatrimestralId() {
        return remanenteCuatrimestralId;
    }

    public void setRemanenteCuatrimestralId(int remanenteCuatrimestralId) {
        this.remanenteCuatrimestralId = remanenteCuatrimestralId;
    }

    public int getRemanenteAnualId() {
        return remanenteAnualId;
    }

    public void setRemanenteAnualId(int remanenteAnualId) {
        this.remanenteAnualId = remanenteAnualId;
    }

    public int getInstitucionId() {
        return institucionId;
    }

    public void setInstitucionId(int institucionId) {
        this.institucionId = institucionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) remanenteCuatrimestralId;
        hash += (int) remanenteAnualId;
        hash += (int) institucionId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RemanenteCuatrimestralPK)) {
            return false;
        }
        RemanenteCuatrimestralPK other = (RemanenteCuatrimestralPK) object;
        if (this.remanenteCuatrimestralId != other.remanenteCuatrimestralId) {
            return false;
        }
        if (this.remanenteAnualId != other.remanenteAnualId) {
            return false;
        }
        if (this.institucionId != other.institucionId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestralPK[ remanenteCuatrimestralId=" + remanenteCuatrimestralId + ", remanenteAnualId=" + remanenteAnualId + ", institucionId=" + institucionId + " ]";
    }
    
}

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
    
}

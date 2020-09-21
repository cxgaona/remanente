/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import ec.gob.dinardap.seguridad.modelo.Institucion;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author christian.gaona
 */
@Entity
@Table(name = "remanente_anual")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemanenteAnual.findAll", query = "SELECT r FROM RemanenteAnual r"),
    @NamedQuery(name = "RemanenteAnual.findByRemanenteAnualId", query = "SELECT r FROM RemanenteAnual r WHERE r.remanenteAnualPK.remanenteAnualId = :remanenteAnualId"),
    @NamedQuery(name = "RemanenteAnual.findByInstitucionId", query = "SELECT r FROM RemanenteAnual r WHERE r.remanenteAnualPK.institucionId = :institucionId"),
    @NamedQuery(name = "RemanenteAnual.findByAnio", query = "SELECT r FROM RemanenteAnual r WHERE r.anio = :anio")})
public class RemanenteAnual implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RemanenteAnualPK remanenteAnualPK;
    @Column(name = "anio")
    private Integer anio;
    @ManyToOne
    @JoinColumn(name = "institucion_id", referencedColumnName = "institucion_id", insertable = false, updatable = false)
    private Institucion institucion;
    
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

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }
    
    @XmlTransient
    public List<RemanenteCuatrimestral> getRemanenteCuatrimestralList() {
        return remanenteCuatrimestralList;
    }

    public void setRemanenteCuatrimestralList(List<RemanenteCuatrimestral> remanenteCuatrimestralList) {
        this.remanenteCuatrimestralList = remanenteCuatrimestralList;
    }

}

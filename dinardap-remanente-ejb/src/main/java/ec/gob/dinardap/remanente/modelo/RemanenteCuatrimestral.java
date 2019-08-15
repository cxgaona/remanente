/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author christian.gaona
 */
@Entity
@Table(name = "remanente_cuatrimestral")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemanenteCuatrimestral.findAll", query = "SELECT r FROM RemanenteCuatrimestral r")
    , @NamedQuery(name = "RemanenteCuatrimestral.findByRemanenteCuatrimestralId", query = "SELECT r FROM RemanenteCuatrimestral r WHERE r.remanenteCuatrimestralPK.remanenteCuatrimestralId = :remanenteCuatrimestralId")
    , @NamedQuery(name = "RemanenteCuatrimestral.findByRemanenteAnualId", query = "SELECT r FROM RemanenteCuatrimestral r WHERE r.remanenteCuatrimestralPK.remanenteAnualId = :remanenteAnualId")
    , @NamedQuery(name = "RemanenteCuatrimestral.findByInstitucionId", query = "SELECT r FROM RemanenteCuatrimestral r WHERE r.remanenteCuatrimestralPK.institucionId = :institucionId")
    , @NamedQuery(name = "RemanenteCuatrimestral.findByFecha", query = "SELECT r FROM RemanenteCuatrimestral r WHERE r.fecha = :fecha")
    , @NamedQuery(name = "RemanenteCuatrimestral.findByInformeRemanenteUrl", query = "SELECT r FROM RemanenteCuatrimestral r WHERE r.informeRemanenteUrl = :informeRemanenteUrl")
    , @NamedQuery(name = "RemanenteCuatrimestral.findByInformeTecnicoUrl", query = "SELECT r FROM RemanenteCuatrimestral r WHERE r.informeTecnicoUrl = :informeTecnicoUrl")})
public class RemanenteCuatrimestral implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected RemanenteCuatrimestralPK remanenteCuatrimestralPK;

    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @Column(name = "informe_remanente_url", length = 2147483647)
    private String informeRemanenteUrl;

    @Column(name = "informe_tecnico_url", length = 2147483647)
    private String informeTecnicoUrl;

    @OneToMany(mappedBy = "remanenteCuatrimestral")
    private List<Bandeja> bandejaList;

    @OneToMany(mappedBy = "remanenteCuatrimestral")
    private List<RemanenteMensual> remanenteMensualList;

    @ManyToOne(optional = false)
    @JoinColumns({
        @JoinColumn(name = "remanente_anual_id", referencedColumnName = "remanente_anual_id", insertable = false, updatable = false)
        , @JoinColumn(name = "institucion_id", referencedColumnName = "institucion_id", insertable = false, updatable = false)})
    private RemanenteAnual remanenteAnual;
    @OneToMany(mappedBy = "remanenteCuatrimestral")
    private List<EstadoRemanenteCuatrimestral> estadoRemanenteCuatrimestralList;

    public RemanenteCuatrimestral() {
    }

    public RemanenteCuatrimestral(RemanenteCuatrimestralPK remanenteCuatrimestralPK) {
        this.remanenteCuatrimestralPK = remanenteCuatrimestralPK;
    }

    public RemanenteCuatrimestral(int remanenteCuatrimestralId, int remanenteAnualId, int institucionId) {
        this.remanenteCuatrimestralPK = new RemanenteCuatrimestralPK(remanenteCuatrimestralId, remanenteAnualId, institucionId);
    }

    public RemanenteCuatrimestralPK getRemanenteCuatrimestralPK() {
        return remanenteCuatrimestralPK;
    }

    public void setRemanenteCuatrimestralPK(RemanenteCuatrimestralPK remanenteCuatrimestralPK) {
        this.remanenteCuatrimestralPK = remanenteCuatrimestralPK;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getInformeRemanenteUrl() {
        return informeRemanenteUrl;
    }

    public void setInformeRemanenteUrl(String informeRemanenteUrl) {
        this.informeRemanenteUrl = informeRemanenteUrl;
    }

    public String getInformeTecnicoUrl() {
        return informeTecnicoUrl;
    }

    public void setInformeTecnicoUrl(String informeTecnicoUrl) {
        this.informeTecnicoUrl = informeTecnicoUrl;
    }

    @XmlTransient
    public List<Bandeja> getBandejaList() {
        return bandejaList;
    }

    public void setBandejaList(List<Bandeja> bandejaList) {
        this.bandejaList = bandejaList;
    }

    @XmlTransient
    public List<RemanenteMensual> getRemanenteMensualList() {
        return remanenteMensualList;
    }

    public void setRemanenteMensualList(List<RemanenteMensual> remanenteMensualList) {
        this.remanenteMensualList = remanenteMensualList;
    }

    public RemanenteAnual getRemanenteAnual() {
        return remanenteAnual;
    }

    public void setRemanenteAnual(RemanenteAnual remanenteAnual) {
        this.remanenteAnual = remanenteAnual;
    }

    @XmlTransient
    public List<EstadoRemanenteCuatrimestral> getEstadoRemanenteCuatrimestralList() {
        return estadoRemanenteCuatrimestralList;
    }

    public void setEstadoRemanenteCuatrimestralList(List<EstadoRemanenteCuatrimestral> estadoRemanenteCuatrimestralList) {
        this.estadoRemanenteCuatrimestralList = estadoRemanenteCuatrimestralList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (remanenteCuatrimestralPK != null ? remanenteCuatrimestralPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RemanenteCuatrimestral)) {
            return false;
        }
        RemanenteCuatrimestral other = (RemanenteCuatrimestral) object;
        if ((this.remanenteCuatrimestralPK == null && other.remanenteCuatrimestralPK != null) || (this.remanenteCuatrimestralPK != null && !this.remanenteCuatrimestralPK.equals(other.remanenteCuatrimestralPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.dinardap.remanente.modelo.RemanenteCuatrimestral[ remanenteCuatrimestralPK=" + remanenteCuatrimestralPK + " ]";
    }

}

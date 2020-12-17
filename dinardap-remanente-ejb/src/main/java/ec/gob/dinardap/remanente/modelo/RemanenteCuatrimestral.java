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
import javax.validation.constraints.Size;
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
    @NamedQuery(name = "RemanenteCuatrimestral.findAll", query = "SELECT r FROM RemanenteCuatrimestral r"),
    @NamedQuery(name = "RemanenteCuatrimestral.findByRemanenteCuatrimestralId", query = "SELECT r FROM RemanenteCuatrimestral r WHERE r.remanenteCuatrimestralPK.remanenteCuatrimestralId = :remanenteCuatrimestralId"),
    @NamedQuery(name = "RemanenteCuatrimestral.findByRemanenteAnualId", query = "SELECT r FROM RemanenteCuatrimestral r WHERE r.remanenteCuatrimestralPK.remanenteAnualId = :remanenteAnualId"),
    @NamedQuery(name = "RemanenteCuatrimestral.findByInstitucionId", query = "SELECT r FROM RemanenteCuatrimestral r WHERE r.remanenteCuatrimestralPK.institucionId = :institucionId"),
    @NamedQuery(name = "RemanenteCuatrimestral.findByCuatrimestre", query = "SELECT r FROM RemanenteCuatrimestral r WHERE r.cuatrimestre = :cuatrimestre"),
    @NamedQuery(name = "RemanenteCuatrimestral.findByFechaRegistro", query = "SELECT r FROM RemanenteCuatrimestral r WHERE r.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "RemanenteCuatrimestral.findByInformeRemanenteUrl", query = "SELECT r FROM RemanenteCuatrimestral r WHERE r.informeRemanenteUrl = :informeRemanenteUrl"),
    @NamedQuery(name = "RemanenteCuatrimestral.findByInformeTecnicoUrl", query = "SELECT r FROM RemanenteCuatrimestral r WHERE r.informeTecnicoUrl = :informeTecnicoUrl")})
public class RemanenteCuatrimestral implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RemanenteCuatrimestralPK remanenteCuatrimestralPK;
    @Column(name = "cuatrimestre")
    private Integer cuatrimestre;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Size(max = 2147483647)
    @Column(name = "informe_remanente_url")
    private String informeRemanenteUrl;
    @Size(max = 2147483647)
    @Column(name = "informe_tecnico_url")
    private String informeTecnicoUrl;
    @OneToMany(mappedBy = "remanenteCuatrimestral")
    private List<Bandeja> bandejaList;
    @JoinColumns({
        @JoinColumn(name = "remanente_anual_id", referencedColumnName = "remanente_anual_id", insertable = false, updatable = false),
        @JoinColumn(name = "institucion_id", referencedColumnName = "institucion_id", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private RemanenteAnual remanenteAnual;
    @OneToMany(mappedBy = "remanenteCuatrimestral")
    private List<EstadoRemanenteCuatrimestral> estadoRemanenteCuatrimestralList;
    @OneToMany(mappedBy = "remanenteCuatrimestral")
    private List<RemanenteMensual> remanenteMensualList;

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

    public Integer getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(Integer cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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

    @XmlTransient
    public List<RemanenteMensual> getRemanenteMensualList() {
        return remanenteMensualList;
    }

    public void setRemanenteMensualList(List<RemanenteMensual> remanenteMensualList) {
        this.remanenteMensualList = remanenteMensualList;
    }

}

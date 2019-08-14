/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
@Table(name = "remanente_mensual")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemanenteMensual.findAll", query = "SELECT r FROM RemanenteMensual r")
    , @NamedQuery(name = "RemanenteMensual.findByRemanenteMensualId", query = "SELECT r FROM RemanenteMensual r WHERE r.remanenteMensualId = :remanenteMensualId")
    , @NamedQuery(name = "RemanenteMensual.findByFecha", query = "SELECT r FROM RemanenteMensual r WHERE r.fecha = :fecha")
    , @NamedQuery(name = "RemanenteMensual.findByTotal", query = "SELECT r FROM RemanenteMensual r WHERE r.total = :total")
    , @NamedQuery(name = "RemanenteMensual.findByComentarios", query = "SELECT r FROM RemanenteMensual r WHERE r.comentarios = :comentarios")
    , @NamedQuery(name = "RemanenteMensual.findBySolicitudCambioUrl", query = "SELECT r FROM RemanenteMensual r WHERE r.solicitudCambioUrl = :solicitudCambioUrl")
    , @NamedQuery(name = "RemanenteMensual.findByInformeAprobacionUrl", query = "SELECT r FROM RemanenteMensual r WHERE r.informeAprobacionUrl = :informeAprobacionUrl")})
public class RemanenteMensual implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "remanente_mensual_id")
    private Integer remanenteMensualId;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private Double total;
    @Size(max = 500)
    @Column(name = "comentarios")
    private String comentarios;
    @Size(max = 2147483647)
    @Column(name = "solicitud_cambio_url")
    private String solicitudCambioUrl;
    @Size(max = 2147483647)
    @Column(name = "informe_aprobacion_url")
    private String informeAprobacionUrl;
    @OneToMany(mappedBy = "remanenteMensualId")
    private List<Bandeja> bandejaList;
    @JoinColumns({
        @JoinColumn(name = "remanente_cuatrimestral_id", referencedColumnName = "remanente_cuatrimestral_id")
        , @JoinColumn(name = "remanente_anual_id", referencedColumnName = "remanente_anual_id")
        , @JoinColumn(name = "institucion_id", referencedColumnName = "institucion_id")})
    @ManyToOne
    private RemanenteCuatrimestral remanenteCuatrimestral;
    @OneToMany(mappedBy = "remanenteMensualOrigenId")
    private List<RemanenteMensual> remanenteMensualList;
    @JoinColumn(name = "remanente_mensual_origen_id", referencedColumnName = "remanente_mensual_id")
    @ManyToOne
    private RemanenteMensual remanenteMensualOrigenId;
    @OneToMany(mappedBy = "remanenteMensualId")
    private List<EstadoRemanenteMensual> estadoRemanenteMensualList;
    @OneToMany(mappedBy = "remanenteMensualId")
    private List<Transaccion> transaccionList;

    public RemanenteMensual() {
    }

    public RemanenteMensual(Integer remanenteMensualId) {
        this.remanenteMensualId = remanenteMensualId;
    }

    public Integer getRemanenteMensualId() {
        return remanenteMensualId;
    }

    public void setRemanenteMensualId(Integer remanenteMensualId) {
        this.remanenteMensualId = remanenteMensualId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getSolicitudCambioUrl() {
        return solicitudCambioUrl;
    }

    public void setSolicitudCambioUrl(String solicitudCambioUrl) {
        this.solicitudCambioUrl = solicitudCambioUrl;
    }

    public String getInformeAprobacionUrl() {
        return informeAprobacionUrl;
    }

    public void setInformeAprobacionUrl(String informeAprobacionUrl) {
        this.informeAprobacionUrl = informeAprobacionUrl;
    }

    @XmlTransient
    public List<Bandeja> getBandejaList() {
        return bandejaList;
    }

    public void setBandejaList(List<Bandeja> bandejaList) {
        this.bandejaList = bandejaList;
    }

    public RemanenteCuatrimestral getRemanenteCuatrimestral() {
        return remanenteCuatrimestral;
    }

    public void setRemanenteCuatrimestral(RemanenteCuatrimestral remanenteCuatrimestral) {
        this.remanenteCuatrimestral = remanenteCuatrimestral;
    }

    @XmlTransient
    public List<RemanenteMensual> getRemanenteMensualList() {
        return remanenteMensualList;
    }

    public void setRemanenteMensualList(List<RemanenteMensual> remanenteMensualList) {
        this.remanenteMensualList = remanenteMensualList;
    }

    public RemanenteMensual getRemanenteMensualOrigenId() {
        return remanenteMensualOrigenId;
    }

    public void setRemanenteMensualOrigenId(RemanenteMensual remanenteMensualOrigenId) {
        this.remanenteMensualOrigenId = remanenteMensualOrigenId;
    }

    @XmlTransient
    public List<EstadoRemanenteMensual> getEstadoRemanenteMensualList() {
        return estadoRemanenteMensualList;
    }

    public void setEstadoRemanenteMensualList(List<EstadoRemanenteMensual> estadoRemanenteMensualList) {
        this.estadoRemanenteMensualList = estadoRemanenteMensualList;
    }

    @XmlTransient
    public List<Transaccion> getTransaccionList() {
        return transaccionList;
    }

    public void setTransaccionList(List<Transaccion> transaccionList) {
        this.transaccionList = transaccionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (remanenteMensualId != null ? remanenteMensualId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RemanenteMensual)) {
            return false;
        }
        RemanenteMensual other = (RemanenteMensual) object;
        if ((this.remanenteMensualId == null && other.remanenteMensualId != null) || (this.remanenteMensualId != null && !this.remanenteMensualId.equals(other.remanenteMensualId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.dinardap.remanente.modelo.RemanenteMensual[ remanenteMensualId=" + remanenteMensualId + " ]";
    }
    
}

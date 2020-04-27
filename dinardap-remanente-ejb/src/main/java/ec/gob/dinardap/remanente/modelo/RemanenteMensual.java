/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author christian.gaona
 */
@Entity
@Table(name = "remanente_mensual")
@NamedQueries({
    @NamedQuery(name = "RemanenteMensual.findAll", query = "SELECT r FROM RemanenteMensual r")
    , @NamedQuery(name = "RemanenteMensual.findByRemanenteMensualId", query = "SELECT r FROM RemanenteMensual r WHERE r.remanenteMensualId = :remanenteMensualId")
    , @NamedQuery(name = "RemanenteMensual.findByMes", query = "SELECT r FROM RemanenteMensual r WHERE r.mes = :mes")
    , @NamedQuery(name = "RemanenteMensual.findByFechaRegistro", query = "SELECT r FROM RemanenteMensual r WHERE r.fechaRegistro = :fechaRegistro")
    , @NamedQuery(name = "RemanenteMensual.findByTotal", query = "SELECT r FROM RemanenteMensual r WHERE r.total = :total")
    , @NamedQuery(name = "RemanenteMensual.findByComentarios", query = "SELECT r FROM RemanenteMensual r WHERE r.comentarios = :comentarios")
    , @NamedQuery(name = "RemanenteMensual.findBySolicitudCambioUrl", query = "SELECT r FROM RemanenteMensual r WHERE r.solicitudCambioUrl = :solicitudCambioUrl")
    , @NamedQuery(name = "RemanenteMensual.findByInformeAprobacionUrl", query = "SELECT r FROM RemanenteMensual r WHERE r.informeAprobacionUrl = :informeAprobacionUrl")})
public class RemanenteMensual implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "REMANENTE_MENSUAL_GENERATOR", sequenceName = "remanente_mensual_remanente_mensual_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REMANENTE_MENSUAL_GENERATOR")
    @Column(name = "remanente_mensual_id")
    private Integer remanenteMensualId;

    @Column(name = "mes")
    private Integer mes;

    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "comentarios", length = 500)
    private String comentarios;

    @Column(name = "solicitud_cambio_url", length = 2147483647)
    private String solicitudCambioUrl;

    @Column(name = "informe_aprobacion_url", length = 2147483647)
    private String informeAprobacionUrl;

    //Prórroga
    @ManyToOne
    @JoinColumn(name = "prorroga_remanente_mensual_id", referencedColumnName = "prorroga_remanente_mensual_id")
    private ProrrogaRemanenteMensual prorrogaRemanenteMensualId;

    //Bandeja//
    @OneToMany(mappedBy = "remanenteMensualId")
    private List<Bandeja> bandejaList;

    //RemanenteCuatrimestral//
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "remanente_cuatrimestral_id", referencedColumnName = "remanente_cuatrimestral_id")
        , @JoinColumn(name = "remanente_anual_id", referencedColumnName = "remanente_anual_id")
        , @JoinColumn(name = "institucion_id", referencedColumnName = "institucion_id")})
    private RemanenteCuatrimestral remanenteCuatrimestral;

    //Remanente Origen//
    @OneToMany(mappedBy = "remanenteMensualOrigenId")
    private List<RemanenteMensual> remanenteMensualList;

    @ManyToOne
    @JoinColumn(name = "remanente_mensual_origen_id", referencedColumnName = "remanente_mensual_id")
    private RemanenteMensual remanenteMensualOrigenId;
    //Remanente Origen//

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

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
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

    public List<EstadoRemanenteMensual> getEstadoRemanenteMensualList() {
        return estadoRemanenteMensualList;
    }

    public void setEstadoRemanenteMensualList(List<EstadoRemanenteMensual> estadoRemanenteMensualList) {
        this.estadoRemanenteMensualList = estadoRemanenteMensualList;
    }

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
        return "RemanenteMensual{" + "remanenteMensualId=" + remanenteMensualId + ", mes=" + mes + ", fechaRegistro=" + fechaRegistro + ", total=" + total + ", comentarios=" + comentarios + ", solicitudCambioUrl=" + solicitudCambioUrl + ", informeAprobacionUrl=" + informeAprobacionUrl + ", bandejaList=" + bandejaList + ", remanenteCuatrimestral=" + remanenteCuatrimestral + ", remanenteMensualList=" + remanenteMensualList + ", remanenteMensualOrigenId=" + remanenteMensualOrigenId + ", estadoRemanenteMensualList=" + estadoRemanenteMensualList + ", transaccionList=" + transaccionList + '}';
    }

    public ProrrogaRemanenteMensual getProrrogaRemanenteMensualId() {
        return prorrogaRemanenteMensualId;
    }

    public void setProrrogaRemanenteMensualId(ProrrogaRemanenteMensual prorrogaRemanenteMensualId) {
        this.prorrogaRemanenteMensualId = prorrogaRemanenteMensualId;
    }

}

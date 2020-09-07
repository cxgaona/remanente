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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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
@Table(name = "transaccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transaccion.findAll", query = "SELECT t FROM Transaccion t"),
    @NamedQuery(name = "Transaccion.findByTransaccionId", query = "SELECT t FROM Transaccion t WHERE t.transaccionId = :transaccionId"),
    @NamedQuery(name = "Transaccion.findByValorTotal", query = "SELECT t FROM Transaccion t WHERE t.valorTotal = :valorTotal"),
    @NamedQuery(name = "Transaccion.findByRespaldoUrl", query = "SELECT t FROM Transaccion t WHERE t.respaldoUrl = :respaldoUrl"),
    @NamedQuery(name = "Transaccion.findByFechaRegistro", query = "SELECT t FROM Transaccion t WHERE t.fechaRegistro = :fechaRegistro")})
public class Transaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @Basic(optional = false)
    
    @Id
    @SequenceGenerator(name = "TRANSACCION_GENERATOR", sequenceName = "transaccion_transaccion_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSACCION_GENERATOR")
    @Column(name = "transaccion_id")
    private Integer transaccionId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_total")
    private BigDecimal valorTotal;
    @Size(max = 2147483647)
    @Column(name = "respaldo_url")
    private String respaldoUrl;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @OneToMany(mappedBy = "transaccionId")
    private List<FacturaPagada> facturaPagadaList;
    @OneToMany(mappedBy = "transaccionId")
    private List<Nomina> nominaList;
    @OneToMany(mappedBy = "transaccionId")
    private List<Tramite> tramiteList;
    @JoinColumn(name = "catalogo_transaccion_id", referencedColumnName = "catalogo_transaccion_id")
    @ManyToOne
    private CatalogoTransaccion catalogoTransaccion;
    @JoinColumn(name = "remanente_mensual_id", referencedColumnName = "remanente_mensual_id")
    @ManyToOne
    private RemanenteMensual remanenteMensual;

    public Transaccion() {
    }

    public Transaccion(Integer transaccionId) {
        this.transaccionId = transaccionId;
    }

    public Integer getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(Integer transaccionId) {
        this.transaccionId = transaccionId;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getRespaldoUrl() {
        return respaldoUrl;
    }

    public void setRespaldoUrl(String respaldoUrl) {
        this.respaldoUrl = respaldoUrl;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @XmlTransient
    public List<Nomina> getNominaList() {
        return nominaList;
    }

    public void setNominaList(List<Nomina> nominaList) {
        this.nominaList = nominaList;
    }

    @XmlTransient
    public List<Tramite> getTramiteList() {
        return tramiteList;
    }

    public void setTramiteList(List<Tramite> tramiteList) {
        this.tramiteList = tramiteList;
    }

    @XmlTransient
    public List<FacturaPagada> getFacturaPagadaList() {
        return facturaPagadaList;
    }

    public void setFacturaPagadaList(List<FacturaPagada> facturaPagadaList) {
        this.facturaPagadaList = facturaPagadaList;
    }

    public CatalogoTransaccion getCatalogoTransaccion() {
        return catalogoTransaccion;
    }

    public void setCatalogoTransaccion(CatalogoTransaccion catalogoTransaccion) {
        this.catalogoTransaccion = catalogoTransaccion;
    }

    public RemanenteMensual getRemanenteMensual() {
        return remanenteMensual;
    }

    public void setRemanenteMensual(RemanenteMensual remanenteMensual) {
        this.remanenteMensual = remanenteMensual;
    }    
    
}

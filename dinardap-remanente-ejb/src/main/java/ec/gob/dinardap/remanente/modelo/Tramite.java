/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author christian.gaona
 */
@Entity
@Table(name = "tramite")
@NamedQueries({
    @NamedQuery(name = "Tramite.findAll", query = "SELECT t FROM Tramite t")
    , @NamedQuery(name = "Tramite.findByTramiteId", query = "SELECT t FROM Tramite t WHERE t.tramiteId = :tramiteId")
    , @NamedQuery(name = "Tramite.findByNumero", query = "SELECT t FROM Tramite t WHERE t.numero = :numero")
    , @NamedQuery(name = "Tramite.findByFecha", query = "SELECT t FROM Tramite t WHERE t.fecha = :fecha")
    , @NamedQuery(name = "Tramite.findByFechaRegistro", query = "SELECT t FROM Tramite t WHERE t.fechaRegistro = :fechaRegistro")
    , @NamedQuery(name = "Tramite.findByNumeroComprobantePago", query = "SELECT t FROM Tramite t WHERE t.numeroComprobantePago = :numeroComprobantePago")
    , @NamedQuery(name = "Tramite.findByValor", query = "SELECT t FROM Tramite t WHERE t.valor = :valor")
    , @NamedQuery(name = "Tramite.findByActividadRegistral", query = "SELECT t FROM Tramite t WHERE t.actividadRegistral = :actividadRegistral")
    , @NamedQuery(name = "Tramite.findByTipo", query = "SELECT t FROM Tramite t WHERE t.tipo = :tipo")
    , @NamedQuery(name = "Tramite.findByActo", query = "SELECT t FROM Tramite t WHERE t.acto = :acto")})
public class Tramite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TRAMITE_GENERATOR", sequenceName = "tramite_tramite_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRAMITE_GENERATOR")
    @Column(name = "tramite_id")
    private Integer tramiteId;

    @Size(max = 10)
    @Column(name = "numero")
    private String numero;
    @Size(max = 10)
    @Column(name = "numero_repertorio")
    private String numeroRepertorio;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Size(max = 15)
    @Column(name = "numero_comprobante_pago")
    private String numeroComprobantePago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Size(max = 50)
    @Column(name = "actividad_registral")
    private String actividadRegistral;
    @Size(max = 50)
    @Column(name = "tipo")
    private String tipo;
    @Size(max = 300)
    @Column(name = "acto")
    private String acto;
    @JoinColumn(name = "transaccion_id", referencedColumnName = "transaccion_id")
    @ManyToOne
    private Transaccion transaccionId;

    public Tramite() {
    }

    public Tramite(Integer tramiteId) {
        this.tramiteId = tramiteId;
    }

    public Integer getTramiteId() {
        return tramiteId;
    }

    public void setTramiteId(Integer tramiteId) {
        this.tramiteId = tramiteId;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNumeroComprobantePago() {
        return numeroComprobantePago;
    }

    public void setNumeroComprobantePago(String numeroComprobantePago) {
        this.numeroComprobantePago = numeroComprobantePago;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getActividadRegistral() {
        return actividadRegistral;
    }

    public void setActividadRegistral(String actividadRegistral) {
        this.actividadRegistral = actividadRegistral;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getActo() {
        return acto;
    }

    public void setActo(String acto) {
        this.acto = acto;
    }

    public Transaccion getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(Transaccion transaccionId) {
        this.transaccionId = transaccionId;
    }

    public String getNumeroRepertorio() {
        return numeroRepertorio;
    }

    public void setNumeroRepertorio(String numeroRepertorio) {
        this.numeroRepertorio = numeroRepertorio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tramiteId != null ? tramiteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tramite)) {
            return false;
        }
        Tramite other = (Tramite) object;
        if ((this.tramiteId == null && other.tramiteId != null) || (this.tramiteId != null && !this.tramiteId.equals(other.tramiteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.dinardap.remanente.modelo.Tramite[ tramiteId=" + tramiteId + " ]";
    }

}

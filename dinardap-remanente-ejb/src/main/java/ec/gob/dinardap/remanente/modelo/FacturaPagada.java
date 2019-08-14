/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author christian.gaona
 */
@Entity
@Table(name = "factura_pagada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacturaPagada.findAll", query = "SELECT f FROM FacturaPagada f")
    , @NamedQuery(name = "FacturaPagada.findByFacturaPagadaId", query = "SELECT f FROM FacturaPagada f WHERE f.facturaPagadaId = :facturaPagadaId")
    , @NamedQuery(name = "FacturaPagada.findByNumero", query = "SELECT f FROM FacturaPagada f WHERE f.numero = :numero")
    , @NamedQuery(name = "FacturaPagada.findByTipo", query = "SELECT f FROM FacturaPagada f WHERE f.tipo = :tipo")
    , @NamedQuery(name = "FacturaPagada.findByDetalle", query = "SELECT f FROM FacturaPagada f WHERE f.detalle = :detalle")
    , @NamedQuery(name = "FacturaPagada.findByValor", query = "SELECT f FROM FacturaPagada f WHERE f.valor = :valor")
    , @NamedQuery(name = "FacturaPagada.findByFechaRegistro", query = "SELECT f FROM FacturaPagada f WHERE f.fechaRegistro = :fechaRegistro")})
public class FacturaPagada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "factura_pagada_id")
    private Integer facturaPagadaId;
    @Size(max = 10)
    @Column(name = "numero")
    private String numero;
    @Size(max = 50)
    @Column(name = "tipo")
    private String tipo;
    @Size(max = 300)
    @Column(name = "detalle")
    private String detalle;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
    @JoinColumn(name = "transaccion_id", referencedColumnName = "transaccion_id")
    @ManyToOne
    private Transaccion transaccionId;

    public FacturaPagada() {
    }

    public FacturaPagada(Integer facturaPagadaId) {
        this.facturaPagadaId = facturaPagadaId;
    }

    public Integer getFacturaPagadaId() {
        return facturaPagadaId;
    }

    public void setFacturaPagadaId(Integer facturaPagadaId) {
        this.facturaPagadaId = facturaPagadaId;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Transaccion getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(Transaccion transaccionId) {
        this.transaccionId = transaccionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facturaPagadaId != null ? facturaPagadaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturaPagada)) {
            return false;
        }
        FacturaPagada other = (FacturaPagada) object;
        if ((this.facturaPagadaId == null && other.facturaPagadaId != null) || (this.facturaPagadaId != null && !this.facturaPagadaId.equals(other.facturaPagadaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.dinardap.remanente.modelo.FacturaPagada[ facturaPagadaId=" + facturaPagadaId + " ]";
    }
    
}

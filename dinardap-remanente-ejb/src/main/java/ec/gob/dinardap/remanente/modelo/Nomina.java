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
@Table(name = "nomina")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nomina.findAll", query = "SELECT n FROM Nomina n")
    , @NamedQuery(name = "Nomina.findByNominaId", query = "SELECT n FROM Nomina n WHERE n.nominaId = :nominaId")
    , @NamedQuery(name = "Nomina.findByNomNombres", query = "SELECT n FROM Nomina n WHERE n.nomNombres = :nomNombres")
    , @NamedQuery(name = "Nomina.findByCargo", query = "SELECT n FROM Nomina n WHERE n.cargo = :cargo")
    , @NamedQuery(name = "Nomina.findByRemuneracion", query = "SELECT n FROM Nomina n WHERE n.remuneracion = :remuneracion")
    , @NamedQuery(name = "Nomina.findByAportePatronal", query = "SELECT n FROM Nomina n WHERE n.aportePatronal = :aportePatronal")
    , @NamedQuery(name = "Nomina.findByImpuestoRenta", query = "SELECT n FROM Nomina n WHERE n.impuestoRenta = :impuestoRenta")
    , @NamedQuery(name = "Nomina.findByFondosReserva", query = "SELECT n FROM Nomina n WHERE n.fondosReserva = :fondosReserva")
    , @NamedQuery(name = "Nomina.findByDecimoTercero", query = "SELECT n FROM Nomina n WHERE n.decimoTercero = :decimoTercero")
    , @NamedQuery(name = "Nomina.findByDecimoCuarto", query = "SELECT n FROM Nomina n WHERE n.decimoCuarto = :decimoCuarto")
    , @NamedQuery(name = "Nomina.findByTotalDesc", query = "SELECT n FROM Nomina n WHERE n.totalDesc = :totalDesc")
    , @NamedQuery(name = "Nomina.findByLiquidoRecibir", query = "SELECT n FROM Nomina n WHERE n.liquidoRecibir = :liquidoRecibir")
    , @NamedQuery(name = "Nomina.findByFechaRegistro", query = "SELECT n FROM Nomina n WHERE n.fechaRegistro = :fechaRegistro")})
public class Nomina implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nomina_id")
    private Integer nominaId;
    @Size(max = 300)
    @Column(name = "nom_nombres")
    private String nomNombres;
    @Size(max = 300)
    @Column(name = "cargo")
    private String cargo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "remuneracion")
    private Double remuneracion;
    @Column(name = "aporte_patronal")
    private Double aportePatronal;
    @Column(name = "impuesto_renta")
    private Double impuestoRenta;
    @Column(name = "fondos_reserva")
    private Double fondosReserva;
    @Column(name = "decimo_tercero")
    private Double decimoTercero;
    @Column(name = "decimo_cuarto")
    private Double decimoCuarto;
    @Column(name = "total_desc")
    private Double totalDesc;
    @Column(name = "liquido_recibir")
    private Double liquidoRecibir;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
    @JoinColumn(name = "transaccion_id", referencedColumnName = "transaccion_id")
    @ManyToOne
    private Transaccion transaccionId;

    public Nomina() {
    }

    public Nomina(Integer nominaId) {
        this.nominaId = nominaId;
    }

    public Integer getNominaId() {
        return nominaId;
    }

    public void setNominaId(Integer nominaId) {
        this.nominaId = nominaId;
    }

    public String getNomNombres() {
        return nomNombres;
    }

    public void setNomNombres(String nomNombres) {
        this.nomNombres = nomNombres;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Double getRemuneracion() {
        return remuneracion;
    }

    public void setRemuneracion(Double remuneracion) {
        this.remuneracion = remuneracion;
    }

    public Double getAportePatronal() {
        return aportePatronal;
    }

    public void setAportePatronal(Double aportePatronal) {
        this.aportePatronal = aportePatronal;
    }

    public Double getImpuestoRenta() {
        return impuestoRenta;
    }

    public void setImpuestoRenta(Double impuestoRenta) {
        this.impuestoRenta = impuestoRenta;
    }

    public Double getFondosReserva() {
        return fondosReserva;
    }

    public void setFondosReserva(Double fondosReserva) {
        this.fondosReserva = fondosReserva;
    }

    public Double getDecimoTercero() {
        return decimoTercero;
    }

    public void setDecimoTercero(Double decimoTercero) {
        this.decimoTercero = decimoTercero;
    }

    public Double getDecimoCuarto() {
        return decimoCuarto;
    }

    public void setDecimoCuarto(Double decimoCuarto) {
        this.decimoCuarto = decimoCuarto;
    }

    public Double getTotalDesc() {
        return totalDesc;
    }

    public void setTotalDesc(Double totalDesc) {
        this.totalDesc = totalDesc;
    }

    public Double getLiquidoRecibir() {
        return liquidoRecibir;
    }

    public void setLiquidoRecibir(Double liquidoRecibir) {
        this.liquidoRecibir = liquidoRecibir;
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
        hash += (nominaId != null ? nominaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nomina)) {
            return false;
        }
        Nomina other = (Nomina) object;
        if ((this.nominaId == null && other.nominaId != null) || (this.nominaId != null && !this.nominaId.equals(other.nominaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.dinardap.remanente.modelo.Nomina[ nominaId=" + nominaId + " ]";
    }
    
}

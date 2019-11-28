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
@Table(name = "nomina")
@NamedQueries({
    @NamedQuery(name = "Nomina.findAll", query = "SELECT n FROM Nomina n")
    ,
    @NamedQuery(name = "Nomina.findByNominaId", query = "SELECT n FROM Nomina n WHERE n.nominaId = :nominaId")
    ,
    @NamedQuery(name = "Nomina.findByNombre", query = "SELECT n FROM Nomina n WHERE n.nombre = :nombre")
    ,
    @NamedQuery(name = "Nomina.findByCargo", query = "SELECT n FROM Nomina n WHERE n.cargo = :cargo")
    ,
    @NamedQuery(name = "Nomina.findByRemuneracion", query = "SELECT n FROM Nomina n WHERE n.remuneracion = :remuneracion")
    ,
    @NamedQuery(name = "Nomina.findByAportePatronal", query = "SELECT n FROM Nomina n WHERE n.aportePatronal = :aportePatronal")
    ,
    @NamedQuery(name = "Nomina.findByImpuestoRenta", query = "SELECT n FROM Nomina n WHERE n.impuestoRenta = :impuestoRenta")
    ,
    @NamedQuery(name = "Nomina.findByFondosReserva", query = "SELECT n FROM Nomina n WHERE n.fondosReserva = :fondosReserva")
    ,
    @NamedQuery(name = "Nomina.findByDecimoTercero", query = "SELECT n FROM Nomina n WHERE n.decimoTercero = :decimoTercero")
    ,
    @NamedQuery(name = "Nomina.findByDecimoCuarto", query = "SELECT n FROM Nomina n WHERE n.decimoCuarto = :decimoCuarto")
    ,
    @NamedQuery(name = "Nomina.findByTotalDesc", query = "SELECT n FROM Nomina n WHERE n.totalDesc = :totalDesc")
    ,
    @NamedQuery(name = "Nomina.findByLiquidoRecibir", query = "SELECT n FROM Nomina n WHERE n.liquidoRecibir = :liquidoRecibir")
    ,
    @NamedQuery(name = "Nomina.findByFechaRegistro", query = "SELECT n FROM Nomina n WHERE n.fechaRegistro = :fechaRegistro")})
public class Nomina implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "NOMINA_GENERATOR", sequenceName = "nomina_nomina_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOMINA_GENERATOR")
    @Column(name = "nomina_id")
    private Integer nominaId;

    @Size(max = 300)
    @Column(name = "nombre")
    private String nombre;

    @Size(max = 300)
    @Column(name = "cargo")
    private String cargo;

    @Column(name = "remuneracion")
    private BigDecimal remuneracion;

    @Column(name = "aporte_patronal")
    private BigDecimal aportePatronal;

    @Column(name = "impuesto_renta")
    private BigDecimal impuestoRenta;

    @Column(name = "fondos_reserva")
    private BigDecimal fondosReserva;

    @Column(name = "decimo_tercero")
    private BigDecimal decimoTercero;

    @Column(name = "decimo_cuarto")
    private BigDecimal decimoCuarto;

    @Column(name = "total_desc")
    private BigDecimal totalDesc;

    @Column(name = "liquido_recibir")
    private BigDecimal liquidoRecibir;

    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public BigDecimal getRemuneracion() {
        return remuneracion;
    }

    public void setRemuneracion(BigDecimal remuneracion) {
        this.remuneracion = remuneracion;
    }

    public BigDecimal getAportePatronal() {
        return aportePatronal;
    }

    public void setAportePatronal(BigDecimal aportePatronal) {
        this.aportePatronal = aportePatronal;
    }

    public BigDecimal getImpuestoRenta() {
        return impuestoRenta;
    }

    public void setImpuestoRenta(BigDecimal impuestoRenta) {
        this.impuestoRenta = impuestoRenta;
    }

    public BigDecimal getFondosReserva() {
        return fondosReserva;
    }

    public void setFondosReserva(BigDecimal fondosReserva) {
        this.fondosReserva = fondosReserva;
    }

    public BigDecimal getDecimoTercero() {
        return decimoTercero;
    }

    public void setDecimoTercero(BigDecimal decimoTercero) {
        this.decimoTercero = decimoTercero;
    }

    public BigDecimal getDecimoCuarto() {
        return decimoCuarto;
    }

    public void setDecimoCuarto(BigDecimal decimoCuarto) {
        this.decimoCuarto = decimoCuarto;
    }

    public BigDecimal getTotalDesc() {
        return totalDesc;
    }

    public void setTotalDesc(BigDecimal totalDesc) {
        this.totalDesc = totalDesc;
    }

    public BigDecimal getLiquidoRecibir() {
        return liquidoRecibir;
    }

    public void setLiquidoRecibir(BigDecimal liquidoRecibir) {
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
        return "Nomina{" + "nominaId=" + nominaId + ", nombre=" + nombre + ", cargo=" + cargo + ", remuneracion=" + remuneracion + ", aportePatronal=" + aportePatronal + ", impuestoRenta=" + impuestoRenta + ", fondosReserva=" + fondosReserva + ", decimoTercero=" + decimoTercero + ", decimoCuarto=" + decimoCuarto + ", totalDesc=" + totalDesc + ", liquidoRecibir=" + liquidoRecibir + ", fechaRegistro=" + fechaRegistro + ", transaccionId=" + transaccionId + '}';
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import ec.gob.dinardap.seguridad.modelo.Usuario;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
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
@Table(name = "bandeja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bandeja.findAll", query = "SELECT b FROM Bandeja b")
    , @NamedQuery(name = "Bandeja.findByBandejaId", query = "SELECT b FROM Bandeja b WHERE b.bandejaId = :bandejaId")
    , @NamedQuery(name = "Bandeja.findByDescripcion", query = "SELECT b FROM Bandeja b WHERE b.descripcion = :descripcion")
    , @NamedQuery(name = "Bandeja.findByTipo", query = "SELECT b FROM Bandeja b WHERE b.tipo = :tipo")})
public class Bandeja implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "BANDEJA_GENERATOR", sequenceName = "bandeja_bandeja_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANDEJA_GENERATOR")
    @Column(name = "bandeja_id")
    private Integer bandejaId;

    @Size(max = 300)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 50)
    @Column(name = "tipo")
    private String tipo;

    @Column(name = "leido")
    private Boolean leido;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "fecha_leido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaLeido;

    @JoinColumns({
        @JoinColumn(name = "remanente_cuatrimestral_id", referencedColumnName = "remanente_cuatrimestral_id"),
        @JoinColumn(name = "remanente_anual_id", referencedColumnName = "remanente_anual_id"),
        @JoinColumn(name = "institucion_id", referencedColumnName = "institucion_id")})
    @ManyToOne
    private RemanenteCuatrimestral remanenteCuatrimestral;
    
    @JoinColumn(name = "remanente_mensual_id", referencedColumnName = "remanente_mensual_id")
    @ManyToOne
    private RemanenteMensual remanenteMensual;
    
    @JoinColumn(name = "usuario_asignado_id", referencedColumnName = "usuario_id")
    @ManyToOne
    private Usuario usuarioAsignado;
    
    @JoinColumn(name = "usuario_solicitante_id", referencedColumnName = "usuario_id")
    @ManyToOne
    private Usuario usuarioSolicitante;

    public Bandeja() {
    }

    public Bandeja(Integer bandejaId) {
        this.bandejaId = bandejaId;
    }

    public Integer getBandejaId() {
        return bandejaId;
    }

    public void setBandejaId(Integer bandejaId) {
        this.bandejaId = bandejaId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaLeido() {
        return fechaLeido;
    }

    public void setFechaLeido(Date fechaLeido) {
        this.fechaLeido = fechaLeido;
    }

    public RemanenteCuatrimestral getRemanenteCuatrimestral() {
        return remanenteCuatrimestral;
    }

    public void setRemanenteCuatrimestral(RemanenteCuatrimestral remanenteCuatrimestral) {
        this.remanenteCuatrimestral = remanenteCuatrimestral;
    }

    public RemanenteMensual getRemanenteMensual() {
        return remanenteMensual;
    }

    public void setRemanenteMensual(RemanenteMensual remanenteMensual) {
        this.remanenteMensual = remanenteMensual;
    }

    public Usuario getUsuarioAsignado() {
        return usuarioAsignado;
    }

    public void setUsuarioAsignado(Usuario usuarioAsignado) {
        this.usuarioAsignado = usuarioAsignado;
    }

    public Usuario getUsuarioSolicitante() {
        return usuarioSolicitante;
    }

    public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }
    
    
}

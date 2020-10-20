/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import ec.gob.dinardap.seguridad.modelo.Institucion;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Cristian
 */
@Entity
@Table(name = "remanente_historico")
@NamedQueries({
    @NamedQuery(name = "RemanenteHistorico.findAll", query = "SELECT r FROM RemanenteHistorico r"),
    @NamedQuery(name = "RemanenteHistorico.findByRemanenteHistoricoId", query = "SELECT r FROM RemanenteHistorico r WHERE r.remanenteHistoricoId = :remanenteHistoricoId"),
    @NamedQuery(name = "RemanenteHistorico.findByInstitucion", query = "SELECT r FROM RemanenteHistorico r WHERE r.institucion = :institucion"),
    @NamedQuery(name = "RemanenteHistorico.findByAnio", query = "SELECT r FROM RemanenteHistorico r WHERE r.anio = :anio"),
    @NamedQuery(name = "RemanenteHistorico.findByCuatrimestre", query = "SELECT r FROM RemanenteHistorico r WHERE r.cuatrimestre = :cuatrimestre"),
    @NamedQuery(name = "RemanenteHistorico.findByValor", query = "SELECT r FROM RemanenteHistorico r WHERE r.valor = :valor"),
    @NamedQuery(name = "RemanenteHistorico.findByOficioGadRegistro", query = "SELECT r FROM RemanenteHistorico r WHERE r.oficioGadRegistro = :oficioGadRegistro"),
    @NamedQuery(name = "RemanenteHistorico.findByFechaOficioGadRegistro", query = "SELECT r FROM RemanenteHistorico r WHERE r.fechaOficioGadRegistro = :fechaOficioGadRegistro"),
    @NamedQuery(name = "RemanenteHistorico.findByMemorandoRegional", query = "SELECT r FROM RemanenteHistorico r WHERE r.memorandoRegional = :memorandoRegional"),
    @NamedQuery(name = "RemanenteHistorico.findByFechaMemorandoRegional", query = "SELECT r FROM RemanenteHistorico r WHERE r.fechaMemorandoRegional = :fechaMemorandoRegional"),
    @NamedQuery(name = "RemanenteHistorico.findByMemorandoCgrs", query = "SELECT r FROM RemanenteHistorico r WHERE r.memorandoCgrs = :memorandoCgrs"),
    @NamedQuery(name = "RemanenteHistorico.findByFechaMemorandoCgrs", query = "SELECT r FROM RemanenteHistorico r WHERE r.fechaMemorandoCgrs = :fechaMemorandoCgrs"),
    @NamedQuery(name = "RemanenteHistorico.findByMemorandoFinanciero", query = "SELECT r FROM RemanenteHistorico r WHERE r.memorandoFinanciero = :memorandoFinanciero"),
    @NamedQuery(name = "RemanenteHistorico.findByFechaMemorandoFinanciero", query = "SELECT r FROM RemanenteHistorico r WHERE r.fechaMemorandoFinanciero = :fechaMemorandoFinanciero")})
public class RemanenteHistorico implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name = "REMANENTE_HISTORICO_GENERATOR", sequenceName = "remanente_historico_remanente_historico_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REMANENTE_HISTORICO_GENERATOR")
    @Column(name = "remanente_historico_id")
    private Integer remanenteHistoricoId;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "institucion_id", referencedColumnName = "institucion_id")
    private Institucion institucion;
    
    @Column(name = "anio")
    private Integer anio;
    
    @Column(name = "cuatrimestre")
    private Short cuatrimestre;

    @Column(name = "valor")
    private BigDecimal valor;
    
    @Column(name = "oficio_gad_registro", length = 50)
    private String oficioGadRegistro;
    
    @Column(name = "fecha_oficio_gad_registro")
    @Temporal(TemporalType.DATE)
    private Date fechaOficioGadRegistro;
    
    @Column(name = "memorando_regional", length = 50)
    private String memorandoRegional;
    
    @Column(name = "fecha_memorando_regional")
    @Temporal(TemporalType.DATE)
    private Date fechaMemorandoRegional;
    
    @Column(name = "memorando_cgrs", length = 50)
    private String memorandoCgrs;
    
    @Column(name = "fecha_memorando_cgrs")
    @Temporal(TemporalType.DATE)
    private Date fechaMemorandoCgrs;
    
    @Column(name = "memorando_financiero", length = 50)
    private String memorandoFinanciero;
    
    @Column(name = "fecha_memorando_financiero")
    @Temporal(TemporalType.DATE)
    private Date fechaMemorandoFinanciero;

    public RemanenteHistorico() {
    }

    public RemanenteHistorico(Integer remanenteHistoricoId) {
        this.remanenteHistoricoId = remanenteHistoricoId;
    }

    public Integer getRemanenteHistoricoId() {
        return remanenteHistoricoId;
    }

    public void setRemanenteHistoricoId(Integer remanenteHistoricoId) {
        this.remanenteHistoricoId = remanenteHistoricoId;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Short getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(Short cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getOficioGadRegistro() {
        return oficioGadRegistro;
    }

    public void setOficioGadRegistro(String oficioGadRegistro) {
        this.oficioGadRegistro = oficioGadRegistro;
    }

    public Date getFechaOficioGadRegistro() {
        return fechaOficioGadRegistro;
    }

    public void setFechaOficioGadRegistro(Date fechaOficioGadRegistro) {
        this.fechaOficioGadRegistro = fechaOficioGadRegistro;
    }

    public String getMemorandoRegional() {
        return memorandoRegional;
    }

    public void setMemorandoRegional(String memorandoRegional) {
        this.memorandoRegional = memorandoRegional;
    }

    public Date getFechaMemorandoRegional() {
        return fechaMemorandoRegional;
    }

    public void setFechaMemorandoRegional(Date fechaMemorandoRegional) {
        this.fechaMemorandoRegional = fechaMemorandoRegional;
    }

    public String getMemorandoCgrs() {
        return memorandoCgrs;
    }

    public void setMemorandoCgrs(String memorandoCgrs) {
        this.memorandoCgrs = memorandoCgrs;
    }

    public Date getFechaMemorandoCgrs() {
        return fechaMemorandoCgrs;
    }

    public void setFechaMemorandoCgrs(Date fechaMemorandoCgrs) {
        this.fechaMemorandoCgrs = fechaMemorandoCgrs;
    }

    public String getMemorandoFinanciero() {
        return memorandoFinanciero;
    }

    public void setMemorandoFinanciero(String memorandoFinanciero) {
        this.memorandoFinanciero = memorandoFinanciero;
    }

    public Date getFechaMemorandoFinanciero() {
        return fechaMemorandoFinanciero;
    }

    public void setFechaMemorandoFinanciero(Date fechaMemorandoFinanciero) {
        this.fechaMemorandoFinanciero = fechaMemorandoFinanciero;
    }

}

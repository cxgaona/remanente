/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import java.io.Serializable;
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
 * @author Cristian
 */
@Entity
@Table(name = "informe_cumplimiento", schema = "ec_dinardap_inventario")
@NamedQueries({
    @NamedQuery(name = "InformeCumplimiento.findAll", query = "SELECT i FROM InformeCumplimiento i"),
    @NamedQuery(name = "InformeCumplimiento.findByInformeCumplimientoId", query = "SELECT i FROM InformeCumplimiento i WHERE i.informeCumplimientoId = :informeCumplimientoId"),
    @NamedQuery(name = "InformeCumplimiento.findByInstitucion", query = "SELECT i FROM InformeCumplimiento i WHERE i.institucion = :institucion"),
    @NamedQuery(name = "InformeCumplimiento.findByUsuario", query = "SELECT i FROM InformeCumplimiento i WHERE i.usuario = :usuario"),
    @NamedQuery(name = "InformeCumplimiento.findByAnio", query = "SELECT i FROM InformeCumplimiento i WHERE i.anio = :anio"),
    @NamedQuery(name = "InformeCumplimiento.findByUrlArchivo", query = "SELECT i FROM InformeCumplimiento i WHERE i.urlArchivo = :urlArchivo"),
    @NamedQuery(name = "InformeCumplimiento.findByFechaRegistro", query = "SELECT i FROM InformeCumplimiento i WHERE i.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "InformeCumplimiento.findByTipo", query = "SELECT i FROM InformeCumplimiento i WHERE i.tipo = :tipo"),
    @NamedQuery(name = "InformeCumplimiento.findByEstado", query = "SELECT i FROM InformeCumplimiento i WHERE i.estado = :estado")})
public class InformeCumplimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "INFORME_CUMPLIMIENTO_GENERATOR", sequenceName = "informe_cumplimiento_informe_cumplimiento_id_seq", allocationSize = 1, schema = "ec_dinardap_inventario")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INFORME_CUMPLIMIENTO_GENERATOR")
    @Column(name = "informe_cumplimiento_id")
    private Integer informeCumplimientoId;
    
    @ManyToOne
    @JoinColumn(name = "institucion_id", referencedColumnName = "institucion_id")
    private Institucion institucion;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id")
    private Usuario usuario;
    
    @Column(name = "anio")
    private Integer anio;
    
    @Size(max = 256)
    @Column(name = "url_archivo")
    private String urlArchivo;
    
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    
    @Size(max = 3)
    @Column(name = "tipo")
    private String tipo;
    
    @Column(name = "estado")
    private Short estado;

    public InformeCumplimiento() {
    }

    public InformeCumplimiento(Integer informeCumplimientoId) {
        this.informeCumplimientoId = informeCumplimientoId;
    }

    public Integer getInformeCumplimientoId() {
        return informeCumplimientoId;
    }

    public void setInformeCumplimientoId(Integer informeCumplimientoId) {
        this.informeCumplimientoId = informeCumplimientoId;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getUrlArchivo() {
        return urlArchivo;
    }

    public void setUrlArchivo(String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.Size;

/**
 *
 * @author christian.gaona
 */
@Entity
@Table(name = "institucion_requerida")
@NamedQueries({
    @NamedQuery(name = "InstitucionRequerida.findAll", query = "SELECT i FROM InstitucionRequerida i")
    , @NamedQuery(name = "InstitucionRequerida.findByInstitucionId", query = "SELECT i FROM InstitucionRequerida i WHERE i.institucionId = :institucionId")
    , @NamedQuery(name = "InstitucionRequerida.findByRuc", query = "SELECT i FROM InstitucionRequerida i WHERE i.ruc = :ruc")
    , @NamedQuery(name = "InstitucionRequerida.findByNombre", query = "SELECT i FROM InstitucionRequerida i WHERE i.nombre = :nombre")
    , @NamedQuery(name = "InstitucionRequerida.findByEmail", query = "SELECT i FROM InstitucionRequerida i WHERE i.email = :email")
    , @NamedQuery(name = "InstitucionRequerida.findByProvinciaCanton", query = "SELECT i FROM InstitucionRequerida i WHERE i.provinciaCanton = :provinciaCanton")
    , @NamedQuery(name = "InstitucionRequerida.findByTipo", query = "SELECT i FROM InstitucionRequerida i WHERE i.tipo = :tipo")})
public class InstitucionRequerida implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "INSTITUCION_REQUERIDA_GENERATOR", sequenceName = "institucion_requerida_institucion_id_seq",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSTITUCION_REQUERIDA_GENERATOR")
    @Column(name = "institucion_id")
    private Integer institucionId;

    @Column(name = "ruc", length = 13)
    private String ruc;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "email", length = 300)
    private String email;

    @Column(name = "provincia_canton", length = 100)
    private String provinciaCanton;

    @Column(name = "tipo", length = 50)
    private String tipo;

    //Dirección Regional//
    @OneToMany(mappedBy = "direccionRegional")
    private List<InstitucionRequerida> direccionRegionalList;

    @ManyToOne
    @JoinColumn(name = "direccion_regional_id", referencedColumnName = "institucion_id")
    private InstitucionRequerida direccionRegional;
    //Dirección Regional//

    //GAD//
    @OneToMany(mappedBy = "gad")
    private List<InstitucionRequerida> gadList;

    @ManyToOne
    @JoinColumn(name = "gad_id", referencedColumnName = "institucion_id")
    private InstitucionRequerida gad;
    //GAD//

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "institucionRequerida")
    private List<RemanenteAnual> remanenteAnualList;

    @OneToMany(mappedBy = "institucionId")
    private List<Usuario> usuarioList;

    public InstitucionRequerida() {
    }

    public InstitucionRequerida(Integer institucionId) {
        this.institucionId = institucionId;
    }

    public Integer getInstitucionId() {
        return institucionId;
    }

    public void setInstitucionId(Integer institucionId) {
        this.institucionId = institucionId;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvinciaCanton() {
        return provinciaCanton;
    }

    public void setProvinciaCanton(String provinciaCanton) {
        this.provinciaCanton = provinciaCanton;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<InstitucionRequerida> getDireccionRegionalList() {
        return direccionRegionalList;
    }

    public void setDireccionRegionalList(List<InstitucionRequerida> direccionRegionalList) {
        this.direccionRegionalList = direccionRegionalList;
    }

    public InstitucionRequerida getDireccionRegional() {
        return direccionRegional;
    }

    public void setDireccionRegional(InstitucionRequerida direccionRegional) {
        this.direccionRegional = direccionRegional;
    }

    public List<InstitucionRequerida> getGadList() {
        return gadList;
    }

    public void setGadList(List<InstitucionRequerida> gadList) {
        this.gadList = gadList;
    }

    public InstitucionRequerida getGad() {
        return gad;
    }

    public void setGad(InstitucionRequerida gad) {
        this.gad = gad;
    }

    public List<RemanenteAnual> getRemanenteAnualList() {
        return remanenteAnualList;
    }

    public void setRemanenteAnualList(List<RemanenteAnual> remanenteAnualList) {
        this.remanenteAnualList = remanenteAnualList;
    }

    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (institucionId != null ? institucionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InstitucionRequerida)) {
            return false;
        }
        InstitucionRequerida other = (InstitucionRequerida) object;
        if ((this.institucionId == null && other.institucionId != null) || (this.institucionId != null && !this.institucionId.equals(other.institucionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InstitucionRequerida{" + "institucionId=" + institucionId + ", ruc=" + ruc + ", nombre=" + nombre + ", email=" + email + ", provinciaCanton=" + provinciaCanton + ", tipo=" + tipo + ", direccionRegionalList=" + direccionRegionalList + ", direccionRegional=" + direccionRegional + ", gadList=" + gadList + ", gad=" + gad + ", remanenteAnualList=" + remanenteAnualList + ", usuarioList=" + usuarioList + '}';
    }

}

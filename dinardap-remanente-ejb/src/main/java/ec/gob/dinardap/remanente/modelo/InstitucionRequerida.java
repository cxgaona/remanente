/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import ec.gob.dinardap.seguridad.modelo.Institucion;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author christian.gaona
 */
@Entity
@Table(name = "institucion_requerida")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstitucionRequerida.findAll", query = "SELECT i FROM InstitucionRequerida i")
    , @NamedQuery(name = "InstitucionRequerida.findByInstitucionId", query = "SELECT i FROM InstitucionRequerida i WHERE i.institucionId = :institucionId")
    , @NamedQuery(name = "InstitucionRequerida.findByRuc", query = "SELECT i FROM InstitucionRequerida i WHERE i.ruc = :ruc")
    , @NamedQuery(name = "InstitucionRequerida.findByEmail", query = "SELECT i FROM InstitucionRequerida i WHERE i.email = :email")
    , @NamedQuery(name = "InstitucionRequerida.findByProvinciaCanton", query = "SELECT i FROM InstitucionRequerida i WHERE i.provinciaCanton = :provinciaCanton")
    , @NamedQuery(name = "InstitucionRequerida.findByTipo", query = "SELECT i FROM InstitucionRequerida i WHERE i.tipo = :tipo")})
public class InstitucionRequerida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @JoinColumn(name = "institucion_id")
    @ManyToOne
    private Institucion institucionId;

    @Column(name = "ruc", length = 13)
    private String ruc;

    @Column(name = "email", length = 300)
    private String email;

    @Column(name = "provincia_canton", length = 300)
    private String provinciaCanton;

    @Column(name = "tipo", length = 50)
    private String tipo;

    //Dirección Regional//
    @OneToMany(mappedBy = "institucionDireccionRegional")
    private List<InstitucionRequerida> institucionDireccionRegionalList;
    @ManyToOne
    @JoinColumn(name = "institucion_direccion_regional_id",referencedColumnName = "institucion_id")
    private InstitucionRequerida institucionDireccionRegional;
    //Dirección Regional//

    //GAD//
    @OneToMany(mappedBy = "institucionGad")
    private List<InstitucionRequerida> institucionGadList;
    @ManyToOne
    @JoinColumn(name = "institucion_gad_id",referencedColumnName = "institucion_id")
    private InstitucionRequerida institucionGad;
    //GAD//

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "institucionRequerida")
    private List<RemanenteAnual> remanenteAnualList;
    @OneToMany(mappedBy = "institucionId")
    private List<Usuario> usuarioList;

    public InstitucionRequerida() {
    }

    public Institucion getInstitucionId() {
        return institucionId;
    }

    public void setInstitucionId(Institucion institucionId) {
        this.institucionId = institucionId;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
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

    public List<InstitucionRequerida> getInstitucionDireccionRegionalList() {
        return institucionDireccionRegionalList;
    }

    public void setInstitucionDireccionRegionalList(List<InstitucionRequerida> institucionDireccionRegionalList) {
        this.institucionDireccionRegionalList = institucionDireccionRegionalList;
    }

    public InstitucionRequerida getInstitucionDireccionRegional() {
        return institucionDireccionRegional;
    }

    public void setInstitucionDireccionRegional(InstitucionRequerida institucionDireccionRegional) {
        this.institucionDireccionRegional = institucionDireccionRegional;
    }

    public List<InstitucionRequerida> getInstitucionGadList() {
        return institucionGadList;
    }

    public void setInstitucionGadList(List<InstitucionRequerida> institucionGadList) {
        this.institucionGadList = institucionGadList;
    }

    public InstitucionRequerida getInstitucionGad() {
        return institucionGad;
    }

    public void setInstitucionGad(InstitucionRequerida institucionGad) {
        this.institucionGad = institucionGad;
    }

    @XmlTransient
    public List<RemanenteAnual> getRemanenteAnualList() {
        return remanenteAnualList;
    }

    public void setRemanenteAnualList(List<RemanenteAnual> remanenteAnualList) {
        this.remanenteAnualList = remanenteAnualList;
    }

    @XmlTransient
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
        return "InstitucionRequerida{" + "institucionId=" + institucionId + ", ruc=" + ruc + ", email=" + email + ", provinciaCanton=" + provinciaCanton + ", tipo=" + tipo + ", institucionDireccionRegionalList=" + institucionDireccionRegionalList + ", institucionDireccionRegional=" + institucionDireccionRegional + ", institucionGadList=" + institucionGadList + ", institucionGad=" + institucionGad + ", remanenteAnualList=" + remanenteAnualList + ", usuarioList=" + usuarioList + '}';
    }

}

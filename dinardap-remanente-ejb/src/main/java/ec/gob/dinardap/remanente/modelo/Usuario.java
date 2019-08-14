/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author christian.gaona
 */
@Entity
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
    , @NamedQuery(name = "Usuario.findByUsuarioId", query = "SELECT u FROM Usuario u WHERE u.usuarioId = :usuarioId")
    , @NamedQuery(name = "Usuario.findByNombre", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre")
    , @NamedQuery(name = "Usuario.findByUsuario", query = "SELECT u FROM Usuario u WHERE u.usuario = :usuario")
    , @NamedQuery(name = "Usuario.findByContrasena", query = "SELECT u FROM Usuario u WHERE u.contrasena = :contrasena")
    , @NamedQuery(name = "Usuario.findByRegistrador", query = "SELECT u FROM Usuario u WHERE u.registrador = :registrador")
    , @NamedQuery(name = "Usuario.findByValidador", query = "SELECT u FROM Usuario u WHERE u.validador = :validador")
    , @NamedQuery(name = "Usuario.findByVerificador", query = "SELECT u FROM Usuario u WHERE u.verificador = :verificador")
    , @NamedQuery(name = "Usuario.findByAdministrador", query = "SELECT u FROM Usuario u WHERE u.administrador = :administrador")
    , @NamedQuery(name = "Usuario.findByEstado", query = "SELECT u FROM Usuario u WHERE u.estado = :estado")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "usuario_id")
    private Integer usuarioId;
    @Size(max = 150)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 50)
    @Column(name = "usuario")
    private String usuario;
    @Size(max = 500)
    @Column(name = "contrasena")
    private String contrasena;
    @Column(name = "registrador")
    private Boolean registrador;
    @Column(name = "validador")
    private Boolean validador;
    @Column(name = "verificador")
    private Boolean verificador;
    @Column(name = "administrador")
    private Boolean administrador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "estado")
    private String estado;
    @OneToMany(mappedBy = "usuarioAsignadoId")
    private List<Bandeja> bandejaList;
    @OneToMany(mappedBy = "usuarioSolicitanteId")
    private List<Bandeja> bandejaList1;
    @JoinColumn(name = "institucion_id", referencedColumnName = "institucion_id")
    @ManyToOne
    private InstitucionRequerida institucionId;

    public Usuario() {
    }

    public Usuario(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Usuario(Integer usuarioId, String estado) {
        this.usuarioId = usuarioId;
        this.estado = estado;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Boolean getRegistrador() {
        return registrador;
    }

    public void setRegistrador(Boolean registrador) {
        this.registrador = registrador;
    }

    public Boolean getValidador() {
        return validador;
    }

    public void setValidador(Boolean validador) {
        this.validador = validador;
    }

    public Boolean getVerificador() {
        return verificador;
    }

    public void setVerificador(Boolean verificador) {
        this.verificador = verificador;
    }

    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Bandeja> getBandejaList() {
        return bandejaList;
    }

    public void setBandejaList(List<Bandeja> bandejaList) {
        this.bandejaList = bandejaList;
    }

    @XmlTransient
    public List<Bandeja> getBandejaList1() {
        return bandejaList1;
    }

    public void setBandejaList1(List<Bandeja> bandejaList1) {
        this.bandejaList1 = bandejaList1;
    }

    public InstitucionRequerida getInstitucionId() {
        return institucionId;
    }

    public void setInstitucionId(InstitucionRequerida institucionId) {
        this.institucionId = institucionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioId != null ? usuarioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.usuarioId == null && other.usuarioId != null) || (this.usuarioId != null && !this.usuarioId.equals(other.usuarioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.dinardap.remanente.modelo.Usuario[ usuarioId=" + usuarioId + " ]";
    }
    
}

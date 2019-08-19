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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author christian.gaona
 */
@Entity
@Table(name = "catalogo_transaccion")
@NamedQueries({
    @NamedQuery(name = "CatalogoTransaccion.findAll", query = "SELECT c FROM CatalogoTransaccion c")
    , @NamedQuery(name = "CatalogoTransaccion.findByCatalogoTransaccionId", query = "SELECT c FROM CatalogoTransaccion c WHERE c.catalogoTransaccionId = :catalogoTransaccionId")
    , @NamedQuery(name = "CatalogoTransaccion.findByTipo", query = "SELECT c FROM CatalogoTransaccion c WHERE c.tipo = :tipo")
    , @NamedQuery(name = "CatalogoTransaccion.findByNombre", query = "SELECT c FROM CatalogoTransaccion c WHERE c.nombre = :nombre")})
public class CatalogoTransaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "catalogo_transaccion_id")
    private Integer catalogoTransaccionId;
    @Size(max = 20)
    @Column(name = "tipo")
    private String tipo;
    @Size(max = 100)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "catalogoTransaccionId")
    private List<Transaccion> transaccionList;

    public CatalogoTransaccion() {
    }

    public CatalogoTransaccion(Integer catalogoTransaccionId) {
        this.catalogoTransaccionId = catalogoTransaccionId;
    }

    public Integer getCatalogoTransaccionId() {
        return catalogoTransaccionId;
    }

    public void setCatalogoTransaccionId(Integer catalogoTransaccionId) {
        this.catalogoTransaccionId = catalogoTransaccionId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Transaccion> getTransaccionList() {
        return transaccionList;
    }

    public void setTransaccionList(List<Transaccion> transaccionList) {
        this.transaccionList = transaccionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (catalogoTransaccionId != null ? catalogoTransaccionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CatalogoTransaccion)) {
            return false;
        }
        CatalogoTransaccion other = (CatalogoTransaccion) object;
        if ((this.catalogoTransaccionId == null && other.catalogoTransaccionId != null) || (this.catalogoTransaccionId != null && !this.catalogoTransaccionId.equals(other.catalogoTransaccionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.dinardap.remanente.modelo.CatalogoTransaccion[ catalogoTransaccionId=" + catalogoTransaccionId + " ]";
    }
    
}

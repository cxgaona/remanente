/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Cristian
 */
@Entity
@Table(name = "tipo_libro", schema = "ec_dinardap_inventario")
@NamedQueries({
    @NamedQuery(name = "TipoLibro.findAll", query = "SELECT t FROM TipoLibro t"),
    @NamedQuery(name = "TipoLibro.findByTipoLibroId", query = "SELECT t FROM TipoLibro t WHERE t.tipoLibroId = :tipoLibroId"),
    @NamedQuery(name = "TipoLibro.findByNombre", query = "SELECT t FROM TipoLibro t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoLibro.findByDescripcion", query = "SELECT t FROM TipoLibro t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TipoLibro.findByEstado", query = "SELECT t FROM TipoLibro t WHERE t.estado = :estado")})
public class TipoLibro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "TIPO_LIBRO_GENERATOR", sequenceName = "tipo_libro_tipo_libro_id_seq", allocationSize = 1, schema = "ec_dinardap_inventario")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_LIBRO_GENERATOR")
    @Column(name = "tipo_libro_id")
    private Integer tipoLibroId;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;

    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Column(name = "estado")
    private short estado;

    @OneToMany(mappedBy = "tipoLibro")
    private List<Libro> libroList;

    public TipoLibro() {
    }

    public TipoLibro(Integer tipoLibroId) {
        this.tipoLibroId = tipoLibroId;
    }

    public Integer getTipoLibroId() {
        return tipoLibroId;
    }

    public void setTipoLibroId(Integer tipoLibroId) {
        this.tipoLibroId = tipoLibroId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
    }

    public List<Libro> getLibroList() {
        return libroList;
    }

    public void setLibroList(List<Libro> libroList) {
        this.libroList = libroList;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.modelo;

import ec.gob.dinardap.seguridad.modelo.Institucion;
import java.io.Serializable;
import java.util.List;
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

/**
 *
 * @author Cristian
 */
@Entity
@Table(name = "inventario_anual", schema = "ec_dinardap_inventario")
@NamedQueries({
    @NamedQuery(name = "InventarioAnual.findAll", query = "SELECT i FROM InventarioAnual i"),
    @NamedQuery(name = "InventarioAnual.findByInventarioAnualId", query = "SELECT i FROM InventarioAnual i WHERE i.inventarioAnualId = :inventarioAnualId"),
    @NamedQuery(name = "InventarioAnual.findByInstitucion", query = "SELECT i FROM InventarioAnual i WHERE i.institucion = :institucion"),
    @NamedQuery(name = "InventarioAnual.findByAnio", query = "SELECT i FROM InventarioAnual i WHERE i.anio = :anio")})
public class InventarioAnual implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "INVENTARIO_ANUAL_GENERATOR", sequenceName = "inventario_anual_inventario_anual_id_seq", allocationSize = 1, schema = "ec_dinardap_inventario")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVENTARIO_ANUAL_GENERATOR")
    @Column(name = "inventario_anual_id")
    private Integer inventarioAnualId;

    @ManyToOne
    @JoinColumn(name = "institucion_id", referencedColumnName = "institucion_id")
    private Institucion institucion;

    @Column(name = "anio")
    private Integer anio;
    
    @Column(name = "nombre_registrador")
    private String nombreRegistrador;

    @OneToMany(mappedBy = "inventarioAnual")
    private List<Libro> libroList;

    @OneToMany(mappedBy = "inventarioAnual")
    private List<EstadoInventarioAnual> estadoInventarioAnualList;

    public InventarioAnual() {
    }

    public InventarioAnual(Integer inventarioAnualId) {
        this.inventarioAnualId = inventarioAnualId;
    }

    public Integer getInventarioAnualId() {
        return inventarioAnualId;
    }

    public void setInventarioAnualId(Integer inventarioAnualId) {
        this.inventarioAnualId = inventarioAnualId;
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

    public String getNombreRegistrador() {
        return nombreRegistrador;
    }

    public void setNombreRegistrador(String nombreRegistrador) {
        this.nombreRegistrador = nombreRegistrador;
    }

    public List<Libro> getLibroList() {
        return libroList;
    }

    public void setLibroList(List<Libro> libroList) {
        this.libroList = libroList;
    }

    public List<EstadoInventarioAnual> getEstadoInventarioAnualList() {
        return estadoInventarioAnualList;
    }

    public void setEstadoInventarioAnualList(List<EstadoInventarioAnual> estadoInventarioAnualList) {
        this.estadoInventarioAnualList = estadoInventarioAnualList;
    }

}

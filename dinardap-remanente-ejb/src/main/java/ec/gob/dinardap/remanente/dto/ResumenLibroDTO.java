/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.dto;

/**
 *
 * @author christian.gaona
 */
public class ResumenLibroDTO {

    private String nombreRegistrador;
    private Integer totalLibros;
    private Integer totalPaginasLibros;

    public ResumenLibroDTO() {

    }

    public ResumenLibroDTO(String nombreRegistrador, Integer totalLibros, Integer totalPaginasLibros) {
        this.nombreRegistrador = nombreRegistrador;
        this.totalLibros = totalLibros;
        this.totalPaginasLibros = totalPaginasLibros;
    }

    public String getNombreRegistrador() {
        return nombreRegistrador;
    }

    public void setNombreRegistrador(String nombreRegistrador) {
        this.nombreRegistrador = nombreRegistrador;
    }

    public Integer getTotalLibros() {
        return totalLibros;
    }

    public void setTotalLibros(Integer totalLibros) {
        this.totalLibros = totalLibros;
    }

    public Integer getTotalPaginasLibros() {
        return totalPaginasLibros;
    }

    public void setTotalPaginasLibros(Integer totalPaginasLibros) {
        this.totalPaginasLibros = totalPaginasLibros;
    }

  

    

}

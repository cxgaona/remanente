/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.dto;

import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.Perfil;
import ec.gob.dinardap.seguridad.modelo.Usuario;

/**
 *
 * @author christian.gaona
 */
public class UsuarioDTO {

    private Usuario usuario;
    private Institucion institucion;
    private Perfil perfil;

    public UsuarioDTO() {
        
    }

    public UsuarioDTO(Usuario usuario) {
        this.usuario = usuario;
        this.institucion = usuario.getAsignacionInstitucions().get(0).getInstitucion();
        this.perfil = usuario.getUsuarioPerfilList().get(0).getPerfil();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion() {
        this.institucion = usuario.getAsignacionInstitucions().get(0).getInstitucion();
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil() {
        this.perfil = usuario.getUsuarioPerfilList().get(0).getPerfil();
    }
    
    

}

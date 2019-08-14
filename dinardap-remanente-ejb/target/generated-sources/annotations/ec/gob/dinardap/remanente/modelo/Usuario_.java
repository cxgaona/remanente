package ec.gob.dinardap.remanente.modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Usuario.class)
public abstract class Usuario_ {

	public static volatile SingularAttribute<Usuario, InstitucionRequerida> institucionId;
	public static volatile SingularAttribute<Usuario, Boolean> verificador;
	public static volatile SingularAttribute<Usuario, Boolean> administrador;
	public static volatile SingularAttribute<Usuario, String> estado;
	public static volatile ListAttribute<Usuario, Bandeja> bandejaList1;
	public static volatile SingularAttribute<Usuario, String> usuario;
	public static volatile SingularAttribute<Usuario, String> contrasena;
	public static volatile SingularAttribute<Usuario, Boolean> registrador;
	public static volatile ListAttribute<Usuario, Bandeja> bandejaList;
	public static volatile SingularAttribute<Usuario, Boolean> validador;
	public static volatile SingularAttribute<Usuario, Integer> usuarioId;
	public static volatile SingularAttribute<Usuario, String> nombre;

}


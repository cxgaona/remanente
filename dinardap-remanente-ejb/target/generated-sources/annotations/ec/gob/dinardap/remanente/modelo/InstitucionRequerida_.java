package ec.gob.dinardap.remanente.modelo;

import ec.gob.dinardap.seguridad.modelo.Institucion;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(InstitucionRequerida.class)
public abstract class InstitucionRequerida_ {

	public static volatile SingularAttribute<InstitucionRequerida, Institucion> institucionId;
	public static volatile SingularAttribute<InstitucionRequerida, String> ruc;
	public static volatile SingularAttribute<InstitucionRequerida, String> tipo;
	public static volatile SingularAttribute<InstitucionRequerida, InstitucionRequerida> institucionGad;
	public static volatile ListAttribute<InstitucionRequerida, Usuario> usuarioList;
	public static volatile SingularAttribute<InstitucionRequerida, InstitucionRequerida> institucionDireccionRegional;
	public static volatile ListAttribute<InstitucionRequerida, InstitucionRequerida> institucionDireccionRegionalList;
	public static volatile SingularAttribute<InstitucionRequerida, String> email;
	public static volatile SingularAttribute<InstitucionRequerida, String> provinciaCanton;
	public static volatile ListAttribute<InstitucionRequerida, RemanenteAnual> remanenteAnualList;
	public static volatile ListAttribute<InstitucionRequerida, InstitucionRequerida> institucionGadList;

}


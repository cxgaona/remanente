package ec.gob.dinardap.remanente.modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Bandeja.class)
public abstract class Bandeja_ {

	public static volatile SingularAttribute<Bandeja, String> descripcion;
	public static volatile SingularAttribute<Bandeja, RemanenteMensual> remanenteMensualId;
	public static volatile SingularAttribute<Bandeja, String> estado;
	public static volatile SingularAttribute<Bandeja, RemanenteCuatrimestral> remanenteCuatrimestral;
	public static volatile SingularAttribute<Bandeja, Usuario> usuarioAsignadoId;
	public static volatile SingularAttribute<Bandeja, Usuario> usuarioSolicitanteId;
	public static volatile SingularAttribute<Bandeja, Integer> bandejaId;

}


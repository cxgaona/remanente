package ec.gob.dinardap.remanente.modelo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EstadoRemanenteCuatrimestral.class)
public abstract class EstadoRemanenteCuatrimestral_ {

	public static volatile SingularAttribute<EstadoRemanenteCuatrimestral, String> descripcion;
	public static volatile SingularAttribute<EstadoRemanenteCuatrimestral, RemanenteCuatrimestral> remanenteCuatrimestral;
	public static volatile SingularAttribute<EstadoRemanenteCuatrimestral, Date> fechaRegistro;
	public static volatile SingularAttribute<EstadoRemanenteCuatrimestral, Integer> estadoRemanenteCuatrimestral;

}


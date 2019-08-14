package ec.gob.dinardap.remanente.modelo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EstadoRemanenteMensual.class)
public abstract class EstadoRemanenteMensual_ {

	public static volatile SingularAttribute<EstadoRemanenteMensual, String> descripcion;
	public static volatile SingularAttribute<EstadoRemanenteMensual, RemanenteMensual> remanenteMensualId;
	public static volatile SingularAttribute<EstadoRemanenteMensual, Date> fechaRegistro;
	public static volatile SingularAttribute<EstadoRemanenteMensual, Integer> estadoRemanenteMensualId;

}


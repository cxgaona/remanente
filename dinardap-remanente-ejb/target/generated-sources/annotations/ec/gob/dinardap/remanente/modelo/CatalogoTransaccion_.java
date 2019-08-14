package ec.gob.dinardap.remanente.modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CatalogoTransaccion.class)
public abstract class CatalogoTransaccion_ {

	public static volatile SingularAttribute<CatalogoTransaccion, String> tipo;
	public static volatile SingularAttribute<CatalogoTransaccion, Integer> catalogoTransaccionId;
	public static volatile SingularAttribute<CatalogoTransaccion, String> nombre;
	public static volatile ListAttribute<CatalogoTransaccion, Transaccion> transaccionList;

}


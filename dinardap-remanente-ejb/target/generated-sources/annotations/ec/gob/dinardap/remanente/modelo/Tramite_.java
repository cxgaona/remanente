package ec.gob.dinardap.remanente.modelo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Tramite.class)
public abstract class Tramite_ {

	public static volatile SingularAttribute<Tramite, String> acto;
	public static volatile SingularAttribute<Tramite, Date> fecha;
	public static volatile SingularAttribute<Tramite, String> numeroComprobantePago;
	public static volatile SingularAttribute<Tramite, String> tipo;
	public static volatile SingularAttribute<Tramite, String> numero;
	public static volatile SingularAttribute<Tramite, String> actividadRegistral;
	public static volatile SingularAttribute<Tramite, Date> fechaRegistro;
	public static volatile SingularAttribute<Tramite, Double> valor;
	public static volatile SingularAttribute<Tramite, Integer> tramiteId;
	public static volatile SingularAttribute<Tramite, Transaccion> transaccionId;

}


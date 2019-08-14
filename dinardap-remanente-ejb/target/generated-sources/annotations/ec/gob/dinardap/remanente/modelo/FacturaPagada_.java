package ec.gob.dinardap.remanente.modelo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FacturaPagada.class)
public abstract class FacturaPagada_ {

	public static volatile SingularAttribute<FacturaPagada, Integer> facturaPagadaId;
	public static volatile SingularAttribute<FacturaPagada, String> tipo;
	public static volatile SingularAttribute<FacturaPagada, String> numero;
	public static volatile SingularAttribute<FacturaPagada, Date> fechaRegistro;
	public static volatile SingularAttribute<FacturaPagada, Double> valor;
	public static volatile SingularAttribute<FacturaPagada, String> detalle;
	public static volatile SingularAttribute<FacturaPagada, Transaccion> transaccionId;

}


package ec.gob.dinardap.remanente.modelo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Transaccion.class)
public abstract class Transaccion_ {

	public static volatile SingularAttribute<Transaccion, RemanenteMensual> remanenteMensualId;
	public static volatile SingularAttribute<Transaccion, String> respaldoUrl;
	public static volatile SingularAttribute<Transaccion, Date> fechaRegistro;
	public static volatile ListAttribute<Transaccion, FacturaPagada> facturaPagadaList;
	public static volatile SingularAttribute<Transaccion, Double> valorTotal;
	public static volatile ListAttribute<Transaccion, Nomina> nominaList;
	public static volatile SingularAttribute<Transaccion, CatalogoTransaccion> catalogoTransaccionId;
	public static volatile ListAttribute<Transaccion, Tramite> tramiteList;
	public static volatile SingularAttribute<Transaccion, Integer> transaccionId;

}


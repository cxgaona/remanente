package ec.gob.dinardap.remanente.modelo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Nomina.class)
public abstract class Nomina_ {

	public static volatile SingularAttribute<Nomina, Double> remuneracion;
	public static volatile SingularAttribute<Nomina, Double> impuestoRenta;
	public static volatile SingularAttribute<Nomina, Double> decimoTercero;
	public static volatile SingularAttribute<Nomina, Double> totalDesc;
	public static volatile SingularAttribute<Nomina, Date> fechaRegistro;
	public static volatile SingularAttribute<Nomina, String> nomNombres;
	public static volatile SingularAttribute<Nomina, Double> liquidoRecibir;
	public static volatile SingularAttribute<Nomina, Double> aportePatronal;
	public static volatile SingularAttribute<Nomina, Double> fondosReserva;
	public static volatile SingularAttribute<Nomina, Double> decimoCuarto;
	public static volatile SingularAttribute<Nomina, Integer> nominaId;
	public static volatile SingularAttribute<Nomina, String> cargo;
	public static volatile SingularAttribute<Nomina, Transaccion> transaccionId;

}


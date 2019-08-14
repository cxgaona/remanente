package ec.gob.dinardap.remanente.modelo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RemanenteMensual.class)
public abstract class RemanenteMensual_ {

	public static volatile SingularAttribute<RemanenteMensual, Integer> remanenteMensualId;
	public static volatile SingularAttribute<RemanenteMensual, Date> fecha;
	public static volatile SingularAttribute<RemanenteMensual, Double> total;
	public static volatile SingularAttribute<RemanenteMensual, RemanenteCuatrimestral> remanenteCuatrimestral;
	public static volatile SingularAttribute<RemanenteMensual, String> informeAprobacionUrl;
	public static volatile ListAttribute<RemanenteMensual, EstadoRemanenteMensual> estadoRemanenteMensualList;
	public static volatile SingularAttribute<RemanenteMensual, String> solicitudCambioUrl;
	public static volatile ListAttribute<RemanenteMensual, Bandeja> bandejaList;
	public static volatile ListAttribute<RemanenteMensual, RemanenteMensual> remanenteMensualList;
	public static volatile SingularAttribute<RemanenteMensual, RemanenteMensual> remanenteMensualOrigenId;
	public static volatile SingularAttribute<RemanenteMensual, String> comentarios;
	public static volatile ListAttribute<RemanenteMensual, Transaccion> transaccionList;

}


package ec.gob.dinardap.remanente.modelo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RemanenteCuatrimestral.class)
public abstract class RemanenteCuatrimestral_ {

	public static volatile SingularAttribute<RemanenteCuatrimestral, Date> fecha;
	public static volatile SingularAttribute<RemanenteCuatrimestral, RemanenteCuatrimestralPK> remanenteCuatrimestralPK;
	public static volatile SingularAttribute<RemanenteCuatrimestral, RemanenteAnual> remanenteAnual;
	public static volatile SingularAttribute<RemanenteCuatrimestral, String> informeTecnicoUrl;
	public static volatile ListAttribute<RemanenteCuatrimestral, Bandeja> bandejaList;
	public static volatile ListAttribute<RemanenteCuatrimestral, EstadoRemanenteCuatrimestral> estadoRemanenteCuatrimestralList;
	public static volatile ListAttribute<RemanenteCuatrimestral, RemanenteMensual> remanenteMensualList;
	public static volatile SingularAttribute<RemanenteCuatrimestral, String> informeRemanenteUrl;

}


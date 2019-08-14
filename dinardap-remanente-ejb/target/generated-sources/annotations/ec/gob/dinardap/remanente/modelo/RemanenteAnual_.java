package ec.gob.dinardap.remanente.modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RemanenteAnual.class)
public abstract class RemanenteAnual_ {

	public static volatile SingularAttribute<RemanenteAnual, RemanenteAnualPK> remanenteAnualPK;
	public static volatile ListAttribute<RemanenteAnual, RemanenteCuatrimestral> remanenteCuatrimestralList;
	public static volatile SingularAttribute<RemanenteAnual, InstitucionRequerida> institucionRequerida;
	public static volatile SingularAttribute<RemanenteAnual, Integer> anio;

}


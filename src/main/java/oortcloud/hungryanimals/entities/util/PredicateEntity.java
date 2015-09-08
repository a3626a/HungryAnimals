package oortcloud.hungryanimals.entities.util;

import net.minecraft.entity.Entity;

import com.google.common.base.Predicate;

abstract public class PredicateEntity implements Predicate {

	abstract public boolean apply(Entity input);
	
	@Override
	public boolean apply(Object input) {
		return apply((Entity)input);
	}

}

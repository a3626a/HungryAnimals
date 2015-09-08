package oortcloud.hungryanimals.entities.util;

import oortcloud.hungryanimals.core.lib.Strings;
import net.minecraft.entity.Entity;

import com.google.common.base.Predicate;

public class PredicateUtil {

	public static Predicate IS_HUNGRY_ANIMAL = new PredicateEntity() {
		@Override
		public boolean apply(Entity input) {
			return input.getExtendedProperties(Strings.extendedPropertiesKey)!=null;
		}
	};
	
}

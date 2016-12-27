package oortcloud.hungryanimals.entities.util;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import oortcloud.hungryanimals.core.lib.Strings;

public class PredicateUtil {

	public static Predicate<Entity> IS_HUNGRY_ANIMAL = new Predicate<Entity>() {
		@Override
		public boolean apply(Entity input) {
			return input.getExtendedProperties(Strings.extendedPropertiesKey)!=null;
		}
	};
	
}

package oortcloud.hungryanimals.entities.util;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;

public class PredicateUtil {

	public static Predicate<Entity> IS_HUNGRY_ANIMAL = new Predicate<Entity>() {
		@Override
		public boolean apply(Entity input) {
			return input.hasCapability(ProviderHungryAnimal.CAP_HUNGRYANIMAL, null);
		}
	};
	
}

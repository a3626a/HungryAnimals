package oortcloud.hungryanimals.utils;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;

public class Predicates {

	// TODO Use These predicates to optimize code
	public static Predicate<Entity> IS_HUNGRY_ANIMAL = new Predicate<Entity>() {
		@Override
		public boolean apply(Entity input) {
			return input.hasCapability(ProviderHungryAnimal.CAP, null);
		}
	};
	public static Predicate<Entity> IS_TAMABLE_ANIMAL = new Predicate<Entity>() {
		@Override
		public boolean apply(Entity input) {
			return input.hasCapability(ProviderTamableAnimal.CAP, null);
		}
	};
}

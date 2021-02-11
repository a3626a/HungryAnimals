package oortcloud.hungryanimals.utils;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;

public class Predicates {

	public static Predicate<Entity> IS_HUNGRY_ANIMAL = new Predicate<Entity>() {
		@Override
		public boolean apply(@Nullable Entity input) {
			if (input == null)
				return false;
			return input.hasCapability(ProviderHungryAnimal.CAP).orElse(null);
		}
	};
	public static Predicate<Entity> IS_TAMABLE_ANIMAL = new Predicate<Entity>() {
		@Override
		public boolean apply(@Nullable Entity input) {
			if (input == null)
				return false;
			return input.hasCapability(ProviderTamableAnimal.CAP).orElse(null);
		}
	};
}

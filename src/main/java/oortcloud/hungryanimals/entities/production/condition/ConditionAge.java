package oortcloud.hungryanimals.entities.production.condition;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.MobEntity;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.capability.ICapabilityAgeable;
import oortcloud.hungryanimals.entities.capability.ProviderAgeable;

public class ConditionAge implements Predicate<MobEntity> {

	private boolean adult_only;
	
	public ConditionAge(boolean adult_only) {
		this.adult_only = adult_only;
	}
	
	@Override
	public boolean apply(@Nullable MobEntity input) {
		if (input == null) {
			return false;
		}
		ICapabilityAgeable ageable = input.getCapability(ProviderAgeable.CAP, null);
		
		if (ageable == null) {
			return adult_only;
		}
		
		if (adult_only) {
			return ageable.getAge() >= 0;
		} else {
			return ageable.getAge() < 0;
		}
	}

	public static Predicate<MobEntity> parse(JsonElement jsonEle) {
		try {
			String age = JsonUtils.getString(jsonEle, "age");
			if (age.equals("baby")) {
				return new ConditionAge(false);
			} else if (age.equals("adult")) {
				return new ConditionAge(true);
			} else {
				HungryAnimals.logger.warn("age must be one of \"baby\" or \"adult\". ignore this condition");
				return Predicates.alwaysTrue();
			}
		} catch (JsonSyntaxException e) {
			HungryAnimals.logger.warn("age must be a string. ignore this condition");
			return Predicates.alwaysTrue();
		}
	}
}

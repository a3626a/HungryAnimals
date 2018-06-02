package oortcloud.hungryanimals.entities.production.condition;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;

public class ConditionAge implements Predicate<EntityAgeable> {

	private boolean adult_only;
	
	public ConditionAge(boolean adult_only) {
		this.adult_only = adult_only;
	}
	
	@Override
	public boolean apply(@Nullable EntityAgeable input) {
		if (input == null) {
			return false;
		}
		if (adult_only) {
			return input.getGrowingAge() >= 0;
		} else {
			return input.getGrowingAge() < 0;
		}
	}

	public static Predicate<EntityAgeable> parse(JsonElement jsonEle) {
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

package oortcloud.hungryanimals.entities.production.condition;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;

import net.minecraft.entity.EntityAgeable;

public class ConditionAge implements Predicate<EntityAgeable> {

	@Override
	public boolean apply(EntityAgeable input) {
		return input.getGrowingAge() >= 0;
	}

	public static Predicate<EntityAgeable> parse(JsonElement jsonEle) {
		return new ConditionAge();
	}
	
}

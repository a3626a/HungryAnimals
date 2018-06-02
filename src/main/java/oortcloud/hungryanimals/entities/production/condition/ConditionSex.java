package oortcloud.hungryanimals.entities.production.condition;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.capability.ICapabilitySexual;
import oortcloud.hungryanimals.entities.capability.ICapabilitySexual.Sex;
import oortcloud.hungryanimals.entities.capability.ProviderSexual;

public class ConditionSex implements Predicate<EntityAgeable> {

	private boolean femaleOnly;
	
	public ConditionSex(boolean femaleOnly) {
		this.femaleOnly = femaleOnly;
	}
	
	@Override
	public boolean apply(@Nullable EntityAgeable input) {
		if (input == null)
			return false;
		
		ICapabilitySexual sexual = input.getCapability(ProviderSexual.CAP, null);
		if (sexual != null) {
			if (femaleOnly) {
				return sexual.getSex() == Sex.FEMALE;
			} else {
				return sexual.getSex() == Sex.MALE;
			}
		} else {
			return true;
		}
	}
	
	public static Predicate<EntityAgeable> parse(JsonElement jsonEle) {
		try {
			String sex = JsonUtils.getString(jsonEle, "sex");
			if (sex.equals("female")) {
				return new ConditionSex(true);
			} else if (sex.equals("male")) {
				return new ConditionSex(false);
			} else {
				HungryAnimals.logger.warn("sex must be one of \"female\" or \"male\". ignore this condition");
				return Predicates.alwaysTrue();
			}
		} catch (JsonSyntaxException e) {
			HungryAnimals.logger.warn("sex must be a string. ignore this condition");
			return Predicates.alwaysTrue();
		}
	}

}

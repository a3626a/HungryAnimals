package oortcloud.hungryanimals.entities.production.condition;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.entities.capability.ICapabilitySexual;
import oortcloud.hungryanimals.entities.capability.ICapabilitySexual.Sex;
import oortcloud.hungryanimals.entities.capability.ProviderSexual;

public class ConditionSex implements Predicate<EntityAgeable> {

	private boolean femaleOnly;
	
	public ConditionSex(boolean femaleOnly) {
		this.femaleOnly = femaleOnly;
	}
	
	@Override
	public boolean apply(EntityAgeable input) {
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
		JsonObject jsonObj = (JsonObject)jsonEle;
		
		boolean femaleOnly = JsonUtils.getBoolean(jsonObj, "female_only");
		
		return new ConditionSex(femaleOnly);
	}

}

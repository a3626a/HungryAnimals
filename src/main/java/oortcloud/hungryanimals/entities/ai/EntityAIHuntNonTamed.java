package oortcloud.hungryanimals.entities.ai;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;

public class EntityAIHuntNonTamed extends EntityAIHunt {

	private EntityTameable tameable;

	public EntityAIHuntNonTamed(EntityTameable tameable, boolean checkSight, boolean herding) {
		this(tameable, 10, checkSight, false, herding);
	}

	public EntityAIHuntNonTamed(EntityTameable tameable, int chance, boolean checkSight, boolean onlyNearby, boolean herding) {
		super(tameable, chance, checkSight, onlyNearby, herding);
		this.tameable = tameable;
	}
	
	@Override
	public boolean shouldExecute() {
		return !tameable.isTamed() && super.shouldExecute();
	}

	public static AIFactory parse(JsonElement jsonEle) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Target must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		
		JsonObject jsonObject = (JsonObject)jsonEle ;
		
		int chance = JsonUtils.getInt(jsonObject, "chance");
		boolean checkSight = JsonUtils.getBoolean(jsonObject, "check_sight");
		boolean onlyNearby = JsonUtils.getBoolean(jsonObject, "only_nearby");
		boolean herding = JsonUtils.getBoolean(jsonObject, "herding");
		return (entity) -> new EntityAIHuntNonTamed((EntityTameable) entity, chance, checkSight, onlyNearby, herding);
	}
	
}

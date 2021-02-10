package oortcloud.hungryanimals.entities.ai;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.JSONUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;

public class EntityAIHuntNonTamed extends EntityAIHunt {

	private TameableEntity tameable;

	public EntityAIHuntNonTamed(TameableEntity tameable, boolean checkSight, boolean herding) {
		this(tameable, 10, checkSight, false, herding);
	}

	public EntityAIHuntNonTamed(TameableEntity tameable, int chance, boolean checkSight, boolean onlyNearby, boolean herding) {
		super(tameable, chance, checkSight, onlyNearby, herding);
		this.tameable = tameable;
	}
	
	@Override
	public boolean shouldExecute() {
		return !tameable.isTamed() && super.shouldExecute();
	}

	public static void parse(JsonElement jsonEle, AIContainer aiContainer) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Target must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		
		JsonObject jsonObject = (JsonObject)jsonEle ;
		
		int chance = JSONUtils.getInt(jsonObject, "chance");
		boolean checkSight = JSONUtils.getBoolean(jsonObject, "check_sight");
		boolean onlyNearby = JSONUtils.getBoolean(jsonObject, "only_nearby");
		boolean herding = JSONUtils.getBoolean(jsonObject, "herding");
		
		AIFactory factory = (entity) -> {
			if (entity instanceof TameableEntity) {
				return new EntityAIHuntNonTamed((TameableEntity) entity, chance, checkSight, onlyNearby, herding);
			} else {
				HungryAnimals.logger.error("Animals which uses AI Hunt Non Tamed must extend EntityTamable. {} don't.", EntityList.getKey(entity));
				return null;
			}
		};
		aiContainer.getTarget().putLast(factory);
	}
	
}

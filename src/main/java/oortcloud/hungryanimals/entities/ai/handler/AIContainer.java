package oortcloud.hungryanimals.entities.ai.handler;

import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;

public class AIContainer implements IAIContainer<EntityAnimal> {
	
	private AIContainerTask task;
	private AIContainerTarget target;
	
	public AIContainer() {
		task = new AIContainerTask();
		target = new AIContainerTarget();
	}
	
	public AIContainerTask getTask() {
		return task;
	}
	
	public AIContainerTarget getTarget() {
		return target;
	}
	
	@Override
	public void registerAI(EntityAnimal entity) {
		task.registerAI(entity);
		target.registerAI(entity);
	}
	
	public void putBefore(AIFactory factory, Class<? extends EntityAIBase> target) {
		if (factory != null) {
			getTask().before(target).put(factory);
		}
	}
	
	public static AIFactory parseField(JsonObject jsonObj, String name, Function<JsonElement, AIFactory> parser) {
		if (!jsonObj.has(name)) {
			return null;
		}
		JsonElement jsonEle = jsonObj.get(name);
		return parser.apply(jsonEle);
	}

}

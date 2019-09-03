package oortcloud.hungryanimals.api;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.google.gson.JsonElement;

import net.minecraft.entity.EntityLiving;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.IAIContainer;

public interface IAIRegistry {
	public void registerAIContainerModifier(String type, String modifierName, BiConsumer<JsonElement, AIContainer> modifier); 
	public void registerAIContainer(String type, Function<JsonElement, IAIContainer<EntityLiving>> aiContainer); 
}

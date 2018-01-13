package oortcloud.hungryanimals.api;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.google.gson.JsonElement;

import net.minecraft.entity.passive.EntityAnimal;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.IAIContainer;

public interface IAIRegistry {
	public boolean registerAIContainerModifier(String type, BiConsumer<AIContainer, JsonElement> modifier); 
	public boolean registerAIContainer(String type, Function<JsonElement, IAIContainer<EntityAnimal>> aiContainer); 
}

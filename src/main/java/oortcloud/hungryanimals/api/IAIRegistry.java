package oortcloud.hungryanimals.api;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.google.gson.JsonElement;

import net.minecraft.entity.MobEntity;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.IAIContainer;

public interface IAIRegistry {
	void registerAIContainerModifier(String type, String modifierName, BiConsumer<JsonElement, AIContainer> modifier);
	void registerAIContainer(String type, BiFunction<Class<? extends MobEntity>, JsonElement, IAIContainer<MobEntity>> aiContainer);
}

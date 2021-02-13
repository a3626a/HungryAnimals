package oortcloud.hungryanimals.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.*;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.LazyForgeRegistry;
import oortcloud.hungryanimals.core.lib.References;

@Mod.EventBusSubscriber
public class Conditions {
	public static final DeferredRegister<ICondition.Serializer> CONDITIONS = new DeferredRegister<>(LazyForgeRegistry.of(ICondition.Serializer.class), References.MODID);
	public static final RegistryObject<ICondition.Serializer> ADJACENT = CONDITIONS.register("not_adjacent",
			ConditionAdjacent.Serializer::new
	);
	public static final RegistryObject<ICondition.Serializer> BELOW = CONDITIONS.register("below",
			ConditionBelow.Serializer::new
	);
	public static final RegistryObject<ICondition.Serializer> CHANCE = CONDITIONS.register("chance",
			ConditionChance.Serializer::new
	);

	public static ICondition parse(JsonElement jsonEle) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("Condition must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		JsonObject jsonObj = (JsonObject) jsonEle;

		IForgeRegistry<ICondition.Serializer> registry = GameRegistry.findRegistry(ICondition.Serializer.class);
		List<ICondition> conditions = new ArrayList<>();
		for (Entry<String, JsonElement> i : jsonObj.entrySet()) {
			ICondition.Serializer serializer = RegistryObject.of(new ResourceLocation(i.getKey()), registry).get();
			if (serializer == null) {
				HungryAnimals.logger.warn("{} is not a valid condition name.", i.getKey());
				continue;
			}
			conditions.add(serializer.deserialize(i.getValue()));
		}
		
		return new ConditionAnd(conditions);
	}

	@SubscribeEvent
	public static void newRegistry(RegistryEvent.NewRegistry event) {
		new RegistryBuilder<ICondition.Serializer>()
				.setName(new ResourceLocation(References.MODID, "condition"))
				.setType(ICondition.Serializer.class)
				.create();
	}
	
}

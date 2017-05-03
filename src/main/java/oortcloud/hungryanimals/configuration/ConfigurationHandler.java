package oortcloud.hungryanimals.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.ai.AIContainer;
import oortcloud.hungryanimals.entities.ai.AIContainerRegisterEvent;
import oortcloud.hungryanimals.entities.ai.AIManager;
import oortcloud.hungryanimals.entities.ai.IAIContainer;
import oortcloud.hungryanimals.entities.attributes.AttributeEntry;
import oortcloud.hungryanimals.entities.attributes.AttributeManager;
import oortcloud.hungryanimals.entities.attributes.AttributeRegisterEvent;
import oortcloud.hungryanimals.entities.attributes.IAttributeEntry;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState.HashBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceManager;
import oortcloud.hungryanimals.entities.food_preferences.HungryAnimalRegisterEvent;
import oortcloud.hungryanimals.entities.loot_tables.LootTableModifier;

public class ConfigurationHandler {

	private static ConfigurationHandlerJSON foodPreferencesBlock;
	private static ConfigurationHandlerJSON foodPreferencesItem;
	private static ConfigurationHandlerJSON attributes;
	private static ConfigurationHandlerJSON lootTables;
	private static ConfigurationHandlerJSON ais;
	
	public static Gson GSON_INSTANCE_FOOD_PREFERENCE_BLOCK = new GsonBuilder().registerTypeAdapter(HashBlockState.class, new HashBlockState.Serializer())
			.create();
	public static Gson GSON_INSTANCE_FOOD_PREFERENCE_ITEM = new GsonBuilder().registerTypeAdapter(HashItemType.class, new HashItemType.Serializer()).create();

	public static void init(FMLPreInitializationEvent event) {
		File basefodler = new File(event.getModConfigurationDirectory(), References.MODID);
		foodPreferencesBlock = new ConfigurationHandlerJSON(basefodler, "food_preferences/block", (file, animal) -> {
			JsonArray arr;
			try {
				arr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonArray();
			} catch (JsonSyntaxException | IOException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { foodPreferencesBlock.getDescriptor(), file, animal, e });
				return;
			}
			Map<HashBlockState, Double> map = new HashMap<HashBlockState, Double>();
			for (JsonElement i : arr) {
				JsonObject obj = i.getAsJsonObject();
				HashBlockState state = GSON_INSTANCE_FOOD_PREFERENCE_BLOCK.fromJson(obj.getAsJsonObject("block"), HashBlockState.class);
				double hunger = obj.getAsJsonPrimitive("hunger").getAsDouble();
				map.put(state, hunger);
			}
			HungryAnimalRegisterEvent.FoodPreferenceBlockStateRegisterEvent event_ = new HungryAnimalRegisterEvent.FoodPreferenceBlockStateRegisterEvent(animal,
					map);
			MinecraftForge.EVENT_BUS.post(event_);
			FoodPreferenceManager.getInstance().REGISTRY_BLOCK.put(animal, new FoodPreferenceBlockState(map));
		});
		foodPreferencesItem = new ConfigurationHandlerJSON(basefodler, "food_preferences/item", (file, animal) -> {
			JsonArray arr;
			try {
				arr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonArray();
			} catch (JsonSyntaxException | IOException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { foodPreferencesItem.getDescriptor(), file, animal, e });
				return;
			}
			Map<HashItemType, Double> map = new HashMap<HashItemType, Double>();
			for (JsonElement i : arr) {
				JsonObject obj = i.getAsJsonObject();
				HashItemType state = GSON_INSTANCE_FOOD_PREFERENCE_ITEM.fromJson(obj.getAsJsonObject("item"), HashItemType.class);
				double hunger = obj.getAsJsonPrimitive("hunger").getAsDouble();
				map.put(state, hunger);
			}
			HungryAnimalRegisterEvent.FoodPreferenceItemStackRegisterEvent event_ = new HungryAnimalRegisterEvent.FoodPreferenceItemStackRegisterEvent(animal,
					map);
			MinecraftForge.EVENT_BUS.post(event_);
			FoodPreferenceManager.getInstance().REGISTRY_ITEM.put(animal, new FoodPreferenceItemStack(map));
		});
		attributes = new ConfigurationHandlerJSON(basefodler, "attributes", (file, animal) -> {
			JsonObject jobj;
			try {
				jobj = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonObject();
			} catch (JsonSyntaxException | IOException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { attributes.getDescriptor(), file, animal, e });
				return;
			}
			
			// AUTO FILE FORMAT CHECK AND UPDATE
			List<IAttributeEntry> list = new ArrayList<IAttributeEntry>();
			for (Entry<String, JsonElement> i : jobj.entrySet()) {
				if (!AttributeManager.getInstance().ATTRIBUTES.containsKey(i.getKey())) {
					HungryAnimals.logger.warn("Couldn\'t load {} {} of {}", new Object[] { attributes.getDescriptor(), i, animal });
					continue;
				}
				IAttribute attribute = AttributeManager.getInstance().ATTRIBUTES.get(i.getKey()).attribute;
				boolean shouldRegister = AttributeManager.getInstance().ATTRIBUTES.get(i.getKey()).shouldRegister;
				list.add(new AttributeEntry(attribute, shouldRegister, i.getValue().getAsDouble()));
			}
			AttributeRegisterEvent event_ = new AttributeRegisterEvent(animal, list);
			MinecraftForge.EVENT_BUS.post(event_);
			AttributeManager.getInstance().REGISTRY.put(animal, list);
		});
		lootTables = new ConfigurationHandlerJSON(basefodler, "loot_tables/entities", (file, animal) -> {
		});
		ais = new ConfigurationHandlerJSON(basefodler, "ais", (file, animal) -> {
			JsonObject arr;
			try {
				arr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonObject();
			} catch (JsonSyntaxException | IOException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { attributes.getDescriptor(), file, animal, e });
				return;
			}
			String ai = arr.get("type").getAsString();
			IAIContainer<EntityAnimal> aiContainer = AIManager.getInstance().AITYPES.get(ai).apply(animal);
			MinecraftForge.EVENT_BUS.post(new AIContainerRegisterEvent(animal, (AIContainer)aiContainer));
			AIManager.getInstance().REGISTRY.put(animal, aiContainer);
		});
		LootTableModifier.init(basefodler);
		ConfigurationHandlerAnimal.init(new File(event.getModConfigurationDirectory() + "/" + References.MODID + "/Animal.cfg"));
		ConfigurationHandlerWorld.init(new File(event.getModConfigurationDirectory() + "/" + References.MODID + "/World.cfg"));
		ConfigurationHandlerRecipe.init(new File(event.getModConfigurationDirectory() + "/" + References.MODID + "/Recipe.cfg"));
	}

	public static void sync() {
		ConfigurationHandlerWorld.sync();
		ConfigurationHandlerRecipe.sync();
		foodPreferencesBlock.sync();
		foodPreferencesItem.sync();
		attributes.sync();
		lootTables.sync();
		ais.sync();
		LootTableModifier.sync();
	}

	public static void postSync() {
		ConfigurationHandlerAnimal.sync();
	}

}

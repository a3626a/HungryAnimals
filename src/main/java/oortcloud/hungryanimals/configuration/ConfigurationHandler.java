package oortcloud.hungryanimals.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
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
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState.HashBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceManager;
import oortcloud.hungryanimals.entities.food_preferences.HungryAnimalRegisterEvent;
import oortcloud.hungryanimals.entities.loot_tables.LootTableModifier;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue;

public class ConfigurationHandler {

	private static ConfigurationHandlerJSON foodPreferencesBlock;
	private static ConfigurationHandlerJSON foodPreferencesItem;
	private static ConfigurationHandlerJSON attributes;
	private static ConfigurationHandlerJSON lootTables;
	private static ConfigurationHandlerJSON ais;
	private static ConfigurationHandlerRecipe recipes;

	public static Gson GSON_INSTANCE_FOOD_PREFERENCE_BLOCK = new GsonBuilder().registerTypeAdapter(HashBlockState.class, new HashBlockState.Serializer())
			.create();
	public static Gson GSON_INSTANCE_FOOD_PREFERENCE_ITEM = new GsonBuilder().registerTypeAdapter(HashItemType.class, new HashItemType.Serializer()).create();

	public static void init(FMLPreInitializationEvent event) {
		File basefolder = new File(event.getModConfigurationDirectory(), References.MODID);
		
		System.out.println(basefolder.getAbsolutePath());
		
		foodPreferencesBlock = new ConfigurationHandlerJSON(basefolder, "food_preferences/block", (file, animal) -> {
			JsonArray jsonArr;
			try {
				jsonArr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonArray();
			} catch (JsonSyntaxException | IOException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { foodPreferencesBlock.getDescriptor(), file, animal, e });
				return;
			}
			Map<HashBlockState, Double> map = new HashMap<HashBlockState, Double>();
			for (JsonElement i : jsonArr) {
				JsonObject jsonObj = i.getAsJsonObject();
				HashBlockState state = GSON_INSTANCE_FOOD_PREFERENCE_BLOCK.fromJson(jsonObj.getAsJsonObject("block"), HashBlockState.class);
				double hunger = jsonObj.getAsJsonPrimitive("hunger").getAsDouble();
				map.put(state, hunger);
			}
			HungryAnimalRegisterEvent.FoodPreferenceBlockStateRegisterEvent event_ = new HungryAnimalRegisterEvent.FoodPreferenceBlockStateRegisterEvent(animal,
					map);
			MinecraftForge.EVENT_BUS.post(event_);
			FoodPreferenceManager.getInstance().REGISTRY_BLOCK.put(animal, new FoodPreferenceBlockState(map));
		});
		foodPreferencesItem = new ConfigurationHandlerJSON(basefolder, "food_preferences/item", (file, animal) -> {
			JsonArray jsonArr;
			try {
				jsonArr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonArray();
			} catch (JsonSyntaxException | IOException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { foodPreferencesItem.getDescriptor(), file, animal, e });
				return;
			}
			Map<HashItemType, Double> map = new HashMap<HashItemType, Double>();
			for (JsonElement i : jsonArr) {
				JsonObject jsonObj = i.getAsJsonObject();
				HashItemType state = GSON_INSTANCE_FOOD_PREFERENCE_ITEM.fromJson(jsonObj.getAsJsonObject("item"), HashItemType.class);
				double hunger = jsonObj.getAsJsonPrimitive("hunger").getAsDouble();
				map.put(state, hunger);
			}
			HungryAnimalRegisterEvent.FoodPreferenceItemStackRegisterEvent event_ = new HungryAnimalRegisterEvent.FoodPreferenceItemStackRegisterEvent(animal,
					map);
			MinecraftForge.EVENT_BUS.post(event_);
			FoodPreferenceManager.getInstance().REGISTRY_ITEM.put(animal, new FoodPreferenceItemStack(map));
		});
		attributes = new ConfigurationHandlerJSON(basefolder, "attributes", (file, animal) -> {
			JsonObject jsonObj;
			try {
				jsonObj = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonObject();
			} catch (JsonSyntaxException | IOException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { attributes.getDescriptor(), file, animal, e });
				return;
			}

			// AUTO FILE FORMAT CHECK AND UPDATE
			if (jsonObj.entrySet().iterator().next().getValue().isJsonObject()) {
				JsonObject updatedJsonObj = new JsonObject();
				for (Entry<String, JsonElement> i : jsonObj.entrySet()) {
					if (i.getKey().equals("hungryanimals.courtship_hungerCondition")) {
						updatedJsonObj.addProperty(ModAttributes.NAME_courtship_hunger_condition, i.getValue().getAsJsonObject().get("value").getAsDouble());
					} else {
						updatedJsonObj.addProperty(i.getKey(), i.getValue().getAsJsonObject().get("value").getAsDouble());
					}
				}
				jsonObj = updatedJsonObj;
				try {
					Files.delete(file.toPath());
					Files.write(file.toPath(), jsonObj.toString().getBytes(), StandardOpenOption.CREATE_NEW);
				} catch (IOException e) {
					HungryAnimals.logger.error("Couldn\'t auto-update old formatted {}\n{}", file, e);
				}
			}

			List<IAttributeEntry> list = new ArrayList<IAttributeEntry>();
			for (Entry<String, JsonElement> i : jsonObj.entrySet()) {
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

		lootTables = new ConfigurationHandlerJSON(basefolder, "loot_tables/entities", (file, animal) -> {
		});
		ais = new ConfigurationHandlerJSON(basefolder, "ais", (file, animal) -> {
			JsonObject jsonObj;
			try {
				jsonObj = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonObject();
			} catch (JsonSyntaxException | IOException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { ais.getDescriptor(), file, animal, e });
				return;
			}
			String ai = jsonObj.get("type").getAsString();
			IAIContainer<EntityAnimal> aiContainer = AIManager.getInstance().AITYPES.get(ai).apply(animal);
			MinecraftForge.EVENT_BUS.post(new AIContainerRegisterEvent(animal, (AIContainer) aiContainer));
			AIManager.getInstance().REGISTRY.put(animal, aiContainer);
		});

		recipes = new ConfigurationHandlerRecipe(basefolder, "recipes", (file) -> {
			JsonArray jsonArr;
			try {
				jsonArr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonArray();
			} catch (JsonSyntaxException | IOException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {}\n{}", new Object[] { recipes.getDescriptor(), file, e });
				return;
			}
			for (JsonElement i : jsonArr) {
				JsonObject jsonObj = i.getAsJsonObject();
				HashItemType state = GSON_INSTANCE_FOOD_PREFERENCE_ITEM.fromJson(jsonObj.getAsJsonObject("item"), HashItemType.class);
				int count = jsonObj.getAsJsonPrimitive("count").getAsInt();
				RecipeAnimalGlue.addRecipe(state, count);
			}
		});

		LootTableModifier.init(basefolder);
		ConfigurationHandlerAnimal.init(new File(event.getModConfigurationDirectory() + "/" + References.MODID + "/Animal.cfg"));
		ConfigurationHandlerWorld.init(new File(event.getModConfigurationDirectory() + "/" + References.MODID + "/World.cfg"));
	}

	public static void sync() {
		ConfigurationHandlerWorld.sync();
		foodPreferencesBlock.sync();
		foodPreferencesItem.sync();
		attributes.sync();
		lootTables.sync();
		ais.sync();
		recipes.sync();
		LootTableModifier.sync();
	}

	public static void postSync() {
		ConfigurationHandlerAnimal.sync();
	}

}

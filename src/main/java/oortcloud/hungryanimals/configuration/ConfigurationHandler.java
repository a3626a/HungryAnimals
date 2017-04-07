package oortcloud.hungryanimals.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState.HashBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceManager;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceRegisterEvent;

public class ConfigurationHandler {

	private static ConfigurationHandlerJSON foodPreferencesBlock;
	private static ConfigurationHandlerJSON foodPreferencesItem;
	public static Gson GSON_INSTANCE_FOOD_PREFERENCE_BLOCK = new GsonBuilder().registerTypeAdapter(HashBlockState.class, new HashBlockState.Serializer())
			.create();
	public static Gson GSON_INSTANCE_FOOD_PREFERENCE_ITEM = new GsonBuilder().registerTypeAdapter(HashItemType.class, new HashItemType.Serializer())
			.create();
	
	public static void init(FMLPreInitializationEvent event) {
		foodPreferencesBlock = new ConfigurationHandlerJSON(event, "food_preferences/block", (file, animal) -> {
			JsonArray arr;
			try {
				arr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonArray();
			} catch (JsonSyntaxException | IOException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { foodPreferencesBlock.getDescriptor(), file, animal, e });
				e.printStackTrace();
				return;
			}
			Map<HashBlockState, Double> map = new HashMap<HashBlockState, Double>();
			for (JsonElement i : arr) {
				JsonObject obj = i.getAsJsonObject();
				HashBlockState state = GSON_INSTANCE_FOOD_PREFERENCE_BLOCK.fromJson(obj.getAsJsonObject("block"), HashBlockState.class);
				double hunger = obj.getAsJsonPrimitive("hunger").getAsDouble();
				map.put(state, hunger);
			}
			FoodPreferenceRegisterEvent.FoodPreferenceBlockStateRegisterEvent event_ = new FoodPreferenceRegisterEvent.FoodPreferenceBlockStateRegisterEvent(
					animal, map);
			MinecraftForge.EVENT_BUS.post(event_);
			FoodPreferenceManager.getInstance().REGISTRY_BLOCK.put(animal, new FoodPreferenceBlockState(map));
		});
		foodPreferencesItem = new ConfigurationHandlerJSON(event, "food_preferences/item", (file, animal) -> {
			JsonArray arr;
			try {
				arr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonArray();
			} catch (JsonSyntaxException | IOException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { foodPreferencesItem.getDescriptor(), file, animal, e });
				e.printStackTrace();
				return;
			}
			Map<HashItemType, Double> map = new HashMap<HashItemType, Double>();
			for (JsonElement i : arr) {
				JsonObject obj = i.getAsJsonObject();
				HashItemType state = GSON_INSTANCE_FOOD_PREFERENCE_ITEM.fromJson(obj.getAsJsonObject("item"), HashItemType.class);
				double hunger = obj.getAsJsonPrimitive("hunger").getAsDouble();
				map.put(state, hunger);
			}
			FoodPreferenceRegisterEvent.FoodPreferenceItemStackRegisterEvent event_ = new FoodPreferenceRegisterEvent.FoodPreferenceItemStackRegisterEvent(
					animal, map);
			MinecraftForge.EVENT_BUS.post(event_);
			FoodPreferenceManager.getInstance().REGISTRY_ITEM.put(animal, new FoodPreferenceItemStack(map));
		});
		ConfigurationHandlerAnimal.init(new File(event.getModConfigurationDirectory() + "/" + References.MODNAME + "/Animal.cfg"));
		ConfigurationHandlerWorld.init(new File(event.getModConfigurationDirectory() + "/" + References.MODNAME + "/World.cfg"));
		ConfigurationHandlerRecipe.init(new File(event.getModConfigurationDirectory() + "/" + References.MODNAME + "/Recipe.cfg"));
		ConfigurationHandlerFoodPreferenceItem.init(new File(event.getModConfigurationDirectory() + "/" + References.MODNAME + "/FoodPreferences/item"));
	}

	public static void sync() {
		ConfigurationHandlerWorld.sync();
		ConfigurationHandlerRecipe.sync();
		foodPreferencesBlock.sync();
	}

	public static void postSync() {
		ConfigurationHandlerAnimal.sync();
	}

}

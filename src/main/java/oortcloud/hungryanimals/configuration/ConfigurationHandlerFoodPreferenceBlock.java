package oortcloud.hungryanimals.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.common.MinecraftForge;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState.HashBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceManager;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceRegisterEvent;
import oortcloud.hungryanimals.entities.properties.handler.HungryAnimalManager;

public class ConfigurationHandlerFoodPreferenceBlock {

	public static Gson GSON_INSTANCE = new GsonBuilder()
			.registerTypeAdapter(HashBlockState.class, new HashBlockState.Serializer()).create();
	public static File directory;

	public static void init(File directory) {

		ConfigurationHandlerFoodPreferenceBlock.directory = directory;

		if (!directory.exists()) {
			try {
				Files.createDirectories(directory.toPath());
			} catch (IOException e) {
				HungryAnimals.logger.warn("Couldn\'t create food preference folder {}", new Object[] { directory, e });
				return;
			}
		}

		for (Class<? extends EntityAnimal> i : HungryAnimalManager.getInstance().getRegisteredAnimal()) {
			File iFile = new File(directory, EntityList.CLASS_TO_NAME.get(i) + ".json");

			if (!iFile.exists()) {
				createDefaultConfigurationFile(iFile);
			}
		}
	}

	public static void createDefaultConfigurationFile(File file) {
		URL url = ConfigurationHandlerFoodPreferenceBlock.class
				.getResource("/assets/" + References.MODID + "/food_preferences/block/" + file.getName());

		if (url == null) {
			HungryAnimals.logger.warn("Couldn\'t load food preference {} from {}", new Object[] { file, url });
			return;
		}

		String s;

		try {
			s = Resources.toString(url, Charsets.UTF_8);
			file.createNewFile();
			FileWriter o = new FileWriter(file);
			o.write(s);
			o.close();
		} catch (IOException ioexception) {
			HungryAnimals.logger.warn("Couldn\'t load food preference {} from {}",
					new Object[] { file, url, ioexception });
		}
	}

	public static void sync() {
		for (Class<? extends EntityAnimal> i : HungryAnimalManager.getInstance().getRegisteredAnimal()) {
			File iFile = new File(directory, EntityList.CLASS_TO_NAME.get(i) + ".json");

			if (iFile.exists()) {
				try {
					syncFile(iFile, i);
				} catch (JsonSyntaxException e) {
					HungryAnimals.logger.warn("Couldn\'t load food preference {} of {}",
							new Object[] { iFile, i, e });
					e.printStackTrace();
				} catch (IOException e) {
					HungryAnimals.logger.warn("Couldn\'t load food preference {} of {}",
							new Object[] { iFile, i, e });
					e.printStackTrace();
				}
			}
		}
	}

	public static void syncFile(File file, Class<? extends EntityAnimal> entity)
			throws JsonSyntaxException, IOException {
		JsonArray arr = (new JsonParser()).parse(new String(Files.readAllBytes(file.toPath()))).getAsJsonArray();
		Map<HashBlockState, Double> map = new HashMap<HashBlockState, Double>();
		for (JsonElement i : arr) {
			JsonObject obj = i.getAsJsonObject();
			HashBlockState state = GSON_INSTANCE.fromJson(obj.getAsJsonObject("block"), HashBlockState.class);
			double hunger = obj.getAsJsonPrimitive("hunger").getAsDouble();
			map.put(state, hunger);
		}
		FoodPreferenceRegisterEvent.FoodPreferenceBlockStateRegisterEvent event = new FoodPreferenceRegisterEvent.FoodPreferenceBlockStateRegisterEvent(entity, map);
		MinecraftForge.EVENT_BUS.post(event);
		FoodPreferenceManager.getInstance().REGISTRY_BLOCK.put(entity, new FoodPreferenceBlockState(map));
	}

}

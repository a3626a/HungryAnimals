package oortcloud.hungryanimals.configuration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.API;
import oortcloud.hungryanimals.api.HAPlugins;
import oortcloud.hungryanimals.blocks.BlockExcreta;
import oortcloud.hungryanimals.blocks.BlockNiterBed;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.ai.handler.AIContainers;
import oortcloud.hungryanimals.entities.ai.handler.IAIContainer;
import oortcloud.hungryanimals.entities.event.EntityEventHandler.Pair;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceEntity;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceIngredient;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceIngredient.FoodPreferenceIngredientEntry;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.handler.Cures;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;
import oortcloud.hungryanimals.entities.handler.InHeats;
import oortcloud.hungryanimals.entities.loot_tables.ModLootTables;
import oortcloud.hungryanimals.entities.production.IProduction;
import oortcloud.hungryanimals.entities.production.Productions;
import oortcloud.hungryanimals.generation.GrassGenerator;
import oortcloud.hungryanimals.generation.GrassGenerators;
import oortcloud.hungryanimals.potion.PotionDisease;
import oortcloud.hungryanimals.potion.PotionOvereat;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue;
import oortcloud.hungryanimals.utils.HashBlockState;
import oortcloud.hungryanimals.utils.ModJsonUtils;

public class ConfigurationHandler {

	private static ConfigurationHandlerJSONAnimal foodPreferencesBlock;
	private static ConfigurationHandlerJSONAnimal foodPreferencesItem;
	private static ConfigurationHandlerJSONAnimal foodPreferencesEntity;
	private static ConfigurationHandlerJSONAnimal attributes;
	private static ConfigurationHandlerJSONAnimal lootTables;
	private static ConfigurationHandlerJSONAnimal ais;
	private static ConfigurationHandlerJSONAnimal productions;
	private static ConfigurationHandlerJSON recipes;
	private static ConfigurationHandlerJSON world;
	private static ConfigurationHandlerJSON animal;
	private static ConfigurationHandlerJSON cures;
	private static ConfigurationHandlerJSON inheat;
	private static ConfigurationHandlerJSON generators;
	private static ConfigurationHandlerJSON disease;
	private static ConfigurationHandlerJSON overeat;

	public static Gson GSON_INSTANCE_ITEM_STACK = new GsonBuilder().registerTypeAdapter(ItemStack.class, new ConfigurationHandler.Serializer()).create();

	public static void init(FMLPreInitializationEvent event) {
		File basefolder = new File(event.getModConfigurationDirectory(), References.MODID);
		File examplefolder = new File(event.getModConfigurationDirectory(), References.MODID + "_example");

		// Create Example Config Folder
		try {
			createExample(examplefolder);
		} catch (URISyntaxException | IOException e1) {
			HungryAnimals.logger.error("Couldn\'t create config examples\n{}", e1);
		}

		foodPreferencesBlock = new ConfigurationHandlerJSONAnimal(basefolder, "food_preferences/block", (jsonElement, animal) -> {
			JsonArray jsonArr = (JsonArray) jsonElement;

			Map<HashBlockState, Pair<Double, Double>> map = new HashMap<HashBlockState, Pair<Double, Double>>();
			for (JsonElement i : jsonArr) {
				JsonObject jsonObj = i.getAsJsonObject();
				HashBlockState state = HashBlockState.parse(jsonObj.getAsJsonObject("block"));
				double nutrient = jsonObj.getAsJsonPrimitive("nutrient").getAsDouble();
				double stomach = jsonObj.getAsJsonPrimitive("stomach").getAsDouble();
				map.put(state, new Pair<Double, Double>(nutrient, stomach));
			}
			FoodPreferences.getInstance().REGISTRY_BLOCK.put(animal, new FoodPreferenceBlockState(map));
		});
		foodPreferencesItem = new ConfigurationHandlerJSONAnimal(basefolder, "food_preferences/item", (jsonElement, animal) -> {
			JsonArray jsonArr = (JsonArray) jsonElement;

			List<FoodPreferenceIngredientEntry> list = new ArrayList<FoodPreferenceIngredientEntry>();
			for (JsonElement i : jsonArr) {
				JsonObject jsonObj = i.getAsJsonObject();
				Ingredient ing = ModJsonUtils.getIngredient(jsonObj.get("item"));
				double nutrient = jsonObj.getAsJsonPrimitive("nutrient").getAsDouble();
				double stomach = jsonObj.getAsJsonPrimitive("stomach").getAsDouble();
				list.add(new FoodPreferenceIngredientEntry(ing, nutrient, stomach));
			}
			FoodPreferences.getInstance().REGISTRY_ITEM.put(animal, new FoodPreferenceIngredient(list));
		});
		foodPreferencesEntity = new ConfigurationHandlerJSONAnimal(basefolder, "food_preferences/entity", (jsonElement, animal) -> {
			JsonArray jsonArr = (JsonArray) jsonElement;

			Set<Class<? extends EntityLiving>> set = new HashSet<Class<? extends EntityLiving>>();
			for (JsonElement i : jsonArr) {
				String resourceLocation = i.getAsString();
				Class<? extends Entity> entityClass = EntityList.getClass(new ResourceLocation(resourceLocation));
				if (entityClass != null) {
					set.add(entityClass.asSubclass(EntityLiving.class));
				} else {
					HungryAnimals.logger.error("cannot find the animal {}", resourceLocation);
				}
			}
			FoodPreferences.getInstance().REGISTRY_ENTITY.put(animal, new FoodPreferenceEntity(set));
		});
		attributes = new ConfigurationHandlerJSONAnimal(basefolder, "attributes", (jsonElement, animal) -> {
			JsonObject jsonObj = (JsonObject) jsonElement;

			for (Entry<String, JsonElement> i : jsonObj.entrySet()) {
				if (i.getValue().isJsonObject()) {
					// Custom shouldRegister
					JsonObject jsonAttribute = i.getValue().getAsJsonObject();
					boolean shouldRegister = JsonUtils.getBoolean(jsonAttribute, "should_register");
					double value = JsonUtils.getFloat(jsonAttribute, "value");
					API.registerAttribute(animal, i.getKey(), value, shouldRegister);
				} else {
					// Default shouldRegister
					double value = i.getValue().getAsDouble();
					API.registerAttribute(animal, i.getKey(), value);
				}
			}
		});

		lootTables = new ConfigurationHandlerJSONAnimal(basefolder, "loot_tables/entities", (jsonElement, animal) -> {

		});
		ais = new ConfigurationHandlerJSONAnimal(basefolder, "ais", (jsonElement, animal) -> {
			IAIContainer<EntityAnimal> aiContainer = AIContainers.getInstance().parse(jsonElement);
			AIContainers.getInstance().register(animal, aiContainer);
		});
		productions = new ConfigurationHandlerJSONAnimal(basefolder, "productions", (jsonElement, animal) -> {
			JsonArray jsonArr = jsonElement.getAsJsonArray();
			
			for (JsonElement i : jsonArr) {
				Function<EntityAnimal, IProduction> factory = Productions.getInstance().parse(i);
				if (factory != null) {
					API.registerProduction(animal, factory);
				} else {
					HungryAnimals.logger.error("{} is not a valid Production", i);
				}
			}
		});
		
		
		recipes = new ConfigurationHandlerJSON(basefolder, "recipes/animalglue", (jsonElement) -> {
			JsonArray jsonArr = (JsonArray) jsonElement;

			for (JsonElement i : jsonArr) {
				JsonObject jsonObj = i.getAsJsonObject();
				Ingredient ing = ModJsonUtils.getIngredient(jsonObj.get("item"));
				int count = jsonObj.getAsJsonPrimitive("count").getAsInt();
				RecipeAnimalGlue.addRecipe(ing, count);
			}
		});
		world = new ConfigurationHandlerJSON(basefolder, "world", (jsonElement) -> {
			JsonObject jsonObj = (JsonObject) jsonElement;

			BlockExcreta.diseaseProbability = jsonObj.getAsJsonPrimitive("disease_probability").getAsDouble();
			BlockExcreta.erosionProbabilityOnHay = jsonObj.getAsJsonPrimitive("erosion_probability_on_hay").getAsDouble();
			BlockExcreta.erosionProbability = jsonObj.getAsJsonPrimitive("erosion_probability").getAsDouble();
			BlockExcreta.fermetationProbability = jsonObj.getAsJsonPrimitive("fermentation_probability").getAsDouble();
			BlockExcreta.fertilizationProbability = jsonObj.getAsJsonPrimitive("fertilization_probability").getAsDouble();
			BlockNiterBed.ripeningProbability = jsonObj.getAsJsonPrimitive("ripening_probability").getAsDouble();
		});
		cures = new ConfigurationHandlerJSON(basefolder, "cures", (jsonElement) -> {
			List<Ingredient> ingredients = ModJsonUtils.getIngredients(jsonElement);
			for (Ingredient i : ingredients)
				Cures.getInstance().register(i);
		});
		inheat = new ConfigurationHandlerJSON(basefolder, "inheat", (jsonElement) -> {
			JsonArray jsonArr = (JsonArray) jsonElement;

			for (JsonElement jsonEle : jsonArr) {
				JsonElement item = jsonEle.getAsJsonObject().get("item");
				Ingredient inheat = ModJsonUtils.getIngredient(item);
				int inheatDuration = JsonUtils.getInt(jsonEle.getAsJsonObject(), "duration");
				InHeats.getInstance().register(inheat, inheatDuration);
			}
		});
		generators = new ConfigurationHandlerJSON(basefolder, "generators", (jsonElement) -> {
			JsonArray jsonArr = (JsonArray) jsonElement;

			for (JsonElement jsonEle : jsonArr) {
				JsonObject jsonObj = jsonEle.getAsJsonObject();
				JsonElement generator = jsonObj.get("generator");
				Biome biome = null;
				if (jsonObj.has("biome")) {
					String biomeName = JsonUtils.getString(jsonObj, "biome");
					biome = GameRegistry.findRegistry(Biome.class).getValue(new ResourceLocation(biomeName));
					if (biome == null) {
						throw new JsonSyntaxException(biomeName);
					}
					GrassGenerators.getInstance().registerByBiome(biome, GrassGenerator.parse(generator));
				} else if (jsonObj.has("types")) {
					JsonElement jsonEleTypes = jsonObj.get("types");
					if (jsonEleTypes instanceof JsonArray) {
						JsonArray jsonArrayTypes = (JsonArray) jsonEleTypes;
						List<String> stringTypes = new ArrayList<>();
						for (JsonElement i : jsonArrayTypes) {
							stringTypes.add(i.getAsString());
						}
						GrassGenerators.getInstance().registerByTypeName(stringTypes, GrassGenerator.parse(generator));
					} else {
						throw new JsonSyntaxException(jsonEleTypes.toString());
					}
				} else {
					GrassGenerators.getInstance().registerByBiome(null, GrassGenerator.parse(generator));
				}

			}
		});
		animal = new ConfigurationHandlerJSON(basefolder, "animal", (jsonElement) -> {
			JsonArray jsonArr = (JsonArray) jsonElement;

			for (JsonElement jsonEle : jsonArr) {

				String name;
				boolean disableTaming = false;
				boolean modelGrowing = true;
				
				if (jsonEle.isJsonObject()) {
					JsonObject jsonObj = jsonEle.getAsJsonObject();
					name = JsonUtils.getString(jsonObj, "name");
					disableTaming = JsonUtils.getBoolean(jsonObj, "disable_taming");
					modelGrowing = JsonUtils.getBoolean(jsonObj, "model_growing");
				} else {
					name = jsonEle.getAsString();
				}
				Class<? extends Entity> entityClass = EntityList.getClass(new ResourceLocation(name));
				if (entityClass != null) {
					if (EntityAnimal.class.isAssignableFrom(entityClass)
							&& !HungryAnimalManager.getInstance().isRegistered(entityClass.asSubclass(EntityAnimal.class))) {
						ResourceLocation resource = EntityList.getKey(entityClass);
						HungryAnimals.logger.info("[Configuration] registered " + resource);
						HungryAnimalManager.getInstance().register(entityClass.asSubclass(EntityAnimal.class), disableTaming, modelGrowing);
					}
				}
			}

			HungryAnimals.logger.info("[Configuration] following entities are compatible, but not registered");
			for (ResourceLocation key : EntityList.getEntityNameList()) {
				Class<? extends Entity> i = EntityList.getClass(key);
				if (i != null && EntityAnimal.class.isAssignableFrom(i) && !HungryAnimalManager.getInstance().isRegistered(i.asSubclass(EntityAnimal.class))) {
					// Lightening bolt is null
					HungryAnimals.logger.info("[Configuration] " + key.toString());
				}
			}
		});
		disease = new ConfigurationHandlerJSON(basefolder, "disease", (jsonElement) -> {
			JsonObject jsonObj = (JsonObject) jsonElement;

			PotionDisease.multiplyMovementSpeed = JsonUtils.getFloat(jsonObj, "multiply_movement_speed");
			PotionDisease.multiplyWeightBMR = JsonUtils.getFloat(jsonObj, "multiply_weight_bmr");
		});
		overeat = new ConfigurationHandlerJSON(basefolder, "overeat", (jsonElement) -> {
			JsonObject jsonObj = (JsonObject) jsonElement;

			PotionOvereat.multiplyMovementSpeed = JsonUtils.getFloat(jsonObj, "multiply_movement_speed");
		});

		ModLootTables.init(basefolder);
	}

	public static void syncPre() {
		disease.sync();
		overeat.sync();
	}

	public static void sync() {
		animal.sync();
		foodPreferencesBlock.sync();
		foodPreferencesItem.sync();
		foodPreferencesEntity.sync();
		attributes.sync();
		lootTables.sync();
		ais.sync();
		recipes.sync();
		world.sync();
		cures.sync();
		inheat.sync();
		generators.sync();
		productions.sync();
		ModLootTables.sync();
	}

	public static void syncPost() {
	}

	public static class Serializer implements JsonDeserializer<ItemStack> {
		public ItemStack deserialize(JsonElement ele, Type type, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonobject = ele.getAsJsonObject();

			String name = JsonUtils.getString(jsonobject, "name");
			Item item = Item.REGISTRY.getObject(new ResourceLocation(name));

			if (item == null) {
				throw new JsonParseException(String.format("{} has wrong name. It cannot find item {}", ele, name));
			}

			if (JsonUtils.hasField(jsonobject, "damage") && JsonUtils.hasField(jsonobject, "count")) {
				return new ItemStack(item, JsonUtils.getInt(jsonobject, "count"), JsonUtils.getInt(jsonobject, "damage"));
			} else if (JsonUtils.hasField(jsonobject, "damage")) {
				return new ItemStack(item, 1, JsonUtils.getInt(jsonobject, "damage"));
			} else if (JsonUtils.hasField(jsonobject, "count")) {
				return new ItemStack(item, JsonUtils.getInt(jsonobject, "count"));
			} else {
				return new ItemStack(item);
			}
		}
	}

	public static String resourceLocationToString(ResourceLocation location) {
		String stringLocation = location.toString();
		return stringLocation.replace(':', '#').replace('/', '@');
	}

	public static ResourceLocation stringToResourceLocation(String location) {
		return new ResourceLocation(location.replace('#', ':').replace('@', '/'));
	}

	private static void createExample(File basefolder) throws IOException, URISyntaxException {
		HAPlugins.getInstance().walkPlugins((path, jsonElement) -> {
			Path parent;
			if (path.getParent() != null) {
				parent = Paths.get(basefolder.toString(), path.getParent().toString());
			} else {
				parent = basefolder.toPath();
			}
			try {
				Files.createDirectories(parent);
			} catch (FileAlreadyExistsException e) {
			} catch (IOException e) {
				HungryAnimals.logger.error("Couldn\'t create folder {}\n{}", parent, e);
			}

			Path target = Paths.get(basefolder.toString(), path.toString());
			try {
				Files.createFile(target);
			} catch (FileAlreadyExistsException e) {
			} catch (IOException e) {
				HungryAnimals.logger.error("Couldn\'t create file {}\n{}", target, e);
			}
			try {
				String beautiful = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
				Files.write(target, Lists.newArrayList(beautiful));
			} catch (IOException e) {
				HungryAnimals.logger.error("Couldn\'t create write {}\n{}", target, e);
			}
		}, (path, txt) -> {
			Path parent;
			if (path.getParent() != null) {
				parent = Paths.get(basefolder.toString(), path.getParent().toString());
			} else {
				parent = basefolder.toPath();
			}
			try {
				Files.createDirectories(parent);
			} catch (FileAlreadyExistsException e) {
			} catch (IOException e) {
				HungryAnimals.logger.error("Couldn\'t create folder {}\n{}", parent, e);
			}

			Path target = Paths.get(basefolder.toString(), path.toString());
			try {
				Files.createFile(target);
			} catch (FileAlreadyExistsException e) {
			} catch (IOException e) {
				HungryAnimals.logger.error("Couldn\'t create file {}\n{}", target, e);
			}
			try {
				Files.write(target, Lists.newArrayList(txt));
			} catch (IOException e) {
				HungryAnimals.logger.error("Couldn\'t create write {}\n{}", target, e);
			}
		});
		/*
		 * String[] directories = new String[] { "", "/ais", "/attributes",
		 * "/food_preferences/block", "/food_preferences/entity",
		 * "/food_preferences/item", "/loot_tables/entities" }; String prefix =
		 * "/assets/hungryanimals";
		 * 
		 * for (String directory : directories) { // Prepare for directory walk
		 * URI uri = ConfigurationHandler.class.getResource(prefix +
		 * directory).toURI(); if (uri.getScheme().equals("jar")) { FileSystem
		 * fileSystem = FileSystems.newFileSystem(uri, Collections.<String,
		 * Object>emptyMap()); Path myPath = fileSystem.getPath(prefix +
		 * directory); walkAndCopy(basefolder, directory, myPath);
		 * fileSystem.close(); } else { Path myPath = Paths.get(uri);
		 * walkAndCopy(basefolder, directory, myPath); }
		 * 
		 * }
		 */
	}

}

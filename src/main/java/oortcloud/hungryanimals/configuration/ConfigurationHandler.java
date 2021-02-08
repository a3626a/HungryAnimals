package oortcloud.hungryanimals.configuration;

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
import com.google.common.collect.Sets;
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
import net.minecraft.entity.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.API;
import oortcloud.hungryanimals.api.HAPlugins;
import oortcloud.hungryanimals.block.ExcretaBlock;
import oortcloud.hungryanimals.block.NiterBedBlock;
import oortcloud.hungryanimals.configuration.master.*;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.ai.handler.AIContainers;
import oortcloud.hungryanimals.entities.ai.handler.IAIContainer;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceEntity;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceFluid;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceIngredient;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceIngredient.FoodPreferenceIngredientEntry;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.handler.Cures;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager.HungryAnimalEntry;
import oortcloud.hungryanimals.entities.handler.InHeats;
import oortcloud.hungryanimals.entities.loot_tables.ModLootTables;
import oortcloud.hungryanimals.entities.production.IProduction;
import oortcloud.hungryanimals.entities.production.Productions;
import oortcloud.hungryanimals.generation.GrassGenerator;
import oortcloud.hungryanimals.generation.GrassGenerators;
import oortcloud.hungryanimals.items.SlingShotItem;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.potion.PotionDisease;
import oortcloud.hungryanimals.potion.PotionOvereat;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue;
import oortcloud.hungryanimals.utils.HashBlockState;
import oortcloud.hungryanimals.utils.ModJsonUtils;
import oortcloud.hungryanimals.utils.Pair;
import oortcloud.hungryanimals.utils.R;

public class ConfigurationHandler {

	private static ConfigurationHandlerJSONAnimal foodPreferencesBlock;
	private static ConfigurationHandlerJSONAnimal foodPreferencesItem;
	private static ConfigurationHandlerJSONAnimal foodPreferencesEntity;
	private static ConfigurationHandlerJSONAnimal foodPreferencesFluid;
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
	private static ConfigurationHandlerJSON slingshot;

	public static Node baked;
	public static NodeCache example;
	public static Node master;
	public static Map<R, JsonElement> map;

	public static Path exampleFolder;
	public static Path baseFolder;

	public static Gson GSON_INSTANCE_ITEM_STACK = new GsonBuilder().registerTypeAdapter(ItemStack.class, new ConfigurationHandler.Serializer()).create();

	public static void init(FMLPreInitializationEvent event) {
		baseFolder = event.getModConfigurationDirectory().toPath().resolve(References.MODID);
		exampleFolder = event.getModConfigurationDirectory().toPath().resolve(References.MODID + "_example");

		// Master Config Graph
		master = new NodeOverride(new NodePath(baseFolder, baseFolder.resolve("master")), new NodePlugin(Paths.get("master")));

		// Config Graph
		example = new NodeCache(new NodeModifier(
				new NodeArrayMerger(
						new NodeModLoaded(new NodePlugin(), Sets.newHashSet("animania")),
						"animal/minecraft/animal.json",
						"animal/animania/animal.json",
						"animal.json"
				),
				Master.get(master, "default")));
		baked = new NodeOverride(new NodeModifier(new NodePath(baseFolder), Master.get(master, "custom")), example);

		foodPreferencesBlock = new ConfigurationHandlerJSONAnimal(baseFolder, "food_preferences/block", (jsonElement, animal) -> {
			JsonArray jsonArr = (JsonArray) jsonElement;

			Map<HashBlockState, Pair<Double, Double>> map = new HashMap<HashBlockState, Pair<Double, Double>>();
			for (JsonElement i : jsonArr) {
				JsonObject jsonObj = i.getAsJsonObject();
				HashBlockState state = HashBlockState.parse(jsonObj.getAsJsonObject("block"));
				double nutrient = JSONUtils.getFloat(jsonObj, "nutrient");
				double stomach = JSONUtils.getFloat(jsonObj, "stomach");
				map.put(state, new Pair<Double, Double>(nutrient, stomach));
			}
			FoodPreferences.getInstance().REGISTRY_BLOCK.put(animal, new FoodPreferenceBlockState(map));
		});
		foodPreferencesItem = new ConfigurationHandlerJSONAnimal(baseFolder, "food_preferences/item", (jsonElement, animal) -> {
			JsonArray jsonArr = (JsonArray) jsonElement;

			List<FoodPreferenceIngredientEntry> list = new ArrayList<FoodPreferenceIngredientEntry>();
			for (JsonElement i : jsonArr) {
				JsonObject jsonObj = i.getAsJsonObject();
				Ingredient ing = ModJsonUtils.getIngredient(jsonObj.get("item"));
				double nutrient = JSONUtils.getFloat(jsonObj, "nutrient");
				double stomach = JSONUtils.getFloat(jsonObj, "stomach");

				if (ing != null) {
					list.add(new FoodPreferenceIngredientEntry(ing, nutrient, stomach));
				} else {
					HungryAnimals.logger.error("{} is not a valid item food preference", i);
				}
			}
			FoodPreferences.getInstance().REGISTRY_ITEM.put(animal, new FoodPreferenceIngredient(list));
		});
		foodPreferencesEntity = new ConfigurationHandlerJSONAnimal(baseFolder, "food_preferences/entity", (jsonElement, animal) -> {
			JsonArray jsonArr = (JsonArray) jsonElement;

			Set<Class<? extends MobEntity>> set = new HashSet<Class<? extends MobEntity>>();
			for (JsonElement i : jsonArr) {
				String resourceLocation = i.getAsString();
				Class<? extends Entity> entityClass = EntityList.getClass(new ResourceLocation(resourceLocation));
				if (entityClass != null) {
					set.add(entityClass.asSubclass(MobEntity.class));
				} else {
					HungryAnimals.logger.error("cannot find the animal {}", resourceLocation);
				}
			}
			FoodPreferences.getInstance().getRegistryEntity().put(animal, new FoodPreferenceEntity(set));
		});
		
		
		foodPreferencesFluid = new ConfigurationHandlerJSONAnimal(baseFolder, "food_preferences/fluid", (jsonElement, animal) -> {
			JsonArray jsonArr = (JsonArray) jsonElement;

			Map<String, Pair<Double, Double>> map = new HashMap<String, Pair<Double, Double>>();
			for (JsonElement i : jsonArr) {
				JsonObject jsonObj = i.getAsJsonObject();
				String fluid = JSONUtils.getString(jsonObj, "fluid");
				double nutrient = JSONUtils.getFloat(jsonObj, "nutrient");
				double stomach = JSONUtils.getFloat(jsonObj, "stomach");
				map.put(fluid, new Pair<Double, Double>(nutrient, stomach));
			}
			FoodPreferences.getInstance().REGISTRY_FLUID.put(animal, new FoodPreferenceFluid(map));
		});
		attributes = new ConfigurationHandlerJSONAnimal(baseFolder, "attributes", (jsonElement, animal) -> {
			JsonObject jsonObj = (JsonObject) jsonElement;

			for (Entry<String, JsonElement> i : jsonObj.entrySet()) {
				if (i.getValue().isJsonObject()) {
					// Custom shouldRegister
					JsonObject jsonAttribute = i.getValue().getAsJsonObject();
					boolean shouldRegister = JSONUtils.getBoolean(jsonAttribute, "should_register");
					double value = JSONUtils.getFloat(jsonAttribute, "value");
					API.registerAttribute(animal, i.getKey(), value, shouldRegister);
				} else {
					// Default shouldRegister
					double value = i.getValue().getAsDouble();
					API.registerAttribute(animal, i.getKey(), value);
				}
			}
		});

		ais = new ConfigurationHandlerJSONAnimal(baseFolder, "ais", (jsonElement, animal) -> {
			IAIContainer<MobEntity> aiContainer = AIContainers.getInstance().parse(animal, jsonElement);
			AIContainers.getInstance().register(animal, aiContainer);
		});
		productions = new ConfigurationHandlerJSONAnimal(baseFolder, "productions", (jsonElement, animal) -> {
			JsonArray jsonArr = jsonElement.getAsJsonArray();

			for (JsonElement i : jsonArr) {
				Function<MobEntity, IProduction> factory = Productions.getInstance().parse(i);
				if (factory != null) {
					API.registerProduction(animal, factory);
				} else {
					HungryAnimals.logger.error("{} is not a valid Production", i);
				}
			}
		});

		recipes = new ConfigurationHandlerJSON(baseFolder, "recipes/animalglue", (jsonElement) -> {
			JsonArray jsonArr = (JsonArray) jsonElement;

			for (JsonElement i : jsonArr) {
				JsonObject jsonObj = i.getAsJsonObject();
				Ingredient ing = ModJsonUtils.getIngredient(jsonObj.get("item"));
				int count = jsonObj.getAsJsonPrimitive("count").getAsInt();

				if (ing != null) {
					RecipeAnimalGlue.addRecipe(ing, count);
				} else {
					HungryAnimals.logger.error("{} is not a valid animal glue recipe", i);
				}
			}
		});
		world = new ConfigurationHandlerJSON(baseFolder, "world", (jsonElement) -> {
			JsonObject jsonObj = (JsonObject) jsonElement;

			ExcretaBlock.diseaseProbability = jsonObj.getAsJsonPrimitive("disease_probability").getAsDouble();
			ExcretaBlock.erosionProbabilityOnHay = jsonObj.getAsJsonPrimitive("erosion_probability_on_hay").getAsDouble();
			ExcretaBlock.erosionProbability = jsonObj.getAsJsonPrimitive("erosion_probability").getAsDouble();
			ExcretaBlock.fermetationProbability = jsonObj.getAsJsonPrimitive("fermentation_probability").getAsDouble();
			ExcretaBlock.fertilizationProbability = jsonObj.getAsJsonPrimitive("fertilization_probability").getAsDouble();
			NiterBedBlock.ripeningProbability = jsonObj.getAsJsonPrimitive("ripening_probability").getAsDouble();
		});
		cures = new ConfigurationHandlerJSON(baseFolder, "cures", (jsonElement) -> {
			List<Ingredient> ingredients = ModJsonUtils.getIngredients(jsonElement);
			for (Ingredient i : ingredients)
				Cures.getInstance().register(i);
		});
		inheat = new ConfigurationHandlerJSON(baseFolder, "inheat", (jsonElement) -> {
			JsonArray jsonArr = (JsonArray) jsonElement;

			for (JsonElement i : jsonArr) {
				JsonObject obj = i.getAsJsonObject();
				Ingredient ing = ModJsonUtils.getIngredient(obj.get("item"));
				int inheatDuration = JSONUtils.getInt(obj, "duration");

				if (ing != null) {
					InHeats.getInstance().register(ing, inheatDuration);
				} else {
					HungryAnimals.logger.error("{} is not a valid inheat item", i);
				}
			}
		});
		generators = new ConfigurationHandlerJSON(baseFolder, "generators", (jsonElement) -> {
			JsonArray jsonArr = (JsonArray) jsonElement;

			for (JsonElement jsonEle : jsonArr) {
				JsonObject jsonObj = jsonEle.getAsJsonObject();
				JsonElement generator = jsonObj.get("generator");
				Biome biome = null;
				if (jsonObj.has("biome")) {
					String biomeName = JSONUtils.getString(jsonObj, "biome");
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
		animal = new ConfigurationHandlerJSON(baseFolder, "animal", (jsonElement) -> {
			JsonArray jsonArr = (JsonArray) jsonElement;

			for (JsonElement jsonEle : jsonArr) {

				String name;

				HungryAnimalEntry entry = new HungryAnimalEntry();
				if (jsonEle.isJsonObject()) {
					JsonObject jsonObj = jsonEle.getAsJsonObject();
					name = JSONUtils.getString(jsonObj, "name");
					if (jsonObj.has("disable_taming")) {
						// Backward compatibility
						entry.isTamable = !JSONUtils.getBoolean(jsonObj, "disable_taming");
					} else if (jsonObj.has("tamable")) {
						entry.isTamable = JSONUtils.getBoolean(jsonObj, "tamable");
					}
					if (jsonObj.has("model_growing")) {
						entry.isModelGrowing = JSONUtils.getBoolean(jsonObj, "model_growing");
					}
					if (jsonObj.has("sexual")) {
						entry.isSexual = JSONUtils.getBoolean(jsonObj, "sexual");
					}
					if (jsonObj.has("ageable")) {
						entry.isAgeable = JSONUtils.getBoolean(jsonObj, "ageable");
					}
					if (jsonObj.has("hungry")) {
						entry.isHungry = JSONUtils.getBoolean(jsonObj, "hungry");
					}
				} else {
					name = jsonEle.getAsString();
				}
				Class<? extends Entity> entityClass = EntityList.getClass(new ResourceLocation(name));
				if (entityClass != null) {
					if (MobEntity.class.isAssignableFrom(entityClass)
							&& !HungryAnimalManager.getInstance().isRegistered(entityClass.asSubclass(MobEntity.class))) {
						ResourceLocation resource = EntityList.getKey(entityClass);
						HungryAnimals.logger.info("[Configuration] registered " + resource);
						HungryAnimalManager.getInstance().register(entityClass.asSubclass(MobEntity.class), entry);
					}
				}
			}

			HungryAnimals.logger.info("[Configuration] following entities are compatible, but not registered");
			for (ResourceLocation key : EntityList.getEntityNameList()) {
				Class<? extends Entity> i = EntityList.getClass(key);
				if (i != null && MobEntity.class.isAssignableFrom(i) && !HungryAnimalManager.getInstance().isRegistered(i.asSubclass(MobEntity.class))) {
					// Lightening bolt is null
					HungryAnimals.logger.info("[Configuration] " + key.toString());
				}
			}
		});
		disease = new ConfigurationHandlerJSON(baseFolder, "disease", (jsonElement) -> {
			JsonObject jsonObj = (JsonObject) jsonElement;

			PotionDisease.multiplyMovementSpeed = JSONUtils.getFloat(jsonObj, "multiply_movement_speed");
			PotionDisease.multiplyWeightBMR = JSONUtils.getFloat(jsonObj, "multiply_weight_bmr");
		});
		overeat = new ConfigurationHandlerJSON(baseFolder, "overeat", (jsonElement) -> {
			JsonObject jsonObj = (JsonObject) jsonElement;

			PotionOvereat.multiplyMovementSpeed = JSONUtils.getFloat(jsonObj, "multiply_movement_speed");
		});

		slingshot = new ConfigurationHandlerJSON(baseFolder, "slingshot", (jsonElement) -> {
			JsonObject jsonObj = (JsonObject) jsonElement;

			JsonElement ammos = jsonObj.get("ammos");
			List<Ingredient> ingredients = ModJsonUtils.getIngredients(ammos);
			for (Ingredient i : ingredients)
				((SlingShotItem) ModItems.SLINGSHOT.get()).ammos.add(i);

			((SlingShotItem) ModItems.SLINGSHOT.get()).damage = JSONUtils.getFloat(jsonObj, "damage");
		});
	}

	public static void syncPre() {
		try {
			map = baked.build();
		} catch (RuntimeException e) {
			HungryAnimals.logger.error(
					"An error occured while preparing configruation system. " +
			        "Loading configuration is aborted. " +
			        "Please read following error log and resolve the problem.");
			e.printStackTrace();

			// map is already null though, explicitly set to null.
			map = null;
		}

		if (map != null) {
			createDirectories(baseFolder, example.cached);
			createExample(exampleFolder, example.cached);

			disease.sync(map);
			overeat.sync(map);
			
			FoodPreferences.getInstance().setRegistryEntityLoader(()->foodPreferencesEntity.sync(map));
		}
	}

	public static void sync() {
		if (map != null) {
			animal.sync(map);
			foodPreferencesBlock.sync(map);
			foodPreferencesItem.sync(map);
			foodPreferencesFluid.sync(map);
			attributes.sync(map);
			lootTables.sync(map);
			ais.sync(map);
			recipes.sync(map);
			world.sync(map);
			cures.sync(map);
			inheat.sync(map);
			generators.sync(map);
			productions.sync(map);
			slingshot.sync(map);
		}
	}

	public static void syncPost() {
		if (map != null) {
			
		}
	}

	public static class Serializer implements JsonDeserializer<ItemStack> {
		public ItemStack deserialize(JsonElement ele, Type type, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonobject = ele.getAsJsonObject();

			String name = JSONUtils.getString(jsonobject, "name");
			Item item = Item.REGISTRY.getObject(new ResourceLocation(name));

			if (item == null) {
				throw new JsonParseException(String.format("{} has wrong name. It cannot find item {}", ele, name));
			}

			if (JSONUtils.hasField(jsonobject, "damage") && JSONUtils.hasField(jsonobject, "count")) {
				return new ItemStack(item, JSONUtils.getInt(jsonobject, "count"), JSONUtils.getInt(jsonobject, "damage"));
			} else if (JSONUtils.hasField(jsonobject, "damage")) {
				return new ItemStack(item, 1, JSONUtils.getInt(jsonobject, "damage"));
			} else if (JSONUtils.hasField(jsonobject, "count")) {
				return new ItemStack(item, JSONUtils.getInt(jsonobject, "count"));
			} else {
				return new ItemStack(item);
			}
		}
	}

	public static Path resourceLocationToPath(ResourceLocation location, String ext) {
		String domain = location.getResourceDomain();
		String path = location.getResourcePath();
		String[] paths = path.split("/");
		paths[paths.length - 1] += "." + ext;
		return Paths.get(domain, paths);
	}

	private static void createDirectories(Path rootDirectory, Map<R, JsonElement> map) {
		for (Entry<R, JsonElement> i : map.entrySet()) {
			Path directory;
			R path = i.getKey();
			R parent = path.getParent();
			if (parent != null) {
				directory = rootDirectory.resolve(parent.toString());
			} else {
				directory = rootDirectory;
			}
			try {
				Files.createDirectories(directory);
			} catch (FileAlreadyExistsException e) {
			} catch (IOException e) {
				HungryAnimals.logger.error("Couldn\'t create folder {}\n{}", directory, e);
			}
		}
	}

	private static void createExample(Path rootDirectory, Map<R, JsonElement> map) {
		for (Entry<R, JsonElement> i : map.entrySet()) {
			Path directory;
			R path = i.getKey();
			R parent = path.getParent();
			if (parent != null) {
				directory = rootDirectory.resolve(parent.toString());
			} else {
				directory = rootDirectory;
			}
			try {
				Files.createDirectories(directory);
			} catch (FileAlreadyExistsException e) {
			} catch (IOException e) {
				HungryAnimals.logger.error("Couldn\'t create folder {}\n{}", directory, e);
			}

			Path target = rootDirectory.resolve(path.toString());
			try {
				Files.createFile(target);
			} catch (FileAlreadyExistsException e) {
			} catch (IOException e) {
				HungryAnimals.logger.error("Couldn\'t create file {}\n{}", target, e);
			}
			try {
				String beautiful = new GsonBuilder().setPrettyPrinting().create().toJson(i.getValue());
				Files.write(target, Lists.newArrayList(beautiful));
			} catch (IOException e) {
				HungryAnimals.logger.error("Couldn\'t create write {}\n{}", target, e);
			}
		}
		
		try {
			HAPlugins.getInstance().walkPlugins(null, (path, txt) -> {
				Path directory;
				R parent = path.getParent();
				if (parent != null) {
					directory = rootDirectory.resolve(parent.toString());
				} else {
					directory = rootDirectory;
				}
				try {
					Files.createDirectories(directory);
				} catch (FileAlreadyExistsException e) {
				} catch (IOException e) {
					HungryAnimals.logger.error("Couldn\'t create folder {}\n{}", parent, e);
				}

				Path target = rootDirectory.resolve(path.toString());
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
		} catch (IOException | URISyntaxException e) {
			HungryAnimals.logger.warn("Problem occured durin creating example config folder.");
			e.printStackTrace();
		}
	}

}

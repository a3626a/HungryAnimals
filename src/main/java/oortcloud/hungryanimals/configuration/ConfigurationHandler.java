package oortcloud.hungryanimals.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.API;
import oortcloud.hungryanimals.blocks.BlockExcreta;
import oortcloud.hungryanimals.blocks.BlockNiterBed;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIContainerTask;
import oortcloud.hungryanimals.entities.ai.handler.AIContainers;
import oortcloud.hungryanimals.entities.ai.handler.EventAIContainerRegister;
import oortcloud.hungryanimals.entities.ai.handler.IAIContainer;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.event.EntityEventHandler.Pair;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceEntity;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceIngredient;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceIngredient.FoodPreferenceIngredientEntry;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.food_preferences.HungryAnimalRegisterEvent;
import oortcloud.hungryanimals.entities.handler.Cures;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;
import oortcloud.hungryanimals.entities.handler.InHeats;
import oortcloud.hungryanimals.entities.loot_tables.ModLootTables;
import oortcloud.hungryanimals.generation.GrassGenerator;
import oortcloud.hungryanimals.generation.GrassGenerators;
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
	private static ConfigurationHandlerJSON recipes;
	private static ConfigurationHandlerJSON world;
	private static ConfigurationHandlerJSON animal;
	private static ConfigurationHandlerJSON cures;
	private static ConfigurationHandlerJSON inheat;
	private static ConfigurationHandlerJSON generators;

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

		foodPreferencesBlock = new ConfigurationHandlerJSONAnimal(basefolder, "food_preferences/block", (text, animal) -> {
			JsonArray jsonArr;
			try {
				jsonArr = (new JsonParser()).parse(text).getAsJsonArray();
			} catch (JsonSyntaxException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { foodPreferencesBlock.getDescriptor(), text, animal, e });
				return;
			}
			Map<HashBlockState, Pair<Double, Double>> map = new HashMap<HashBlockState, Pair<Double, Double>>();
			for (JsonElement i : jsonArr) {
				JsonObject jsonObj = i.getAsJsonObject();
				HashBlockState state = HashBlockState.parse(jsonObj.getAsJsonObject("block"));
				double nutrient = jsonObj.getAsJsonPrimitive("nutrient").getAsDouble();
				double stomach = jsonObj.getAsJsonPrimitive("stomach").getAsDouble();
				map.put(state, new Pair<Double, Double>(nutrient, stomach));
			}
			HungryAnimalRegisterEvent.FoodPreferenceBlockStateRegisterEvent event_ = new HungryAnimalRegisterEvent.FoodPreferenceBlockStateRegisterEvent(animal,
					map);
			MinecraftForge.EVENT_BUS.post(event_);
			FoodPreferences.getInstance().REGISTRY_BLOCK.put(animal, new FoodPreferenceBlockState(map));
		});
		foodPreferencesItem = new ConfigurationHandlerJSONAnimal(basefolder, "food_preferences/item", (text, animal) -> {
			JsonArray jsonArr;
			try {
				jsonArr = (new JsonParser()).parse(text).getAsJsonArray();
			} catch (JsonSyntaxException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { foodPreferencesItem.getDescriptor(), text, animal, e });
				return;
			}
			List<FoodPreferenceIngredientEntry> list = new ArrayList<FoodPreferenceIngredientEntry>();
			for (JsonElement i : jsonArr) {
				JsonObject jsonObj = i.getAsJsonObject();
				Ingredient ing = ModJsonUtils.getIngredient(jsonObj.get("item"));
				double nutrient = jsonObj.getAsJsonPrimitive("nutrient").getAsDouble();
				double stomach = jsonObj.getAsJsonPrimitive("stomach").getAsDouble();
				list.add(new FoodPreferenceIngredientEntry(ing, nutrient, stomach));
			}
			HungryAnimalRegisterEvent.FoodPreferenceItemStackRegisterEvent event_ = new HungryAnimalRegisterEvent.FoodPreferenceItemStackRegisterEvent(animal,
					list);
			MinecraftForge.EVENT_BUS.post(event_);
			FoodPreferences.getInstance().REGISTRY_ITEM.put(animal, new FoodPreferenceIngredient(list));
		});
		foodPreferencesEntity = new ConfigurationHandlerJSONAnimal(basefolder, "food_preferences/entity", (text, animal) -> {
			JsonArray jsonArr;
			try {
				jsonArr = (new JsonParser()).parse(text).getAsJsonArray();
			} catch (JsonSyntaxException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { foodPreferencesItem.getDescriptor(), text, animal, e });
				return;
			}
			Set<Class<? extends EntityLiving>> set = new HashSet<Class<? extends EntityLiving>>();
			for (JsonElement i : jsonArr) {
				String resourceLocation = i.getAsString();
				Class<? extends Entity> entityClass = EntityList.getClass(new ResourceLocation(resourceLocation));
				set.add(entityClass.asSubclass(EntityLiving.class));
			}
			FoodPreferences.getInstance().REGISTRY_ENTITY.put(animal, new FoodPreferenceEntity(set));
		});
		attributes = new ConfigurationHandlerJSONAnimal(basefolder, "attributes", (text, animal) -> {
			JsonObject jsonObj;
			try {
				jsonObj = (new JsonParser()).parse(text).getAsJsonObject();
			} catch (JsonSyntaxException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { attributes.getDescriptor(), text, animal, e });
				return;
			}

			for (Entry<String, JsonElement> i : jsonObj.entrySet()) {
				if (!ModAttributes.getInstance().ATTRIBUTES.containsKey(i.getKey())) {
					HungryAnimals.logger.warn("Couldn\'t load {} {} of {}", new Object[] { attributes.getDescriptor(), i, animal });
					continue;
				}
				IAttribute attribute = ModAttributes.getInstance().ATTRIBUTES.get(i.getKey()).attribute;
				boolean shouldRegister = ModAttributes.getInstance().ATTRIBUTES.get(i.getKey()).shouldRegister;
				API.registerAttribute(animal, attribute, i.getValue().getAsDouble(), shouldRegister);
			}
			// TODO Event 
			// AttributeRegisterEvent event_ = new AttributeRegisterEvent(animal, list);
			// MinecraftForge.EVENT_BUS.post(event_);
			// ModAttributes.getInstance().REGISTRY.put(animal, list);
		});

		lootTables = new ConfigurationHandlerJSONAnimal(basefolder, "loot_tables/entities", (file, animal) -> {
		});
		ais = new ConfigurationHandlerJSONAnimal(basefolder, "ais", (text, animal) -> {
			JsonElement jsonEle;
			try {
				jsonEle = (new JsonParser()).parse(text);
			} catch (JsonSyntaxException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {} of {}\n{}", new Object[] { ais.getDescriptor(), text, animal, e });
				return;
			}
			IAIContainer<EntityAnimal> aiContainer = AIContainers.getInstance().parse(jsonEle);
			if (aiContainer instanceof AIContainerTask) {
				MinecraftForge.EVENT_BUS.post(new EventAIContainerRegister(animal, (AIContainerTask) aiContainer));
			} else if (aiContainer instanceof AIContainer) {
				MinecraftForge.EVENT_BUS.post(new EventAIContainerRegister(animal, ((AIContainer) aiContainer).getTask()));
			}
			AIContainers.getInstance().register(animal, aiContainer);
		});

		recipes = new ConfigurationHandlerJSON(new File(basefolder, "recipes"), "animalglue", (text) -> {
			JsonArray jsonArr;
			try {
				jsonArr = (new JsonParser()).parse(text).getAsJsonArray();
			} catch (JsonSyntaxException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {}\n{}", new Object[] { recipes.getDescriptor(), text, e });
				return;
			}
			for (JsonElement i : jsonArr) {
				JsonObject jsonObj = i.getAsJsonObject();
				Ingredient ing = ModJsonUtils.getIngredient(jsonObj.get("item"));
				int count = jsonObj.getAsJsonPrimitive("count").getAsInt();
				RecipeAnimalGlue.addRecipe(ing, count);
			}
		});
		world = new ConfigurationHandlerJSON(basefolder, "world", (text) -> {
			JsonObject jsonObj;
			try {
				jsonObj = (new JsonParser()).parse(text).getAsJsonObject();
			} catch (JsonSyntaxException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {}\n{}", new Object[] { world.getDescriptor(), text, e });
				return;
			}
			BlockExcreta.diseaseProbability = jsonObj.getAsJsonPrimitive("disease_probability").getAsDouble();
			BlockExcreta.erosionProbabilityOnHay = jsonObj.getAsJsonPrimitive("erosion_probability_on_hay").getAsDouble();
			BlockExcreta.erosionProbability = jsonObj.getAsJsonPrimitive("erosion_probability").getAsDouble();
			BlockExcreta.fermetationProbability = jsonObj.getAsJsonPrimitive("fermentation_probability").getAsDouble();
			BlockExcreta.fertilizationProbability = jsonObj.getAsJsonPrimitive("fertilization_probability").getAsDouble();
			BlockNiterBed.ripeningProbability = jsonObj.getAsJsonPrimitive("ripening_probability").getAsDouble();
		});
		cures = new ConfigurationHandlerJSON(basefolder, "cures", (text) -> {
			List<Ingredient> ingredients = ModJsonUtils.getIngredients((new JsonParser()).parse(text));
			for (Ingredient i : ingredients)
				Cures.getInstance().register(i);
		});
		inheat = new ConfigurationHandlerJSON(basefolder, "inheat", (text) -> {
			JsonArray jsonArr;
			try {
				jsonArr = (new JsonParser()).parse(text).getAsJsonArray();
			} catch (JsonSyntaxException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {}\n{}", new Object[] { world.getDescriptor(), text, e });
				return;
			}

			for (JsonElement jsonEle : jsonArr) {
				JsonElement item = jsonEle.getAsJsonObject().get("item");
				Ingredient inheat = ModJsonUtils.getIngredient(item);
				int inheatDuration = JsonUtils.getInt(jsonEle.getAsJsonObject(), "duration");
				InHeats.getInstance().register(inheat, inheatDuration);
			}
		});
		generators = new ConfigurationHandlerJSON(basefolder, "generators", (text) -> {
			JsonArray jsonArr;
			try {
				jsonArr = (new JsonParser()).parse(text).getAsJsonArray();
			} catch (JsonSyntaxException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {}\n{}", new Object[] { world.getDescriptor(), text, e });
				return;
			}

			for (JsonElement jsonEle : jsonArr) {
				JsonObject jsonObj = jsonEle.getAsJsonObject();
				Biome biome = null;
				if (jsonObj.has("biome")) {
					String biomeName = JsonUtils.getString(jsonObj, "biome");
					biome = GameRegistry.findRegistry(Biome.class).getValue(new ResourceLocation(biomeName));
					if (biome == null) {
						throw new JsonSyntaxException(biomeName);
					}
				}
				JsonElement generator = jsonObj.get("generator");
				GrassGenerators.getInstance().register(biome, GrassGenerator.parse(generator));
			}
		});
		animal = new ConfigurationHandlerJSON(basefolder, "animal", (text) -> {
			JsonArray jsonArr;
			try {
				jsonArr = (new JsonParser()).parse(text).getAsJsonArray();
			} catch (JsonSyntaxException e) {
				HungryAnimals.logger.error("Couldn\'t load {} {}\n{}", new Object[] { animal.getDescriptor(), text, e });
				return;
			}

			for (JsonElement jsonEle : jsonArr) {
				String i = jsonEle.getAsString();
				Class<? extends Entity> entityClass = EntityList.getClass(new ResourceLocation(i));
				if (entityClass != null) {
					if (EntityAnimal.class.isAssignableFrom(entityClass)
							&& !HungryAnimalManager.getInstance().isRegistered(entityClass.asSubclass(EntityAnimal.class))) {
						ResourceLocation name = EntityList.getKey(entityClass);
						HungryAnimals.logger.info("[Configuration] registered " + name);
						HungryAnimalManager.getInstance().register(entityClass.asSubclass(EntityAnimal.class));
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

		ModLootTables.init(basefolder);
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
		ModLootTables.sync();
	}

	public static void postSync() {
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
		return stringLocation.replace(':', '#');
	}

	public static ResourceLocation stringToResourceLocation(String location) {
		return new ResourceLocation(location.replace('#', ':'));
	}

	private static void createExample(File basefolder) throws IOException, URISyntaxException {
		String[] directories = new String[] { "", "/ais", "/attributes", "/food_preferences/block", "/food_preferences/entity", "/food_preferences/item",
				"/loot_tables/entities" };
		String prefix = "/assets/hungryanimals";

		for (String directory : directories) {
			// Prepare for directory walk
			URI uri = ConfigurationHandler.class.getResource(prefix + directory).toURI();
			if (uri.getScheme().equals("jar")) {
				FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
				Path myPath = fileSystem.getPath(prefix + directory);
				walkAndCopy(basefolder, directory, myPath);
				fileSystem.close();
			} else {
				Path myPath = Paths.get(uri);
				walkAndCopy(basefolder, directory, myPath);
			}

		}
	}

	private static void walkAndCopy(File basefolder, String directory, Path path) throws IOException {
		Stream<Path> walk = Files.walk(path, 1);

		// Create Directory
		File parent = new File(basefolder.toString() + directory);
		if (!parent.exists()) {
			try {
				Files.createDirectories(parent.toPath());
			} catch (IOException e) {
				HungryAnimals.logger.error("Couldn\'t create folder {}\n{}", directory, e);
				walk.close();
				return;
			}
		}
		// Create Example Files
		for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
			Path i = it.next();
			if (!Files.isDirectory(i)) {
				String textExample = new String(Files.readAllBytes(i));
				File target = new File(parent, i.getFileName().toString());
				target.createNewFile();
				FileWriter o = new FileWriter(target);
				o.write(textExample);
				o.close();
			}
		}

		walk.close();
	}

}

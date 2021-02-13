package oortcloud.hungryanimals.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainers;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.loot_tables.ModLootTables;
import oortcloud.hungryanimals.entities.production.Productions;
import oortcloud.hungryanimals.generation.Conditions;
import oortcloud.hungryanimals.utils.R;

public class HAPlugins {

	private static final String[] EXTENTIONS = { ".txt", ".css", ".html" };

	private static HAPlugins INSTANCE;

	private List<IHAPlugin> plugins;
	private Map<R, JsonElement> mapJson;
	private Map<R, String> mapText;

	public HAPlugins() {
		plugins = new ArrayList<>();
		mapJson = new HashMap<>();
		mapText = new HashMap<>();
	}

	public static HAPlugins getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HAPlugins();
		}
		return INSTANCE;
	}

	public void init(FMLPreInitializationEvent event) {
		plugins = getModPlugins(event.getAsmData());
		HungryAnimals.logger.info("[API] started injecting json files from following mods {}", plugins);
		try {
			injectJson();
		} catch (IOException | URISyntaxException e) {
			HungryAnimals.logger.error("[API] Failed to inject json");
		}

		for (IHAPlugin i : plugins) {
			i.registerAIs(AIContainers.getInstance());
			// TODO Singleton Mod Loot Tables
			i.registerLootTables(ModLootTables::register);
			i.registerProductions(Productions.getInstance());
		}
	}

	private void injectJson() throws IOException, URISyntaxException {
		List<FileSystem> fileSystemClose = new ArrayList<>();
		List<Stream<Path>> walkClose = new ArrayList<>();

		Map<R, List<String>> map = new HashMap<>();

		for (IHAPlugin i : plugins) {
			String injectionPath = i.getJsonInjectionPath();
			URI root = i.getClass().getResource(injectionPath).toURI();
			Path myPath = null;
			if (root.getScheme().equals("jar")) {
				FileSystem fileSystem = FileSystems.newFileSystem(root, Maps.newHashMap());
				myPath = fileSystem.getPath(injectionPath);
				fileSystemClose.add(fileSystem);
			} else {
				myPath = Paths.get(root);
			}

			Stream<Path> walk = Files.walk(myPath);
			walkClose.add(walk);

			for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
				Path j = it.next();
				if (!Files.isDirectory(j)) {
					String text = new String(Files.readAllBytes(j));
					Path relative = myPath.relativize(j);
					R key = R.get(relative);
					if (!map.containsKey(key)) {
						map.put(key, new ArrayList<>());
					}
					map.get(key).add(text);
				}
			}
		}

		for (Entry<R, List<String>> i : map.entrySet()) {
			if (i.getKey().toString().endsWith(".json")) {
				JsonArray jsonArray = null;
				JsonObject jsonObject = null;
				for (String j : i.getValue()) {
					JsonElement json = (new JsonParser()).parse(j);
					if (json.isJsonArray()) {
						if (jsonArray == null) {
							jsonArray = json.getAsJsonArray();
						} else {
							for (JsonElement jsonEle : json.getAsJsonArray()) {
								jsonArray.add(jsonEle);
							}
						}
					} else if (json.isJsonObject()) {
						if (jsonObject == null) {
							jsonObject = json.getAsJsonObject();
						} else {
							for (Entry<String, JsonElement> e : json.getAsJsonObject().entrySet()) {
								jsonObject.add(e.getKey(), e.getValue());
							}
						}
					}
				}
				if (jsonArray != null) {
					putJson(i.getKey(), jsonArray);
				} else if (jsonObject != null) {
					putJson(i.getKey(), jsonObject);
				} else {
					HungryAnimals.logger.warn("{} is neither json object nor json array.", i.getKey());
				}
			} else {
				boolean isSupportingExtension = false;
				for (String ext : EXTENTIONS) {
					if (i.getKey().toString().endsWith(ext)) {
						isSupportingExtension = true;
						break;
					}
				}
				if (isSupportingExtension) {
					String concatenated = "";
					for (String j : i.getValue()) {
						concatenated += j;
						concatenated += System.lineSeparator();
					}
					putText(i.getKey(), concatenated);
				}
			}
		}

		for (FileSystem i : fileSystemClose) {
			i.close();
		}
		for (Stream<Path> i : walkClose) {
			i.close();
		}
	}

	public void walkPlugins(BiConsumer<R, JsonElement> onjson, BiConsumer<R, String> ontxt) throws IOException, URISyntaxException {
		if (onjson != null) {
			for (Entry<R, JsonElement> i : mapJson.entrySet()) {
				onjson.accept(i.getKey(), i.getValue());
			}
		}
		if (ontxt != null) {
			for (Entry<R, String> i : mapText.entrySet()) {
				ontxt.accept(i.getKey(), i.getValue());
			}
		}
	}

	public void putJson(R key, JsonElement value) {
		mapJson.put(key, value);
	}

	public void putText(R key, String value) {
		mapText.put(key, value);
	}

	public JsonElement getJson(R key) {
		return mapJson.get(key);
	}

	public String getText(R key) {
		return mapText.get(key);
	}

	public static List<IHAPlugin> getModPlugins(ASMDataTable asmDataTable) {
		return getInstances(asmDataTable, HAPlugin.class, IHAPlugin.class);
	}

	@SuppressWarnings("rawtypes")
	private static <T> List<T> getInstances(ASMDataTable asmDataTable, Class annotationClass, Class<T> instanceClass) {
		String annotationClassName = annotationClass.getCanonicalName();
		Set<ASMDataTable.ASMData> asmDatas = asmDataTable.getAll(annotationClassName);
		List<T> instances = new ArrayList<>();
		for (ASMDataTable.ASMData asmData : asmDatas) {
			try {
				Class<?> asmClass = Class.forName(asmData.getClassName());
				Class<? extends T> asmInstanceClass = asmClass.asSubclass(instanceClass);
				T instance = asmInstanceClass.newInstance();
				instances.add(instance);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | LinkageError e) {
				HungryAnimals.logger.error("Failed to load: {}\n{}", asmData.getClassName(), e);
			}
		}
		return instances;
	}

}

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import oortcloud.hungryanimals.HungryAnimals;

public class HAPlugins {

	private static HAPlugins INSTANCE;

	private List<IHAPlugin> plugins;
	private Map<Path, JsonElement> mapJson;
	private Map<Path, String> mapTxt;

	public HAPlugins() {
		plugins = new ArrayList<IHAPlugin>();
		mapJson = new HashMap<Path, JsonElement>();
		mapTxt = new HashMap<Path, String>();
	}

	public static HAPlugins getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HAPlugins();
		}
		return INSTANCE;
	}

	public void init(FMLPreInitializationEvent event) {
		plugins = getModPlugins(event.getAsmData());
		HungryAnimals.logger.info("[Configuration] started injecting json files from following mods {}", plugins);
		try {
			injectJson();
		} catch (IOException | URISyntaxException e) {
			HungryAnimals.logger.error("Failed to inject json");
		}
	}

	private void injectJson() throws IOException, URISyntaxException {
		List<FileSystem> fileSystemClose = new ArrayList<>();
		List<Stream<Path>> walkClose = new ArrayList<>();

		Map<Path, List<String>> map = new HashMap<Path, List<String>>();

		for (IHAPlugin i : plugins) {
			String injectionPath = i.getJsonInjectionPath();
			URI root = i.getClass().getResource(injectionPath).toURI();
			Path myPath = null;
			if (root.getScheme().equals("jar")) {
				FileSystem fileSystem = FileSystems.newFileSystem(root, Collections.<String, Object>emptyMap());
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
					if (!map.containsKey(relative)) {
						map.put(relative, new ArrayList<>());
					}
					map.get(relative).add(text);
				}
			}
		}

		for (Entry<Path, List<String>> i : map.entrySet()) {
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
					mapJson.put(i.getKey(), jsonArray);
				} else if (jsonObject != null) {
					mapJson.put(i.getKey(), jsonObject);
				} else {
					// TODO Warn or Error
				}
			} else if (i.getKey().toString().endsWith(".txt")) {
				String concated = "";
				for (String j : i.getValue()) {
					concated += j;
					concated += "\n";
				}
				mapTxt.put(i.getKey(), concated);
			}
		}

		for (FileSystem i : fileSystemClose) {
			i.close();
		}
		for (Stream<Path> i : walkClose) {
			i.close();
		}
	}

	public void walkPlugins(BiConsumer<Path, JsonElement> onjson, BiConsumer<Path, String> ontxt) throws IOException, URISyntaxException {
		for (Entry<Path, JsonElement> i : mapJson.entrySet()) {
			onjson.accept(i.getKey(), i.getValue());
		}
		for (Entry<Path, String> i : mapTxt.entrySet()) {
			ontxt.accept(i.getKey(), i.getValue());
		}
	}

	public JsonElement getJson(Path target) {
		HungryAnimals.logger.info(mapJson);
		HungryAnimals.logger.info("target : {}, conatins? : {}", target, mapJson.containsKey(target));
		
		return mapJson.get(target);
	}

	public String getTxt(Path target) {
		return mapTxt.get(target);
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

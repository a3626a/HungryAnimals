package oortcloud.hungryanimals.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import oortcloud.hungryanimals.HungryAnimals;

public class HAPlugins {

	private static HAPlugins INSTANCE;
	
	private List<IHAPlugin> plugins;
	
	public HAPlugins() {
		plugins = new ArrayList<IHAPlugin>();
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
		injectJson();
	}
	
	private void injectJson() {
		for (IHAPlugin i : plugins) {
			HungryAnimals.logger.info(i.getClass().getResource(i.getJsonInjectionPath()));
			// walk resource folders and inject json
		}
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

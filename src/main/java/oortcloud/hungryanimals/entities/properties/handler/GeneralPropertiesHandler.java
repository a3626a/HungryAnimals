package oortcloud.hungryanimals.entities.properties.handler;

import java.util.HashMap;

public class GeneralPropertiesHandler {

	private static GeneralPropertiesHandler instance;
	
	public HashMap<Class, GeneralProperty> propertyMap;
	
	public static GeneralPropertiesHandler getInstance() {
		if (instance == null) {
			instance = new GeneralPropertiesHandler();
		}
		return instance;
	}
	
	private GeneralPropertiesHandler() {
		propertyMap = new HashMap<Class, GeneralProperty>();
	}
	
	public void init() {
		propertyMap.clear();
	}
}

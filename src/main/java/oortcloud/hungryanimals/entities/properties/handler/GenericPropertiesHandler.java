package oortcloud.hungryanimals.entities.properties.handler;

import java.util.HashMap;

public class GenericPropertiesHandler {

	private static GenericPropertiesHandler instance;
	
	public HashMap<Class, GenericProperty> propertyMap;
	
	public static GenericPropertiesHandler getInstance() {
		if (instance == null) {
			instance = new GenericPropertiesHandler();
		}
		return instance;
	}
	
	private GenericPropertiesHandler() {
		propertyMap = new HashMap<Class, GenericProperty>();
	}
}

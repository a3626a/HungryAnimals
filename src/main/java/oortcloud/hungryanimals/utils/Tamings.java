package oortcloud.hungryanimals.utils;

import javax.annotation.Nullable;

import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.TamingLevel;

public class Tamings {

	public static double get(@Nullable ICapabilityTamableAnimal cap) {
		if (cap == null)
			return 2.0;
		return cap.getTaming();
	}
	
	public static TamingLevel getLevel(@Nullable ICapabilityTamableAnimal cap) {
		if (cap == null)
			return TamingLevel.TAMED;
		return cap.getTamingLevel();
	}
	
}

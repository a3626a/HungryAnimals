package oortcloud.hungryanimals.entities.capability;

public enum TamingLevel {
    WILD, NEUTRAL, TAMED;
	
	public static TamingLevel fromTaming(double taming) {
		if (taming < -1) {
			return WILD;
		} else if (taming < 1) {
			return NEUTRAL;
		} else {
			return TAMED;
		}
	}
	
}

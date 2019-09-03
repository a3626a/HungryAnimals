package oortcloud.hungryanimals.entities.capability;

public interface ICapabilitySexual {

	public static enum Sex {
		MALE,
		FEMALE
	}
	
	public Sex getSex();
	public void setSex(Sex sex);
	
}

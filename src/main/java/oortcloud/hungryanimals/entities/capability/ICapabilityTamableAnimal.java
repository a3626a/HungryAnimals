package oortcloud.hungryanimals.entities.capability;

public interface ICapabilityTamableAnimal {

	public double getTaming();
	public TamingLevel getTamingLevel();
	public double setTaming(double taming);
	public double addTaming(double taming);

}

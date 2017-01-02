package oortcloud.hungryanimals.entities.properties;

public interface IFoodPreference<T> {

	public boolean canEat(T food);
	public double getHunger(T food);
	
}

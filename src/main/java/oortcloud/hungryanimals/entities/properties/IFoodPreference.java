package oortcloud.hungryanimals.entities.properties;

import java.util.Iterator;

public interface IFoodPreference<T> {

	public boolean canEat(T food);
	public double getHunger(T food);
	public Iterable<T> getFoods();
	
}

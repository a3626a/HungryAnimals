package oortcloud.hungryanimals.entities.production;

public interface IProductionTickable extends IProduction {

	/**
	 * called every sec (20 ticks)
	 */
	public void update();
}

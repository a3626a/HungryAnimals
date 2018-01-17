package oortcloud.hungryanimals.entities.production.utils;

import net.minecraft.entity.passive.EntityAnimal;

public class RangeRandom implements IRange {
	
	private int min;
	private int max;
	
	public RangeRandom(int min, int max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public int get(EntityAnimal animal) {
		int rand = animal.getRNG().nextInt(max-min+1);
		return min+rand;
	}

}

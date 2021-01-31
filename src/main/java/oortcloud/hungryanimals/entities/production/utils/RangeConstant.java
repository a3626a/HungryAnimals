package oortcloud.hungryanimals.entities.production.utils;

import net.minecraft.entity.MobEntity;

public class RangeConstant implements IRange {

	private int constant;
	
	public RangeConstant(int constant) {
		this.constant = constant;
	}
	
	public int get(MobEntity animal) {
		return constant;
	}
	
}

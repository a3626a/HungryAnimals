package oortcloud.hungryanimals.entities.production.utils;

import net.minecraft.entity.EntityLiving;

public class RangeConstant implements IRange {

	private int constant;
	
	public RangeConstant(int constant) {
		this.constant = constant;
	}
	
	public int get(EntityLiving animal) {
		return constant;
	}
	
}

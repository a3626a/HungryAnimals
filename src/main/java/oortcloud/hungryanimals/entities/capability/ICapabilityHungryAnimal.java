package oortcloud.hungryanimals.entities.capability;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public interface ICapabilityHungryAnimal {
		
	public double getHunger();
	public double addHunger(double hunger);
	public double setHunger(double hunger);
	public double getMaxHunger();
	
	public double getExcretion();
	public double addExcretion(double excretion);
	public double setExcretion(double excretion);
	
}

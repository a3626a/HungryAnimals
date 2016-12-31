package oortcloud.hungryanimals.entities.capability;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public interface ICapabilityHungryAnimal {
	
	public double getTaming();
	public double setTaming(double taming);
	
	public double getHunger();
	public double setHunger(double hunger);
	
	public double getExcretion();
	public double setExcretion(double excretion);

	// ICapabiltityEventReciever
	public void onUpdate();
	public void onAttackedByPlayer(float damage, DamageSource source);
	public boolean interact(EntityPlayer entity);
	public void dropFewItems(boolean isHitByPlayer, int looting, List<EntityItem> drops);
	
}

package oortcloud.hungryanimals.entities.capability;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.PacketEntityClient;
import oortcloud.hungryanimals.core.network.SyncIndex;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.render.RenderEntityWeight;
import oortcloud.hungryanimals.potion.ModPotions;

public class CapabilityHungryAnimal implements ICapabilityHungryAnimal {

	public static Method setScale = ReflectionHelper.findMethod(EntityAgeable.class, "setScale", "func_98055_j", float.class);
	
	private double excretion;
	private double stomach; 
	private double nutrient;
	private double weight; 
	
	private boolean prevIsFull;
	private int prevWeight;
	
	private EntityAnimal entity;

	public CapabilityHungryAnimal() {}
	
	public CapabilityHungryAnimal(EntityAnimal entity) {
		this.entity = entity;
		setStomach(0.0);
		setNutrient(0.0);
		setWeight(getNormalWeight());
		setExcretion(0.0);
	}
	

	@Override
	public double getNutrient() {
		return nutrient;
	}

	@Override
	public double addNutrient(double nutrient) {
		double oldNutrient = getNutrient();
		setNutrient(getNutrient() + nutrient);
		return oldNutrient;
	}

	@Override
	public double setNutrient(double nutrient) {
		double oldNutrient = this.nutrient;
		if (nutrient < 0) {
			this.nutrient = 0;
		} else {
			this.nutrient = nutrient;
		}
		return oldNutrient;
	}

	@Override
	public double getStomach() {
		return stomach;
	}

	@Override
	public double addStomach(double stomach) {
		double oldStomach = getStomach();
		setStomach(getStomach() + stomach);
		if (stomach < 0) {
			addExcretion(-stomach * entity.getEntityAttribute(ModAttributes.excretion_factor).getAttributeValue());
		}
		return oldStomach;
	}

	@Override
	public double setStomach(double stomach) {
		double oldStomach = this.stomach;
		if (stomach < 0) {
			this.stomach = 0;
		} else {
			this.stomach = stomach;
		}
		boolean currIsFull = getStomach() >= getMaxStomach();
		
		if (currIsFull != prevIsFull) {
			sync();
			if (currIsFull && !entity.isPotionActive(ModPotions.potionOvereat)) {
				entity.addPotionEffect(new PotionEffect(ModPotions.potionOvereat, Integer.MAX_VALUE, 0, false, false));
			}
		}
		prevIsFull = currIsFull;
		
		return oldStomach;
	}

	@Override
	public double getMaxStomach() {
		return entity.getEntityAttribute(ModAttributes.hunger_stomach_max).getAttributeValue();
	}
	
	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public double addWeight(double weight) {
		double oldWeight = getWeight();
		setWeight(getWeight() + weight);
		return oldWeight;
	}

	@Override
	public double setWeight(double weight) {
		double oldWeight = this.weight;
		if (weight > getMaxWeight()) {
			this.weight = getMaxWeight();
		} else if (weight < 0) {
			this.weight = 0;
		} else {
			this.weight = weight;
		}
		
		int currWeight = (int) getWeight();
		if (currWeight != prevWeight) {
			updateSize();
			sync();
		}
		prevWeight = currWeight;
		
		return oldWeight;
	}
	
	private void updateSize() {
		float ratio = (float) RenderEntityWeight.getRatio(entity);
		
		if (entity.getGrowingAge() < 0) {
			ratio *= 0.5;
		}
		
		try {
			setScale.invoke(entity, ratio);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			HungryAnimals.logger.warn("Problem occured during change entity bounding box. Please report to mod author(oortcloud).");
			e.printStackTrace();
		}
	}
	
	@Override
	public double getStarvinglWeight() {
		return getNormalWeight()*0.5;
	}
	
	@Override
	public double getNormalWeight() {
		int age;
		if (entity.getEntityWorld() == null) {
			age = 0;
		} else {
			age = entity.getGrowingAge();
		}
		double hungerWeightNormal = entity.getEntityAttribute(ModAttributes.hunger_weight_normal).getAttributeValue();
		if (age < 0) {
			age = -age;
			double growingLength = entity.getEntityAttribute(ModAttributes.child_growing_length).getAttributeValue();
			double a = age/growingLength;
			double hungerWeightNormalChild = entity.getEntityAttribute(ModAttributes.hunger_weight_normal_child).getAttributeValue();
			return a*hungerWeightNormalChild+(1-a)*hungerWeightNormal;
		} else {
			return hungerWeightNormal;
		}
	}
	
	@Override
	public double getMaxWeight() {
		return getNormalWeight()*2;
	}

	@Override
	public double getExcretion() {
		return excretion;
	}

	@Override
	public double addExcretion(double excretion) {
		return setExcretion(getExcretion()+excretion);
	}

	@Override
	public double setExcretion(double excretion) {
		double oldExcretion = this.excretion;
		this.excretion = excretion;
		return oldExcretion;
	}

	public void sync() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			WorldServer world = (WorldServer) entity.getEntityWorld();
			for (EntityPlayer i : world.getEntityTracker().getTrackingPlayers(entity)) {
				PacketEntityClient packet = new PacketEntityClient(SyncIndex.HUNGRY_SYNC, entity);
				packet.setDouble(getStomach());
				packet.setDouble(getWeight());
				HungryAnimals.simpleChannel.sendTo(packet, (EntityPlayerMP) i);
			}
		}
	}

	public void syncTo(EntityPlayerMP target) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			PacketEntityClient packet = new PacketEntityClient(SyncIndex.HUNGRY_SYNC, entity);
			packet.setDouble(getStomach());
			packet.setDouble(getWeight());
			HungryAnimals.simpleChannel.sendTo(packet, target);
		}
	}
	
}

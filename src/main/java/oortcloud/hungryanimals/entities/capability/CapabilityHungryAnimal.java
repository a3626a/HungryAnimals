package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.fml.network.PacketDistributor;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.ModPacketHandler;
import oortcloud.hungryanimals.core.network.PacketClientSyncHungry;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.potion.ModPotions;

public class CapabilityHungryAnimal implements ICapabilityHungryAnimal {

	private double excretion;
	private double stomach; 
	private double nutrient;
	private double weight; 
	
	private boolean prevIsFull;
	private int prevWeight;
	
	private MobEntity entity;

	public CapabilityHungryAnimal() {}
	
	public CapabilityHungryAnimal(MobEntity entity) {
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
			addExcretion(-stomach * entity.getAttribute(ModAttributes.EXCRETION_FACTOR.get().attribute).getValue());
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
				entity.addPotionEffect(new EffectInstance(ModPotions.potionOvereat, Integer.MAX_VALUE, 0, false, false));
			}
		}
		prevIsFull = currIsFull;
		
		return oldStomach;
	}

	@Override
	public double getMaxStomach() {
		return entity.getAttribute(ModAttributes.HUNGER_STOMACH_MAX.get().attribute).getValue();
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
			sync();
		}
		prevWeight = currWeight;
		
		return oldWeight;
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
			ICapabilityAgeable capAgeable = entity.getCapability(ProviderAgeable.CAP).orElse(null);
			if (capAgeable == null) {
				age = 0;
			} else {
				age = capAgeable.getAge();
			}
		}
		double hungerWeightNormal = entity.getAttribute(ModAttributes.HUNGER_WEIGHT_NORMAL.get().attribute).getValue();
		if (age < 0) {
			age = -age;
			double growingLength = entity.getAttribute(ModAttributes.CHILD_GROWING_LENGTH.get().attribute).getValue();
			double a = age/growingLength;
			double hungerWeightNormalChild = entity.getAttribute(ModAttributes.HUNGER_WEIGHT_NORMAL_CHILD.get().attribute).getValue();
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
		if (!entity.getEntityWorld().isRemote) {
			ModPacketHandler.INSTANCE.send(
					PacketDistributor.TRACKING_ENTITY.with(() -> entity),
					new PacketClientSyncHungry(entity.getEntityId(), getStomach(), getWeight())
			);
		}
	}

	public void syncTo(ServerPlayerEntity target) {
		if (!entity.getEntityWorld().isRemote) {
			ModPacketHandler.INSTANCE.send(
					PacketDistributor.PLAYER.with(()->target),
					new PacketClientSyncHungry(entity.getEntityId(), getStomach(), getWeight())
			);
		}
	}
}

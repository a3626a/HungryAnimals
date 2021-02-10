package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.AgeableEntity;

public class CapabilityAgeableSub implements ICapabilityAgeable {

	private AgeableEntity entity;
	
	public CapabilityAgeableSub() {
	}
	
	public CapabilityAgeableSub(AgeableEntity entity) {
		this.entity = entity;
	}
	
	@Override
	public int getAge() {
		return entity.getGrowingAge();
	}

	@Override
	public void setAge(int age) {
		entity.setGrowingAge(age);;
	}

}

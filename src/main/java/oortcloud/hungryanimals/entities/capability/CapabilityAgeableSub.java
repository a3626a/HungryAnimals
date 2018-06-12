package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.EntityAgeable;

public class CapabilityAgeableSub implements ICapabilityAgeable {

	private EntityAgeable entity;
	
	public CapabilityAgeableSub() {
	}
	
	public CapabilityAgeableSub(EntityAgeable entity) {
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

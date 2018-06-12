package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.EntityLiving;

public class CapabilityAgeable implements ICapabilityAgeable {

	private EntityLiving entity;
	
	public CapabilityAgeable() {
	}
	
	public CapabilityAgeable(EntityLiving entity) {
		this.entity = entity;
	}
	
	@Override
	public int getAge() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAge(int age) {
		// TODO Auto-generated method stub
	}

}

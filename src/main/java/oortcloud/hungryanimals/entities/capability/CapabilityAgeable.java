package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.MobEntity;

public class CapabilityAgeable implements ICapabilityAgeable {

	private MobEntity entity;
	
	public CapabilityAgeable() {
	}
	
	public CapabilityAgeable(MobEntity entity) {
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

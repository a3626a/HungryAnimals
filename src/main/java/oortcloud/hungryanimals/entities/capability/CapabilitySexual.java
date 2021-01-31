package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.MobEntity;

public class CapabilitySexual implements ICapabilitySexual {

	private Sex sex;
	protected MobEntity entity;
	
	public CapabilitySexual() {}
	
	public CapabilitySexual(MobEntity entity) {
		this.entity = entity;
		if (entity.getRNG().nextBoolean()) {
			setSex(Sex.FEMALE);
		} else {
			setSex(Sex.MALE);
		}
	}
	
	@Override
	public Sex getSex() {
		return sex;
	}
	
	@Override
	public void setSex(Sex sex) {
		this.sex = sex;
	}
	
}

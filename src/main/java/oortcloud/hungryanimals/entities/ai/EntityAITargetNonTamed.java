package oortcloud.hungryanimals.entities.ai;

import net.minecraft.entity.passive.EntityTameable;

public class EntityAITargetNonTamed extends EntityAITarget {

	private EntityTameable tameable;

	public EntityAITargetNonTamed(EntityTameable tameable, boolean checkSight, boolean herding) {
		super(tameable, 10, checkSight, false, herding);
		this.tameable = tameable;
	}

	@Override
	public boolean shouldExecute() {
		return !tameable.isTamed() && super.shouldExecute();
	}

}

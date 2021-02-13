package oortcloud.hungryanimals.entities.attributes;

import net.minecraft.entity.LivingEntity;

public interface IAttributeEntry {
	void apply(LivingEntity entity);
	void register(LivingEntity entity);
}

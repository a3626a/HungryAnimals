package oortcloud.hungryanimals.entities.attributes;

import net.minecraft.entity.EntityLivingBase;

public interface IAttributeEntry {
	public void apply(EntityLivingBase entity);
	public void register(EntityLivingBase entity);
}

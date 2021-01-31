package oortcloud.hungryanimals.entities.attributes;

import net.minecraft.entity.MobEntityBase;

public interface IAttributeEntry {
	public void apply(MobEntityBase entity);
	public void register(MobEntityBase entity);
}

package oortcloud.hungryanimals.entities.attributes;

import net.minecraft.entity.MobEntityBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

public class AttributeEntry implements IAttributeEntry {

	public IAttribute attribute;
	public boolean shouldRegister;
	public double value;

	public AttributeEntry(IAttribute attribute, boolean shouldRegister, double value) {
		this.attribute = attribute;
		this.shouldRegister = shouldRegister;
		this.value = value;
	}
	
	@Override
	public void apply(MobEntityBase entity) {
		entity.getEntityAttribute(attribute).setBaseValue(value);
	}

	@Override
	public void register(MobEntityBase entity) {
		IAttributeInstance attributeInstance = entity.getEntityAttribute(attribute);
		if (attributeInstance == null && shouldRegister) {
			entity.getAttributeMap().registerAttribute(attribute);
			entity.getEntityAttribute(attribute).setBaseValue(value);
		}
	}

}

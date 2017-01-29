package oortcloud.hungryanimals.entities.attributes;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;

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
	public void apply(EntityLivingBase entity) {
		entity.getAttributeMap().getAttributeInstance(attribute).setBaseValue(value);
	}

	@Override
	public void register(EntityLivingBase entity) {
		if (entity.getAttributeMap().getAttributeInstance(attribute) == null && shouldRegister) {
			entity.getAttributeMap().registerAttribute(attribute);
		}
	}

}

package oortcloud.hungryanimals.entities.properties;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.properties.handler.HungryAnimalManager;
import oortcloud.hungryanimals.entities.properties.handler.AnimalCharacteristic;
import oortcloud.hungryanimals.entities.properties.handler.ModAttributes;

public class ExtendedPropertiesHungrySheep extends ExtendedPropertiesHungryAnimal {

	public EntitySheep entity;
	public int wool;

	@Override
	public void init(Entity entity, World world) {
		super.init(entity, world);
		this.entity = (EntitySheep) entity;
	}
	
	@Override
	public void postInit() {
		super.postInit();
		this.removeAI(new Class[] { EntityAIEatGrass.class });
		this.wool = (int) this.entity.getAttributeMap().getAttributeInstance(ModAttributes.wool_delay).getAttributeValue();
	}
	
	@Override
	public void update() {
		if (!this.worldObj.isRemote) {
			if (this.wool > 0 && this.entity.getSheared()) {
				this.wool--;
			}
			if (this.wool == 0 && this.entity.getSheared()) {
				this.entity.setSheared(false);
				this.subHunger(entity.getAttributeMap().getAttributeInstance(ModAttributes.wool_hunger).getAttributeValue());
				this.wool = (int)entity.getAttributeMap().getAttributeInstance(ModAttributes.wool_delay).getAttributeValue();
			}
		}
		super.update();
	}

	@Override
	public void dropFewItems(boolean isHitByPlayer, int looting, List<EntityItem> drops) {
		super.dropFewItems(isHitByPlayer, looting, drops);
		if (entity.isBurning()) {
			for (EntityItem i : drops) {
				if (i.getEntityItem().getItem() == Items.mutton) {
					i.setEntityItemStack(new ItemStack(Items.cooked_mutton, i.getEntityItem().stackSize));
				}
			}
		}
		if (this.entity.getGrowingAge() >= 0) {
			if (!this.entity.getSheared()) {
				this.entity.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), (int)((2 + this.entity.getRNG().nextInt(2))* (1+(entity.getRNG().nextInt(1 + looting)) / 3.0)),
						this.entity.getFleeceColor().getMetadata()), 0.0F);
			}
		}
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound tag = new NBTTagCompound();
		compound.setTag(key, tag);
		tag.setDouble("hunger", this.hunger);
		tag.setDouble("excretion", this.excretion);
		tag.setDouble("tamedValue", this.taming);
		tag.setInteger("wool", this.wool);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound tag = (NBTTagCompound) compound.getTag(key);
		if (tag != null) {
			this.hunger = tag.getDouble("hunger");
			this.excretion = tag.getDouble("excretion");
			this.taming = tag.getDouble("tamedValue");
			this.wool = tag.getInteger("wool");
		}
	}

}

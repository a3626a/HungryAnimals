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
import oortcloud.hungryanimals.entities.properties.handler.GeneralPropertiesHandler;
import oortcloud.hungryanimals.entities.properties.handler.GeneralProperty;

public class ExtendedPropertiesHungrySheep extends ExtendedPropertiesHungryAnimal {

	public EntitySheep entity;
	public int wool;

	public static int wool_delay;
	public static double wool_hunger;

	public static int default_wool_delay;
	public static double default_wool_hunger;

	@Override
	public void init(Entity entity, World world) {
		super.init(entity, world);
		this.entity = (EntitySheep) entity;

		acceptProperty(((GeneralProperty)GeneralPropertiesHandler.getInstance().propertyMap.get(entity.getClass())));
		
		taming_factor = 0.998;
		wool_delay = default_wool_delay;
		wool_hunger = default_wool_hunger;
		this.hunger = this.hunger_max / 2.0;
		this.excretion = 0;
		this.taming = -2;
		this.wool = this.wool_delay;
	}

	@Override
	public void update() {
		if (!this.worldObj.isRemote) {
			if (this.wool > 0 && this.entity.getSheared()) {
				this.wool--;
			}
			if (this.wool == 0 && this.entity.getSheared()) {
				this.entity.setSheared(false);
				this.subHunger(this.wool_hunger);
				this.wool = this.wool_delay;
			}
		}
		super.update();
	}

	public void postInit() {
		super.postInit();
		this.removeAI(new Class[] { EntityAIEatGrass.class });
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

package oortcloud.hungryanimals.entities.properties;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.properties.handler.GenericPropertiesHandler;
import oortcloud.hungryanimals.entities.properties.handler.GenericProperty;

public class ExtendedPropertiesHungryCow extends ExtendedPropertiesHungryAnimal {

	public EntityCow entity;
	public int milk;

	public static int milk_delay;
	public static double milk_hunger;

	public static int default_milk_delay;
	public static double default_milk_hunger;

	@Override
	public void init(Entity entity, World world) {
		super.init(entity, world);
		this.entity = (EntityCow) entity;

		acceptProperty(((GenericProperty)GenericPropertiesHandler.getInstance().propertyMap.get(entity.getClass())));
		
		taming_factor = 0.998;
		milk_delay = default_milk_delay;
		milk_hunger = default_milk_hunger;
		this.hunger = this.hunger_max / 2.0;
		this.excretion = 0;
		this.taming = -2;
		this.milk = this.milk_delay;
	}

	@Override
	public void update() {
		if (!this.worldObj.isRemote) {
			if (this.milk > 0) {
				this.milk--;
			}
		}
		super.update();
	}

	@Override
	public boolean interact(EntityPlayer entity) {
		ItemStack stack = entity.getCurrentEquippedItem();
		if (stack == null)
			return super.interact(entity);

		Item item = stack.getItem();
		if (stack.getItem() == Items.bucket && this.milk == 0 && this.taming >= 1 && !entity.capabilities.isCreativeMode) {
			if (stack.stackSize-- == 1) {
				entity.inventory.setInventorySlotContents(entity.inventory.currentItem, new ItemStack(Items.milk_bucket));
			} else if (!entity.inventory.addItemStackToInventory(new ItemStack(Items.milk_bucket))) {
				entity.dropPlayerItemWithRandomChoice(new ItemStack(Items.milk_bucket, 1, 0), false);
			}
			this.subHunger(milk_hunger);
			this.milk = this.milk_delay;

			return true;
		}

		if (stack.getItem() == Items.bucket) {
			return true;
		}
		// to avoid vanilla milk production
		
		return super.interact(entity);
	}

	@Override
	protected void applyEntityAttributes() {
		this.entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
		this.entity.heal(this.entity.getMaxHealth());
		this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20D);
	}

	@Override
	public void dropFewItems(boolean isHitByPlayer, int looting, List<EntityItem> drops) {
		super.dropFewItems(isHitByPlayer, looting, drops);
		if (entity.isBurning()) {
			for (EntityItem i : drops) {
				if (i.getEntityItem().getItem() == Items.beef) {
					i.setEntityItemStack(new ItemStack(Items.cooked_beef, i.getEntityItem().stackSize));
				}
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
		tag.setInteger("milk", this.milk);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound tag = (NBTTagCompound) compound.getTag(key);
		if (tag != null) {
			this.hunger = tag.getDouble("hunger");
			this.excretion = tag.getDouble("excretion");
			this.taming = tag.getDouble("tamedValue");
			this.milk = tag.getInteger("milk");
		}
	}

}

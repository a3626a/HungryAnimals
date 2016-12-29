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
import oortcloud.hungryanimals.entities.properties.handler.HungryAnimalManager;
import oortcloud.hungryanimals.entities.properties.handler.AnimalCharacteristic;
import oortcloud.hungryanimals.entities.properties.handler.ModAttributes;

public class ExtendedPropertiesHungryCow extends ExtendedPropertiesHungryAnimal {

	public EntityCow entity;
	public int milk;

	@Override
	public void init(Entity entity, World world) {
		super.init(entity, world);
		this.entity = (EntityCow) entity;
	}

	@Override
	public void acceptProperty() {
		super.acceptProperty();
		this.milk = (int) this.entity.getAttributeMap().getAttributeInstance(ModAttributes.milk_delay).getAttributeValue();
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
		if (stack.getItem() == Items.BUCKET && this.milk == 0 && this.taming >= 1 && !entity.capabilities.isCreativeMode) {
			if (stack.stackSize-- == 1) {
				entity.inventory.setInventorySlotContents(entity.inventory.currentItem, new ItemStack(Items.MILK_BUCKET));
			} else if (!entity.inventory.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET))) {
				entity.dropPlayerItemWithRandomChoice(new ItemStack(Items.MILK_BUCKET, 1, 0), false);
			}
			this.subHunger(entity.getAttributeMap().getAttributeInstance(ModAttributes.milk_hunger).getAttributeValue());
			this.milk = (int) entity.getAttributeMap().getAttributeInstance(ModAttributes.milk_delay).getAttributeValue();

			return true;
		}

		if (stack.getItem() == Items.BUCKET) {
			return true;
		}
		// to avoid vanilla milk production

		return super.interact(entity);
	}

	@Override
	public void dropFewItems(boolean isHitByPlayer, int looting, List<EntityItem> drops) {
		super.dropFewItems(isHitByPlayer, looting, drops);
		if (entity.isBurning()) {
			for (EntityItem i : drops) {
				if (i.getEntityItem().getItem() == Items.BEEF) {
					i.setEntityItemStack(new ItemStack(Items.COOKED_BEEF, i.getEntityItem().stackSize));
				}
			}
		}
	}

	@Override
	protected void loadPropertyNBTData(NBTTagCompound tag) {
		super.loadPropertyNBTData(tag);
		milk = tag.getInteger("milk");
	}

	@Override
	protected void savePropertyNBTData(NBTTagCompound tag) {
		super.savePropertyNBTData(tag);
		tag.setInteger("milk", milk);
	}

}

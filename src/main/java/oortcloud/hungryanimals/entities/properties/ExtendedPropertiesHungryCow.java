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
		if (stack.getItem() == Items.bucket && this.milk == 0 && this.taming >= 1 && !entity.capabilities.isCreativeMode) {
			if (stack.stackSize-- == 1) {
				entity.inventory.setInventorySlotContents(entity.inventory.currentItem, new ItemStack(Items.milk_bucket));
			} else if (!entity.inventory.addItemStackToInventory(new ItemStack(Items.milk_bucket))) {
				entity.dropPlayerItemWithRandomChoice(new ItemStack(Items.milk_bucket, 1, 0), false);
			}
			this.subHunger(entity.getAttributeMap().getAttributeInstance(ModAttributes.milk_hunger).getAttributeValue());
			this.milk = (int) entity.getAttributeMap().getAttributeInstance(ModAttributes.milk_delay).getAttributeValue();

			return true;
		}

		if (stack.getItem() == Items.bucket) {
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
				if (i.getEntityItem().getItem() == Items.beef) {
					i.setEntityItemStack(new ItemStack(Items.cooked_beef, i.getEntityItem().stackSize));
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

package oortcloud.hungryanimals.items;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.core.network.PacketPlayerServer;
import oortcloud.hungryanimals.core.network.SyncIndex;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;

public class ItemDebugGlass extends Item {

	public ItemDebugGlass() {
		super();
		setUnlocalizedName(Strings.itemDebugGlassName);
		setCreativeTab(HungryAnimals.tabHungryAnimals);
		
		setMaxStackSize(1);
		ModItems.register(this);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		if (worldIn.isRemote) {
			Entity entity = Minecraft.getMinecraft().objectMouseOver.entityHit;
			if (entity != null) {
				PacketPlayerServer msg = new PacketPlayerServer(SyncIndex.DEBUG_SETTARGET, playerIn.getName());
				msg.setInt(entity.getEntityId());
				HungryAnimals.simpleChannel.sendToServer(msg);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote) {
			NBTTagCompound tag = stack.getTagCompound();
			if (tag != null && tag.hasKey("target")) {
				Entity target = worldIn.getEntityByID(tag.getInteger("target"));
				if (target != null) {
					ExtendedPropertiesHungryAnimal property = (ExtendedPropertiesHungryAnimal) target.getExtendedProperties(Strings.extendedPropertiesKey);
					if (property != null) {
						tag.setDouble("hunger", property.hunger);
						tag.setDouble("excretion", property.excretion);
						tag.setDouble("taming", property.taming);
						tag.setInteger("age", ((EntityAnimal) target).getGrowingAge());
						if (property.ai_moveToFoodbox.pos != null) {
							tag.setInteger("target_X", property.ai_moveToFoodbox.pos.getX());
							tag.setInteger("target_Y", property.ai_moveToFoodbox.pos.getX());
							tag.setInteger("target_Z", property.ai_moveToFoodbox.pos.getX());
						}
					}
				}
			}
		}
	}
}

package oortcloud.hungryanimals.items;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.core.network.PacketPlayerServer;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;

public class ItemDebugGlass extends Item {

	public ItemDebugGlass() {
		super();
		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemDebugGlassName);
		this.setCreativeTab(HungryAnimals.tabHungryAnimals);
		this.setMaxStackSize(1);
		ModItems.register(this);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

		if (world.isRemote) {
			Entity entity = Minecraft.getMinecraft().objectMouseOver.entityHit;
			if (entity != null) {
				PacketPlayerServer msg = new PacketPlayerServer(0, player.getName());
				msg.setInt(entity.getEntityId());
				HungryAnimals.simpleChannel.sendToServer(msg);
			}
		}

		return stack;
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
						tag.setInteger("target_X", property.ai_moveToFoodbox.x);
						tag.setInteger("target_Y", property.ai_moveToFoodbox.y);
						tag.setInteger("target_Z", property.ai_moveToFoodbox.z);
					}
				}
			}
		}
	}
}

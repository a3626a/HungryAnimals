package oortcloud.hungryanimals.items;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.core.network.PacketPlayerServer;
import oortcloud.hungryanimals.core.network.SyncIndex;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;

public class ItemDebugGlass extends Item {

	public ItemDebugGlass() {
		super();
		setUnlocalizedName(References.MODID + "." + Strings.itemDebugGlassName);
		setRegistryName(Strings.itemDebugGlassName);
		setCreativeTab(HungryAnimals.tabHungryAnimals);

		setMaxStackSize(1);
	}

	@Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
		if (playerIn.getEntityWorld().isRemote) {
			Entity entity = Minecraft.getMinecraft().objectMouseOver.entityHit;
			if (entity != null) {
				PacketPlayerServer msg = new PacketPlayerServer(SyncIndex.DEBUG_SETTARGET, playerIn.getName());
				msg.setInt(entity.getEntityId());
				HungryAnimals.simpleChannel.sendToServer(msg);
				return true;
			}
		}
		return false;
    }

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote) {
			NBTTagCompound tag = stack.getTagCompound();
			if (tag != null && tag.hasKey("target")) {
				Entity target = worldIn.getEntityByID(tag.getInteger("target"));
				if (target != null) {
					if (!(target instanceof EntityAnimal))
						return;

					EntityAnimal entity = (EntityAnimal) target;

					if (entity.hasCapability(ProviderHungryAnimal.CAP, null)) {
						ICapabilityHungryAnimal capHungry = entity.getCapability(ProviderHungryAnimal.CAP, null);
						tag.setDouble("weight", capHungry.getWeight());
						tag.setDouble("nutrient", capHungry.getNutrient());
						tag.setDouble("stomach", capHungry.getStomach());
						tag.setDouble("excretion", capHungry.getExcretion());
					}
					
					if (entity.hasCapability(ProviderTamableAnimal.CAP, null)) {
						ICapabilityTamableAnimal capTaming = entity.getCapability(ProviderTamableAnimal.CAP, null);
						tag.setDouble("taming", capTaming.getTaming());
					}
					
					tag.setInteger("age", ((EntityAnimal) target).getGrowingAge());
				}
			}
		}
	}
}

package oortcloud.hungryanimals.items;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
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
import oortcloud.hungryanimals.core.network.PacketServerDGSet;
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
				PacketServerDGSet msg = new PacketServerDGSet(playerIn, entity);
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

					EntityAnimal animal = (EntityAnimal) target;

					ICapabilityHungryAnimal capHungry = animal.getCapability(ProviderHungryAnimal.CAP, null);
					if (capHungry != null) {
						tag.setDouble("weight", capHungry.getWeight());
						tag.setDouble("nutrient", capHungry.getNutrient());
						tag.setDouble("stomach", capHungry.getStomach());
						tag.setDouble("excretion", capHungry.getExcretion());
					}

					ICapabilityTamableAnimal capTaming = animal.getCapability(ProviderTamableAnimal.CAP, null);
					if (capTaming != null) {
						tag.setDouble("taming", capTaming.getTaming());
					}
					tag.setInteger("age", ((EntityAnimal) target).getGrowingAge());
					
					int index = 0;
					for (EntityAITaskEntry i : animal.tasks.taskEntries) {
						NBTTagCompound iTag = new NBTTagCompound();
						String name = i.action.getClass().toString();
						name = name.substring(name.lastIndexOf(".")+1);
						iTag.setString("name", name);
						iTag.setInteger("priority", i.priority);
						iTag.setBoolean("using", i.using);
						tag.setTag("ais."+index, iTag);
						index += 1;
					}
					tag.setInteger("ais.length", index);
				}
			}
		}
	}
}

package oortcloud.hungryanimals.items;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.core.network.PacketServerDGSet;
import oortcloud.hungryanimals.entities.capability.ProviderAgeable;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;

public class DebugGlassItem extends Item {
	public DebugGlassItem() {
		super(
				new Item.Properties()
						.group(HungryAnimals.tabHungryAnimals)
						.maxStackSize(1)
		);
		setRegistryName(Strings.itemDebugGlassName);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		if (playerIn.getEntityWorld().isRemote) {
			if (Minecraft.getInstance().objectMouseOver.getType() == RayTraceResult.Type.ENTITY)
			{
				Entity entity = ((EntityRayTraceResult)Minecraft.getInstance().objectMouseOver).getEntity();
				if (entity != null) {
					PacketServerDGSet msg = new PacketServerDGSet(playerIn, entity);
					HungryAnimals.simpleChannel.sendToServer(msg);
					return true;
				}
			}

		}
		return false;
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote) {
			CompoundNBT tag = stack.getTag();
			if (tag != null && tag.contains("target")) {
				Entity target = worldIn.getEntityByID(tag.getInt("target"));
				if (target != null) {
					if (!(target instanceof MobEntity))
						return;

					MobEntity animal = (MobEntity) target;

					animal.getCapability(ProviderHungryAnimal.CAP, null).ifPresent(
							(capHungry) -> {
								tag.putDouble("weight", capHungry.getWeight());
								tag.putDouble("nutrient", capHungry.getNutrient());
								tag.putDouble("stomach", capHungry.getStomach());
								tag.putDouble("excretion", capHungry.getExcretion());
							}
					);
					animal.getCapability(ProviderTamableAnimal.CAP, null).ifPresent(
							(capTaming) -> {
								tag.putDouble("taming", capTaming.getTaming());
							}
					);
					animal.getCapability(ProviderAgeable.CAP, null).ifPresent(
							(capAgeable) -> {
								tag.putInt("age", capAgeable.getAge());
							}
					);

					int index = 0;

					for (PrioritizedGoal i : animal.goalSelector.goals) {
						CompoundNBT iTag = new CompoundNBT();
						String name = i.getGoal().getClass().toString();
						name = name.substring(name.lastIndexOf(".")+1);
						iTag.putString("name", name);
						iTag.putInt("priority", i.getPriority());
						iTag.putBoolean("using", i.isRunning());
						tag.put("ais."+index, iTag);
						index += 1;
					}
					tag.putInt("ais.length", index);
				}
			}
		}
	}
}

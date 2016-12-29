package oortcloud.hungryanimals.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.EntityBola;

public class ItemBola extends Item {
	
	public ItemBola() {
		super();
		setUnlocalizedName(Strings.itemBolaName);
		setCreativeTab(HungryAnimals.tabHungryAnimals);

		ModItems.register(this);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int tick) {

		int duration = this.getMaxItemUseDuration(stack) - tick;
		ArrowLooseEvent event = new ArrowLooseEvent(player, stack, duration);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) {
			return;
		}
		if (duration >= 100)
			duration = 100;

		if (!world.isRemote) {
			if (!player.capabilities.isCreativeMode) {
				player.getCurrentEquippedItem().stackSize--;
				if (player.getCurrentEquippedItem().stackSize == 0)
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			}
			EntityBola bola = new EntityBola(world, player, (float) (0.015 * duration));
			world.spawnEntityInWorld(bola);
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {

		player.setItemInUse(item, this.getMaxItemUseDuration(item));

		return item;
	}

}

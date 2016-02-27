package oortcloud.hungryanimals.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.EntitySlingShotBall;
import oortcloud.hungryanimals.items.render.CameraTransformModelItemBola;
import oortcloud.hungryanimals.items.render.SmartModelItemSlingshot;

public class ItemSlingShot extends Item {

	public ItemSlingShot() {
		super();
		setUnlocalizedName(Strings.itemSlingShotName);
		setCreativeTab(HungryAnimals.tabHungryAnimals);
		
		setMaxStackSize(1);
		setMaxDamage(64);
		ModItems.register(this);
	}

	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int itemInUseCount) {
		int useDuration = this.getMaxItemUseDuration(stack) - itemInUseCount;

		ArrowLooseEvent event = new ArrowLooseEvent(player, stack, useDuration);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) {
			return;
		}
		useDuration = event.charge;

		if (player.capabilities.isCreativeMode || player.inventory.hasItem(ItemBlock.getItemFromBlock(Blocks.cobblestone))) {
			float f = (float) useDuration / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if ((double) f < 0.1D) {
				return;
			}

			if (f > 1.0F) {
				f = 1.0F;
			}

			EntitySlingShotBall entityball = new EntitySlingShotBall(world, player, f * 2.0F);

			stack.damageItem(1, player);
			world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

			if (!player.capabilities.isCreativeMode) {
				player.inventory.consumeInventoryItem(ItemBlock.getItemFromBlock(Blocks.cobblestone));
			}

			if (!world.isRemote) {
				world.spawnEntityInWorld(entityball);
			}
		}
	}

	public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_) {
		return p_77654_1_;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return 72000;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		ArrowNockEvent event = new ArrowNockEvent(p_77659_3_, p_77659_1_);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) {
			return event.result;
		}

		if (p_77659_3_.capabilities.isCreativeMode || p_77659_3_.inventory.hasItem(ItemBlock.getItemFromBlock(Blocks.cobblestone))) {
			p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
		}

		return p_77659_1_;
	}

}

package oortcloud.hungryanimals.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.EntityBola;

public class ItemBola extends Item {

	public ItemBola() {
		super();
		setUnlocalizedName(References.MODID+"."+Strings.itemBolaName);
		setRegistryName(Strings.itemBolaName);
		setCreativeTab(HungryAnimals.tabHungryAnimals);
		GameRegistry.register(this);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.NONE;
    }
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {

		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			boolean flag = entityplayer.capabilities.isCreativeMode;

			int duration = this.getMaxItemUseDuration(stack) - timeLeft;

			if (duration >= 100)
				duration = 100;

			if (!world.isRemote) {
				float f = (float) (0.015 * duration);
				EntityBola bola = new EntityBola(world, entityplayer, f);
				world.spawnEntityInWorld(bola);
				
				// TODO check sound
				world.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
				
				if (!flag)
                {
                    --stack.stackSize;

                    if (stack.stackSize == 0)
                    {
                        entityplayer.inventory.deleteStack(stack);
                    }
                }

                entityplayer.addStat(StatList.getObjectUseStats(this));
			}

		}
	}

	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		playerIn.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}

}

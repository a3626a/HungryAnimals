package oortcloud.hungryanimals.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.EntityBola;
import oortcloud.hungryanimals.sound_event.ModSoundEvents;
import oortcloud.hungryanimals.stats.ModStatTypes;

public class BolaItem extends Item {
	public BolaItem() {
		super(
				new Item.Properties()
						.group(HungryAnimals.tabHungryAnimals)
		);
		setRegistryName(Strings.itemBolaName);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return super.getUseDuration(stack);
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.NONE;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity livingEntity, int timeLeft) {

		if (livingEntity instanceof PlayerEntity) {
			PlayerEntity entityplayer = (PlayerEntity) livingEntity;
			boolean flag = entityplayer.abilities.isCreativeMode;

			int duration = this.getUseDuration(stack) - timeLeft;

			if (duration >= 100)
				duration = 100;

			if (!world.isRemote) {
				float f = (float) (0.015 * duration);
				EntityBola bola = new EntityBola(world, entityplayer);
				bola.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f, 1.0F);
				world.addEntity(bola);

				world.playSound((PlayerEntity) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, ModSoundEvents.ENTITY_ARROW_SHOOT.get(),
						SoundCategory.NEUTRAL, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

				if (!flag) {
					stack.shrink(1);
				}

				entityplayer.addStat(ModStatTypes.ITEM_USED.get().get(this));
			}

		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
		playerIn.setActiveHand(hand);
		return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(hand));
	}
}

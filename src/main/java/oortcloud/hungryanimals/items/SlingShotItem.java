package oortcloud.hungryanimals.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.stats.StatType;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.EntitySlingShotBall;

public class SlingShotItem extends Item {
	public List<Ingredient> ammos;
	public float damage;
	
	public SlingShotItem() {
		super(
		        new Item.Properties()
                        .group(HungryAnimals.tabHungryAnimals)
                        .maxStackSize(1)
                        .maxDamage(64)
        );
		setRegistryName(Strings.itemSlingShotName);

		ammos = new ArrayList<Ingredient>();
	}

	private ItemStack findAmmo(PlayerEntity player)
    {
        if (this.isArrow(player.getHeldItem(Hand.OFF_HAND)))
        {
            return player.getHeldItem(Hand.OFF_HAND);
        }
        else if (this.isArrow(player.getHeldItem(Hand.MAIN_HAND)))
        {
            return player.getHeldItem(Hand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isArrow(itemstack))
                {
                    return itemstack;
                }
            }

            return player.abilities.isCreativeMode ? new ItemStack(COBBLESTONE.get()) : ItemStack.EMPTY;
        }
    }

	protected boolean isArrow(ItemStack stack)
    {
		if (stack == null)
			return false;
		
		for (Ingredient i : ammos) {
			if (i.test(stack)) {
				return true;
			}
		}
		
		return false;
    }


    private static RegistryObject<Enchantment> INFINITY = RegistryObject.of(new ResourceLocation("minecraft:infinity"), ForgeRegistries.ENCHANTMENTS);
    private static RegistryObject<Item> COBBLESTONE = RegistryObject.of(new ResourceLocation("minecraft:cobblestone"), ForgeRegistries.ITEMS);
    private static RegistryObject<SoundEvent> ENTITY_ARROW_SHOOT = RegistryObject.of(new ResourceLocation("minecraft:entity.arrow.shoot"), ForgeRegistries.SOUND_EVENTS);
    private static RegistryObject<StatType<Item>> ITEM_USED = RegistryObject.of(new ResourceLocation("minecraft:used"), ForgeRegistries.STAT_TYPES);
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {
        if (entityLiving instanceof PlayerEntity)
        {
            PlayerEntity playerentity = (PlayerEntity)entityLiving;
            boolean flag = playerentity.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(INFINITY.get(), stack) > 0;
            ItemStack itemstack = this.findAmmo(playerentity);

            int i = this.getUseDuration(stack) - timeLeft;
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag)
            {
                if (itemstack.isEmpty())
                {
                    itemstack = new ItemStack(COBBLESTONE.get());
                }

                float f = getArrowVelocity(i);
                if ((double)f >= 0.1D)
                {
                    boolean flag1 = playerentity.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(INFINITY.get(), stack) > 0;
                    
                    if (!worldIn.isRemote)
                    {
                    	EntitySlingShotBall entityball = new EntitySlingShotBall(worldIn, playerentity);
                    	entityball.shoot(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F, f * 2.0F, 1.0F);
                    	entityball.getPersistentData().putFloat("hungryanimals.damage", damage);
                    	stack.damageItem(1, playerentity,(p_220009_1_) -> {
                            p_220009_1_.sendBreakAnimation(playerentity.getActiveHand());
                        });

                        worldIn.addEntity(entityball);
                    }

                    worldIn.playSound((PlayerEntity)null, playerentity.posX, playerentity.posY, playerentity.posZ, ENTITY_ARROW_SHOOT.get(), SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !playerentity.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            playerentity.inventory.deleteStack(itemstack);
                        }
                    }

                    playerentity.addStat(ITEM_USED.get().get(this));
                }
            }
        }
    }
    
	public static float getArrowVelocity(int charge)
    {
        float f = (float)charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        return f;
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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand)
    {
		ItemStack itemStack = playerIn.getHeldItem(hand);
        boolean flag = !this.findAmmo(playerIn).isEmpty();

        if (!playerIn.abilities.isCreativeMode && !flag)
        {
            return flag ? new ActionResult<>(ActionResultType.PASS, itemStack) : new ActionResult<>(ActionResultType.FAIL, itemStack);
        }
        else
        {
            playerIn.setActiveHand(hand);
            return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
        }
    }
	
}

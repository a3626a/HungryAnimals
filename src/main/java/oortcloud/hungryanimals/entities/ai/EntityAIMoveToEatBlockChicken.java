package oortcloud.hungryanimals.entities.ai;

import net.minecraft.block.BlockState;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreference;

@Deprecated
public class EntityAIMoveToEatBlockChicken extends EntityAIMoveToEatBlock {

	private IFoodPreference<ItemStack> prefItem;
	
	public EntityAIMoveToEatBlockChicken(MobEntity entity, double speed) {
		super(entity, speed);
		this.prefItem = FoodPreferences.getInstance().REGISTRY_ITEM.get(entity.getClass());
	}

	@Override
	public void eatBlockBonus(BlockState block) {
		if (block.getBlock() == Blocks.TALLGRASS) {
			double prob = pref.getNutrient(block) / 2.0 / prefItem.getNutrient(new ItemStack(Items.WHEAT_SEEDS));
			if (this.entity.getRNG().nextDouble() < prob) {
				ItemStack stack = new ItemStack(Items.WHEAT_SEEDS);
				CompoundNBT tag = new CompoundNBT();
				tag.setBoolean("isNatural", true);
				stack.setTagCompound(tag);
				ItemEntity entity = new ItemEntity(worldObj, this.entity.posX, this.entity.posY, this.entity.posZ, stack);
				entity.motionY = 0.2;
				worldObj.spawnEntity(entity);
			}
		} else {
			super.eatBlockBonus(block);
		}
	}

}

package oortcloud.hungryanimals.entities.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferences;
import oortcloud.hungryanimals.entities.food_preferences.IFoodPreference;

@Deprecated
public class EntityAIMoveToEatBlockChicken extends EntityAIMoveToEatBlock {

	private IFoodPreference<ItemStack> prefItem;
	
	public EntityAIMoveToEatBlockChicken(EntityLiving entity, double speed) {
		super(entity, speed);
		this.prefItem = FoodPreferences.getInstance().REGISTRY_ITEM.get(entity.getClass());
	}

	@Override
	public void eatBlockBonus(IBlockState block) {
		if (block.getBlock() == Blocks.TALLGRASS) {
			double prob = pref.getNutrient(block) / 2.0 / prefItem.getNutrient(new ItemStack(Items.WHEAT_SEEDS));
			if (this.entity.getRNG().nextDouble() < prob) {
				ItemStack stack = new ItemStack(Items.WHEAT_SEEDS);
				NBTTagCompound tag = new NBTTagCompound();
				tag.setBoolean("isNatural", true);
				stack.setTagCompound(tag);
				EntityItem entity = new EntityItem(worldObj, this.entity.posX, this.entity.posY, this.entity.posZ, stack);
				entity.motionY = 0.2;
				worldObj.spawnEntity(entity);
			}
		} else {
			super.eatBlockBonus(block);
		}
	}

}

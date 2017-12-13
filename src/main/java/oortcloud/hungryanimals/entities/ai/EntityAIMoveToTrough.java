package oortcloud.hungryanimals.entities.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungryanimals.blocks.BlockTrough;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceManager;
import oortcloud.hungryanimals.potion.ModPotions;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;

public class EntityAIMoveToTrough extends EntityAIBase {

	private EntityAnimal entity;
	private double speed;
	private World world;
	public BlockPos pos;
	private int delayCounter;
	private static int delay = 100;
	private ICapabilityHungryAnimal capHungry;
	private ICapabilityTamableAnimal capTaming;
	
	public EntityAIMoveToTrough(EntityAnimal entity, double speed) {
		this.delayCounter = entity.getRNG().nextInt(delay);
		this.capHungry = entity.getCapability(ProviderHungryAnimal.CAP, null);
		this.capTaming = entity.getCapability(ProviderTamableAnimal.CAP, null);
		
		this.entity = entity;
		this.world = this.entity.getEntityWorld();
		this.speed = speed;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (pos == null)
			return false;
		
		if (this.delayCounter > 0) {
			--this.delayCounter;
			return false;
		} else {
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() == ModBlocks.trough) {
				TileEntity temp = ((BlockTrough) state.getBlock()).getTileEntity(world, pos);
				if (this.capTaming.getTaming() >= 1 && temp != null && temp instanceof TileEntityTrough) {
					TileEntityTrough trough = (TileEntityTrough) temp;
					return !trough.stack.isEmpty() && FoodPreferenceManager.getInstance().REGISTRY_ITEM.get(entity.getClass()).canEat(this.capHungry, trough.stack);
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	@Override
	public void startExecuting() {
		this.entity.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), this.speed);
	}

	@Override
	public boolean continueExecuting() {
		float distSq = 2;
		if (pos.distanceSqToCenter(entity.posX, entity.posY, entity.posZ) <= distSq) {
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() == ModBlocks.trough) {
				TileEntity tileEntity = ((BlockTrough) state.getBlock()).getTileEntity(world, pos);
				if (tileEntity != null && tileEntity instanceof TileEntityTrough) {
					TileEntityTrough trough = (TileEntityTrough) tileEntity;
					if (!trough.stack.isEmpty() && FoodPreferenceManager.getInstance().REGISTRY_ITEM.get(entity.getClass()).canEat(this.capHungry, trough.stack)) {
						eatFoodBonus(trough.stack);
						trough.stack.shrink(1);
						
						// TODO check valid flag
						world.notifyBlockUpdate(pos, state, state, 3);
					}
				}
			}
			return false;
		}
		return !entity.getNavigator().noPath();
	}

	private void eatFoodBonus(ItemStack item) {
		if (item.isEmpty())
			return;

		double hunger = FoodPreferenceManager.getInstance().REGISTRY_ITEM.get(entity.getClass()).getHunger(item);
		capHungry.addHunger(hunger);

		if (this.entity.getGrowingAge() < 0) {
			NBTTagCompound tag = item.getTagCompound();
			if (tag == null || !tag.hasKey("isNatural") || !tag.getBoolean("isNatural")) {
				int duration = (int) (hunger
						/ entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_bmr).getAttributeValue());
				this.entity.addPotionEffect(new PotionEffect(ModPotions.potionGrowth, duration, 1));
			}
		}

		NBTTagCompound tag = item.getTagCompound();
		if (tag == null || !tag.hasKey("isNatural") || !tag.getBoolean("isNatural")) {
			this.capTaming.addTaming(0.0002
					/ entity.getAttributeMap().getAttributeInstance(ModAttributes.hunger_bmr).getAttributeValue()
					* hunger);
		}

	}
	
	@Override
	public void resetTask() {
		delayCounter = delay;
	}
	
}

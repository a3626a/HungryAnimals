package oortcloud.hungryanimals.entities.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungryanimals.blocks.BlockTrough;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceManager;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;

public class EntityAIMoveToTrough extends EntityAIBase {

	private EntityAnimal entity;
	private double speed;
	private World world;
	public BlockPos pos;
	private int delayCounter;
	private static int delay = 100;

	public EntityAIMoveToTrough(EntityAnimal entity, double speed) {
		this.delayCounter = entity.getRNG().nextInt(delay);
		
		this.entity = entity;
		this.world = this.entity.worldObj;
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
				if (entity.getCapability(ProviderTamableAnimal.CAP, null).getTaming() >= 1 && temp != null && temp instanceof TileEntityTrough) {
					TileEntityTrough trough = (TileEntityTrough) temp;
					return trough.stack != null && FoodPreferenceManager.getInstance().REGISTRY_ITEM.get(entity.getClass()).canEat(trough.stack);
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
					if (trough.stack != null && FoodPreferenceManager.getInstance().REGISTRY_ITEM.get(entity.getClass()).canEat(trough.stack)) {
						property.eatFoodBonus(trough.stack);
						trough.stack.stackSize--;
						if (trough.stack.stackSize == 0)
							trough.stack = null;
						
						// TODO check valid flag
						world.notifyBlockUpdate(pos, state, state, 3);
					}
				}
			}
			return false;
		}
		return !entity.getNavigator().noPath();
	}

	@Override
	public void resetTask() {
		delayCounter = delay;
	}
	
}

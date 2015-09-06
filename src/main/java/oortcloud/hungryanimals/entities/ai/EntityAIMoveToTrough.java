package oortcloud.hungryanimals.entities.ai;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.blocks.BlockTrough;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.core.network.PacketTileEntityClient;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;

public class EntityAIMoveToTrough extends EntityAIBase {

	private final int serchingX = 8;
	private final int serchingY = 2;
	private final int serchingZ = 8;
	private final int serchingIteration = 10;
	private final int serchingApprox = 3;

	private EntityAnimal entity;
	private ExtendedPropertiesHungryAnimal property;
	private double speed;
	private World world;
	public int x;
	public int y;
	public int z;

	public EntityAIMoveToTrough(EntityAnimal entity, ExtendedPropertiesHungryAnimal property, double speed) {
		this.entity = entity;
		this.property = property;
		this.world = this.entity.worldObj;
		this.speed = speed;
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {

		IBlockState state = world.getBlockState(new BlockPos(x, y, z));
		if (state.getBlock() == ModBlocks.trough) {
			TileEntity temp = ((BlockTrough) state.getBlock()).getTileEntity(world, new BlockPos(x, y, z));

			if (property.taming >= 1 && temp != null && temp instanceof TileEntityTrough) {
				TileEntityTrough tile;
				tile = (TileEntityTrough) temp;
				if (tile.stack != null && this.property.canEatFood(tile.stack)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public void startExecuting() {
		this.entity.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed);
	}

	public boolean continueExecuting() {

		if (Math.abs(entity.posX - this.x - 0.5) <= 1.5 && Math.abs(entity.posY - this.y - 0.5) <= 1 && Math.abs(entity.posZ - this.z - 0.5) <= 1.5) {
			IBlockState state = world.getBlockState(new BlockPos(x, y, z));
			if (state.getBlock() == ModBlocks.trough) {
				TileEntity temp = ((BlockTrough) state.getBlock()).getTileEntity(world, new BlockPos(x, y, z));

				if (temp != null && temp instanceof TileEntityTrough) {
					TileEntityTrough tile;
					tile = (TileEntityTrough) temp;
					if (tile.stack != null && this.property.canEatFood(tile.stack)) {
						property.eatFoodBonus(tile.stack);
						tile.stack.stackSize--;
						if (tile.stack.stackSize == 0)
							tile.stack = null;

						PacketTileEntityClient msg0 = new PacketTileEntityClient(0, this.world.provider.getDimensionId(), new BlockPos(this.x, this.y, this.z));
						msg0.setItemStack(tile.stack);
						HungryAnimals.simpleChannel.sendToAll(msg0);
					}
				}
			}
			return false;
		}
		if (entity.getNavigator().noPath()) {
			return false;
		} else {
			return true;
		}
	}

}

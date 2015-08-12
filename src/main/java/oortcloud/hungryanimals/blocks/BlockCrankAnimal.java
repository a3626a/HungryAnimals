package oortcloud.hungryanimals.blocks;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemLead;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.energy.EnergyNetwork;
import oortcloud.hungryanimals.tileentities.TileEntityCrankAnimal;

public class BlockCrankAnimal extends BlockEnergyTransporter {

	protected BlockCrankAnimal() {
		super(Material.wood);

		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.blockCrankAnimalName);
		ModBlocks.register(this);
	}

	/*
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityCrankAnimal crankAnimal = (TileEntityCrankAnimal) worldIn.getTileEntity(pos);
		if (crankAnimal != null) {
			BlockPos primaryPos = crankAnimal.getPrimaryPos();
			BlockPos offset = pos.add(primaryPos.multiply(-1));
			int index = offset.getX() * offset.getX() + offset.getZ() * offset.getZ();

			double x1 = pos.getX();
			double x2 = pos.getX() + 1;
			double z1 = pos.getZ();
			double z2 = pos.getZ() + 1;

			if (index == 2) {
				if (offset.getX() == 1) {
					x1 = pos.getX();
					x2 = pos.getX() + 0.5;
				} else {
					x1 = pos.getX() + 0.5;
					x2 = pos.getX() + 1;
				}
				if (offset.getZ() == 1) {
					z1 = pos.getZ();
					z2 = pos.getZ() + 0.5;
				} else {
					z1 = pos.getZ() + 0.5;
					z2 = pos.getZ() + 1;
				}
			}
			return new AxisAlignedBB(x1, (double) pos.getY(), z1, x2, (double) pos.getY() + 1, z2);
		}
		return super.getCollisionBoundingBox(worldIn, pos, state);
	}
*/
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntityCrankAnimal crankAnimal = (TileEntityCrankAnimal) worldIn.getTileEntity(pos);

		if (crankAnimal != null) {
			TileEntityCrankAnimal crankAnimalPrimary = (TileEntityCrankAnimal) worldIn.getTileEntity(crankAnimal.getPrimaryPos());
			if (crankAnimalPrimary != null) {
				crankAnimalPrimary.setLeashed(playerIn, worldIn);

				double d0 = 7.0D;
				List list = worldIn.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(pos.getX() - d0, pos.getY() - d0, pos.getZ() - d0, pos.getX() + d0,
						pos.getY() + d0, pos.getZ() + d0));
				Iterator iterator = list.iterator();

				while (iterator.hasNext()) {
					EntityLiving entityliving = (EntityLiving) iterator.next();

					if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == playerIn) {
						entityliving.clearLeashed(true, false);
					}
				}

				return true;
			}
		}
		return false;

	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityCrankAnimal crankAnimal = (TileEntityCrankAnimal) worldIn.getTileEntity(pos);

		if (crankAnimal != null) {
			worldIn.destroyBlock(crankAnimal.getPrimaryPos(), false);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);

		TileEntityCrankAnimal crankAnimal = (TileEntityCrankAnimal) worldIn.getTileEntity(pos);

		if (crankAnimal != null) {
			if (worldIn.getBlockState(crankAnimal.getPrimaryPos()).getBlock() != this) {
				worldIn.destroyBlock(pos, false);
			}
		}

	}

	@Override
	public TileEntity createNewTileEntity(World wolrd, int meta) {
		return new TileEntityCrankAnimal();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isNormalCube() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public void divideNetwork(World world, BlockPos pos) {
		Block block;
		block = world.getBlockState(pos.up()).getBlock();
		if (block instanceof BlockEnergyTransporter) {
			((BlockEnergyTransporter) block).setNetwork(world, pos.up(), new EnergyNetwork(0));
		}
		block = world.getBlockState(pos.down()).getBlock();
		if (block instanceof BlockEnergyTransporter) {
			((BlockEnergyTransporter) block).setNetwork(world, pos.down(), new EnergyNetwork(0));
		}
	}

	@Override
	public boolean isTowards(World world, BlockPos pos, EnumFacing side) {
		TileEntityCrankAnimal crankAnimal = (TileEntityCrankAnimal) world.getTileEntity(pos);
		if (crankAnimal.isPrimary()) {
			return side == EnumFacing.DOWN || side == EnumFacing.UP;
		} else {
			return false;
		}
	}

}

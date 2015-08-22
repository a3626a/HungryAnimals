package oortcloud.hungryanimals.blocks;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.energy.PowerNetwork;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.tileentities.TileEntityCrankAnimal;

public class BlockCrankAnimal extends BlockContainer {

	protected BlockCrankAnimal() {
		super(Material.wood);

		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.blockCrankAnimalName);
		ModBlocks.register(this);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}
	
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
			if (crankAnimal.isPrimary()) spawnAsEntity(worldIn, pos, new ItemStack(ModItems.crankAnimal));
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

}

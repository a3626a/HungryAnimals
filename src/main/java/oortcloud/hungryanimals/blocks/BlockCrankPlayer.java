package oortcloud.hungryanimals.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.energy.EnergyNetwork;
import oortcloud.hungryanimals.tileentities.TileEntityCrankPlayer;

public class BlockCrankPlayer extends BlockEnergyTransporter {

	public static final float exhaustion = 0.5F;

	protected BlockCrankPlayer() {
		super(Material.wood);

		this.setBlockBounds(0.375F, 0, 0.375F, 0.625F, 0.5625F, 0.625F);

		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.blockCrankPlayerName);
		this.setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModBlocks.register(this);
	}

	@Override
	public TileEntity createNewTileEntity(World wolrd, int meta) {
		return new TileEntityCrankPlayer();
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
		block = world.getBlockState(pos.down()).getBlock();
		if (block instanceof BlockEnergyTransporter) {
			((BlockEnergyTransporter) block).setNetwork(world, pos.down(), new EnergyNetwork(0));
		}
	}

	@Override
	public boolean isTowards(World world, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntityCrankPlayer te = (TileEntityCrankPlayer) worldIn.getTileEntity(pos);
		if (te.leftTick == 0) {
			playerIn.addExhaustion(exhaustion);
			te.leftTick = 40;
			return true;
		} else {
			return false;
		}
	}

}

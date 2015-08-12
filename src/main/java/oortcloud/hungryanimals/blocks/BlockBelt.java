package oortcloud.hungryanimals.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.energy.EnergyNetwork;
import oortcloud.hungryanimals.tileentities.TileEntityBelt;

public class BlockBelt extends BlockEnergyTransporter {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockBelt() {
		super(Material.wood);

		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH));
		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.blockBeltName);
		this.setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModBlocks.register(this);
	}
	
	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING });
	}

	@Override
	public TileEntity createNewTileEntity(World wolrd, int meta) {
		return new TileEntityBelt();
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
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.fromAngle(placer.rotationYaw));
	}
	
	@Override
	public void divideNetwork(World world, BlockPos pos) {
		Block block;
		block = world.getBlockState(pos.south()).getBlock();
		if (block instanceof BlockEnergyTransporter) {
			((BlockEnergyTransporter) block).setNetwork(world, pos.south(), new EnergyNetwork(0));
		}
		block = world.getBlockState(pos.north()).getBlock();
		if (block instanceof BlockEnergyTransporter) {
			((BlockEnergyTransporter) block).setNetwork(world, pos.north(), new EnergyNetwork(0));
		}
		block = world.getBlockState(pos.east()).getBlock();
		if (block instanceof BlockEnergyTransporter) {
			((BlockEnergyTransporter) block).setNetwork(world, pos.east(), new EnergyNetwork(0));
		}
		block = world.getBlockState(pos.west()).getBlock();
		if (block instanceof BlockEnergyTransporter) {
			((BlockEnergyTransporter) block).setNetwork(world, pos.west(), new EnergyNetwork(0));
		}
	}

	@Override
	public boolean isTowards(World world, BlockPos pos, EnumFacing side) {

		if (side == EnumFacing.DOWN || side == EnumFacing.UP) {
			return false;
		} else {
			EnumFacing meta = (EnumFacing) world.getBlockState(pos).getValue(FACING);
			
			return meta.getAxis() == side.getAxis();
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
	}
}

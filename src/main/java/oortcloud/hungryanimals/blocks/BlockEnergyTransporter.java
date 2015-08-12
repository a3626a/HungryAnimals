package oortcloud.hungryanimals.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import oortcloud.hungryanimals.energy.EnergyNetwork;
import oortcloud.hungryanimals.tileentities.TileEntityEnergyTransporter;

public class BlockEnergyTransporter extends BlockContainer {

	protected BlockEnergyTransporter(Material material) {
		super(material);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		mergeNetwork(worldIn, pos);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		divideNetwork(worldIn, pos);
	}

	public void mergeNetwork(World world, BlockPos pos) {
		setNetwork(world, pos, new EnergyNetwork(0));
	}

	public void divideNetwork(World world, BlockPos pos) {
	}

	public void setNetwork(World world, BlockPos pos, EnergyNetwork net) {

		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity != null && tileEntity instanceof TileEntityEnergyTransporter) {
			TileEntityEnergyTransporter te = (TileEntityEnergyTransporter) world.getTileEntity(pos);

			EnergyNetwork network = te.getNetwork();

			if (network != net) {
				double energy = network.getEnergy() / network.getCapacity() * EnergyNetwork.energyUnit;
				net.setEnergy(net.getEnergy() + energy);
				network.setEnergy(network.getEnergy() - energy);
				net.setCapacity(net.getCapacity() + te.getEnergyCapacity());
				network.setCapacity(network.getCapacity() - te.getEnergyCapacity());
				te.setNetwork(net);
				te.isInitialized = true;

				for (EnumFacing i : EnumFacing.VALUES) {
					if (this.isTowards(world, pos, i)) {
						Block block = world.getBlockState(pos.offset(i)).getBlock();
						if (block instanceof BlockEnergyTransporter && ((BlockEnergyTransporter) block).isTowards(world, pos.offset(i), i.getOpposite())) {
							((BlockEnergyTransporter) block).setNetwork(world, pos.offset(i), net);
						}
					}
				}
			} else {
				return;
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		TileEntityEnergyTransporter tileEntity = new TileEntityEnergyTransporter();
		tileEntity.isInitialized = true;
		return tileEntity;
	}

	public boolean isTowards(World world, BlockPos pos, EnumFacing side) {
		return false;
	}
}

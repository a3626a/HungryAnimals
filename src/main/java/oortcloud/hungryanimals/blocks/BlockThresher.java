package oortcloud.hungryanimals.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.energy.PowerNetwork;
import oortcloud.hungryanimals.tileentities.TileEntityThresher;
import oortcloud.hungryanimals.utils.InventoryUtil;

public class BlockThresher extends BlockContainer {

	public static final float exhaustion = 0.5F;

	protected BlockThresher() {
		super(Material.wood);
		this.setHarvestLevel("axe", 0);
		this.setHardness(2.0F);

		this.setBlockBounds((float) 0.375, 0, (float) 0.375, (float) 0.625, 1, (float) 0.625);
		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.blockThresherName);
		this.setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModBlocks.register(this);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityThresher();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isNormalCube() {
		return false;
	}

	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			TileEntityThresher tileEntity = (TileEntityThresher) worldIn.getTileEntity(pos);
			if (tileEntity.canTakeOut()) {
				return InventoryUtil.interactInventory(playerIn, tileEntity, 0);
			}
		}
		return false;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof IInventory) {
			InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
		}

		super.breakBlock(worldIn, pos, state);
	}
}

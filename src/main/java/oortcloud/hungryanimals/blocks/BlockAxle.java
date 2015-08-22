package oortcloud.hungryanimals.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.items.ItemBelt;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.tileentities.TileEntityAxle;

public class BlockAxle extends BlockContainer {

	public static final PropertyBool VARIANT = PropertyBool.create("hasWheel");

	public BlockAxle() {
		super(Material.wood);

		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, false));
		this.setBlockBounds((float) 0.375, 0, (float) 0.375, (float) 0.625, 1, (float) 0.625);
		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.blockAxleName);
		this.setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModBlocks.register(this);
	}
	
	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { VARIANT });
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityAxle();
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
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
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(ModBlocks.axle));
		if (state.getValue(VARIANT) == Boolean.TRUE) {
			ret.add(new ItemStack(ModItems.wheel));
		}
		
		return ret;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity != null && tileEntity instanceof TileEntityAxle) {
			TileEntityAxle axle = (TileEntityAxle)tileEntity;
			if (TileEntityAxle.isConnected(tileEntity.getWorld(),axle)) {
				int requiredBelt = axle.getBeltLength();
				spawnAsEntity(worldIn, pos, new ItemStack(ModItems.belt, 1, requiredBelt));
			}
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {

		ItemStack item = playerIn.inventory.getCurrentItem();
		IBlockState meta = worldIn.getBlockState(pos);
		if (item != null) {
			if ((Boolean)meta.getValue(VARIANT) == false && item.getItem() == ModItems.wheel) {
				worldIn.setBlockState(pos, meta.withProperty(VARIANT, true), 2);
				if (!playerIn.capabilities.isCreativeMode)
					if (--item.stackSize == 0) {
						playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
					}
				return true;
			}
		}
		return false;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((Boolean)state.getValue(VARIANT))?1:0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return meta==1?getDefaultState().withProperty(VARIANT, true):getDefaultState().withProperty(VARIANT, false);
	}

}
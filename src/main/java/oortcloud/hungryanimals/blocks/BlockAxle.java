package oortcloud.hungryanimals.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.energy.EnergyNetwork;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.tileentities.TileEntityAxle;

public class BlockAxle extends BlockEnergyTransporter {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockAxle.EnumType.class);

	public BlockAxle() {
		super(Material.wood);

		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockAxle.EnumType.AXLE).withProperty(FACING, EnumFacing.SOUTH));
		this.setBlockBounds((float) 0.375, 0, (float) 0.375, (float) 0.625, 1, (float) 0.625);
		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.blockAxleName);
		this.setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModBlocks.register(this);
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, VARIANT });
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
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityAxle();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {

		ItemStack item = playerIn.inventory.getCurrentItem();
		IBlockState meta = worldIn.getBlockState(pos);
		if (item != null) {
			if (meta.getValue(VARIANT) == EnumType.AXLE && item.getItem() == ModItems.wheel) {
				worldIn.setBlockState(pos, meta.withProperty(VARIANT, EnumType.WHEEL), 2);
				if (--item.stackSize == 0) {
					playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
				}
				return true;
			} else if (meta.getValue(VARIANT) == EnumType.WHEEL && item.getItem() == ItemBlock.getItemFromBlock(ModBlocks.belt)) {
				worldIn.setBlockState(pos, meta.withProperty(VARIANT, EnumType.BELT), 2);
				if (--item.stackSize == 0) {
					playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
				}
				if (!worldIn.isRemote)
					setNetwork(worldIn, pos, new EnergyNetwork(0));
				return true;
			}
		} else {
			if (meta.getValue(VARIANT) == EnumType.BELT) {
				worldIn.setBlockState(pos, meta.withProperty(FACING, ((EnumFacing) meta.getValue(FACING)).rotateY()), 2);
				if (!worldIn.isRemote) {
					Block block;
					block = worldIn.getBlockState(pos.south()).getBlock();
					if (block instanceof BlockEnergyTransporter) {
						((BlockEnergyTransporter) block).setNetwork(worldIn, pos.south(), new EnergyNetwork(0));
					}
					block = worldIn.getBlockState(pos.north()).getBlock();
					if (block instanceof BlockEnergyTransporter) {
						((BlockEnergyTransporter) block).setNetwork(worldIn, pos.north(), new EnergyNetwork(0));
					}
					block = worldIn.getBlockState(pos.east()).getBlock();
					if (block instanceof BlockEnergyTransporter) {
						((BlockEnergyTransporter) block).setNetwork(worldIn, pos.east(), new EnergyNetwork(0));
					}
					block = worldIn.getBlockState(pos.west()).getBlock();
					if (block instanceof BlockEnergyTransporter) {
						((BlockEnergyTransporter) block).setNetwork(worldIn, pos.west(), new EnergyNetwork(0));
					}
				}
			}
			return true;
		}
		return false;

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

		if (side == EnumFacing.UP || side == EnumFacing.DOWN) {
			return true;
		} else {
			if (world.getBlockState(pos).getValue(VARIANT) == EnumType.BELT) {
				return world.getBlockState(pos).getValue(FACING) == side;
			} else {
				return false;
			}
		}
	}

	public static enum EnumType implements IStringSerializable {
		AXLE(0, "axle"), WHEEL(1, "wheel"), BELT(2, "belt");
		private static final BlockAxle.EnumType[] META_LOOKUP = new BlockAxle.EnumType[values().length];
		private final int meta;
		private final String name;
		private final String unlocalizedName;

		private EnumType(int meta, String name) {
			this(meta, name, name);
		}

		private EnumType(int meta, String name, String unlocalizedName) {
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
		}

		public int getMetadata() {
			return this.meta;
		}

		public String toString() {
			return this.name;
		}

		public static BlockAxle.EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		public String getName() {
			return this.name;
		}

		public String getUnlocalizedName() {
			return this.unlocalizedName;
		}

		static {
			BlockAxle.EnumType[] var0 = values();
			int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2) {
				BlockAxle.EnumType var3 = var0[var2];
				META_LOOKUP[var3.getMetadata()] = var3;
			}
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex() + (((EnumType) state.getValue(VARIANT)).getMetadata() << 2);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(VARIANT, EnumType.byMetadata(meta >> 2));
	}
}
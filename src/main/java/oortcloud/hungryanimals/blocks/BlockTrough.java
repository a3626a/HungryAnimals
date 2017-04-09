package oortcloud.hungryanimals.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;

public class BlockTrough extends BlockContainer {

	public static final PropertyEnum<EnumPartType> PART = PropertyEnum.create("part", EnumPartType.class);
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final AxisAlignedBB BOUND_BOX = new AxisAlignedBB(0F, 0F, 0F, 1F, 0.5F, 1F);
	public static final AxisAlignedBB FLOOR = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
	public static final AxisAlignedBB EAST = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.125F, 0.5F, 1.0F);
	public static final AxisAlignedBB WEST = new AxisAlignedBB(0.875F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
	public static final AxisAlignedBB NORTH = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.125F);
	public static final AxisAlignedBB SOUTH = new AxisAlignedBB(0.0F, 0.0F, 0.875F, 1.0F, 0.5F, 1.0F);
	
	
	private final Random random = new Random();

	protected BlockTrough() {
		super(Material.WOOD);
		setHarvestLevel("axe", 0);
		setHardness(2.0F);
		
		setUnlocalizedName(References.MODID+"."+Strings.blockTroughName); 
		setRegistryName(Strings.blockTroughName);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, net.minecraft.util.math.BlockPos pos) {
		return BOUND_BOX;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, PART });
	}

	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z) {
		return ModItems.trough;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.IGNORE;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public void onNeighborChange(IBlockAccess worldIn, BlockPos pos, BlockPos neighborBlock) {
		IBlockState state = worldIn.getBlockState(pos);
		EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);

		if (state.getValue(PART) == BlockTrough.EnumPartType.HEAD) {
			if (worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() != this) {
				((World) worldIn).setBlockToAir(pos);
			}
		} else if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this) {
			((World) worldIn).setBlockToAir(pos);

			if (!((World) worldIn).isRemote) {
				this.dropBlockAsItem((World) worldIn, pos, state, 0);
				TileEntityTrough trough = (TileEntityTrough) worldIn.getTileEntity(pos);
				if (trough != null)
					dropStoredItems((World) worldIn, pos, trough);
			}
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(PART) == EnumPartType.HEAD ? null : ModItems.trough;
	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
		if (state.getValue(PART) == BlockTrough.EnumPartType.FOOT) {
			super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
		}
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		// to prevent item drop in creative mode
		if (player.capabilities.isCreativeMode && state.getValue(PART) == BlockTrough.EnumPartType.HEAD) {
			BlockPos blockpos1 = pos.offset(((EnumFacing) state.getValue(FACING)).getOpposite());

			if (worldIn.getBlockState(blockpos1).getBlock() == this) {
				worldIn.setBlockToAir(blockpos1);
			}
		}
		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, FLOOR);
		
		EnumFacing rot = ((EnumFacing) state.getValue(FACING));
		if (state.getValue(PART) == EnumPartType.HEAD)
			rot = rot.getOpposite();

		if (rot != EnumFacing.SOUTH) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH);
		}
		if (rot != EnumFacing.WEST) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST);
		}
		if (rot != EnumFacing.NORTH) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH);
		}
		if (rot != EnumFacing.EAST) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST);
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return (getStateFromMeta(meta).getValue(PART) == EnumPartType.FOOT) ? new TileEntityTrough() : null;
	}


	public TileEntity getTileEntity(World world, BlockPos pos) {
		IBlockState meta = world.getBlockState(pos);
		if (meta.getBlock() == this) {
			return (meta.getValue(PART) == EnumPartType.HEAD ? world.getTileEntity(pos.offset(((EnumFacing) meta.getValue(FACING)).getOpposite())) : world.getTileEntity(pos));
		} else {
			return null;
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = this.getTileEntity(worldIn, pos);

		if (!(te instanceof TileEntityTrough)) {
			return false;
		}

		TileEntityTrough foodbox = (TileEntityTrough) te;
		ItemStack stackinfoodbox = foodbox.stack;
		ItemStack stackinhand = playerIn.getHeldItemMainhand();

		if (stackinhand == null) {
			if (stackinfoodbox != null) {
				if (playerIn.inventory.addItemStackToInventory(stackinfoodbox)) {
					foodbox.stack = null;
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		if (stackinfoodbox == null) {
			if (stackinhand.stackSize > 16) {
				foodbox.stack = stackinhand.splitStack(16);
			} else {
				foodbox.stack = stackinhand;
				playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
			}
		} else if (stackinfoodbox.getItem() == stackinhand.getItem()) {
			if (stackinhand.stackSize + stackinfoodbox.stackSize > 16) {
				stackinfoodbox.stackSize = 16;
				stackinhand.stackSize += stackinfoodbox.stackSize - 16;
			} else {
				stackinfoodbox.stackSize += stackinhand.stackSize;
				playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
			}
		} else {
			return false;
		}

		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

		TileEntityTrough trough = (TileEntityTrough) worldIn.getTileEntity(pos);
		if (trough != null)
			dropStoredItems(worldIn, pos, trough);

		super.breakBlock(worldIn, pos, state);
	}

	private void dropStoredItems(World worldIn, BlockPos pos, TileEntityTrough trough) {
		if (trough != null) {
			ItemStack itemstack = trough.stack;
			if (itemstack != null) {
				float f = this.random.nextFloat() * 0.8F + 0.1F;
				float f1 = this.random.nextFloat() * 0.8F + 0.1F;
				float f2 = this.random.nextFloat() * 0.8F + 0.1F;

				while (itemstack.stackSize > 0) {
					int j1 = this.random.nextInt(3) + 3;

					if (j1 > itemstack.stackSize) {
						j1 = itemstack.stackSize;
					}

					itemstack.stackSize -= j1;
					EntityItem entityitem = new EntityItem(worldIn, (double) ((float) pos.getX() + f), (double) ((float) pos.getY() + f1), (double) ((float) pos.getZ() + f2),
							new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

					if (itemstack.hasTagCompound()) {
						entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
					}

					float f3 = 0.05F;
					entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
					entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
					entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
					worldIn.spawnEntityInWorld(entityitem);
				}
			}

		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getHorizontal(meta & 3);
		return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(PART, (meta >> 2 == 1) ? EnumPartType.HEAD : EnumPartType.FOOT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex() + ((state.getValue(PART) == EnumPartType.HEAD ? 1 : 0) << 2);
	}

	public static enum EnumPartType implements IStringSerializable {
		HEAD("head"), FOOT("foot");
		private final String name;

		private EnumPartType(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		public String getName() {
			return this.name;
		}
	}
}

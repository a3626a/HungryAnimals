package oortcloud.hungryanimals.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;

public class BlockTrough extends BlockContainer {

	public static final PropertyEnum PART = PropertyEnum.create("part", EnumPartType.class);
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	private final Random random = new Random();

	protected BlockTrough() {
		super(Material.wood);
		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.blockTroughName);
		this.setHarvestLevel("axe", 0);
		this.setHardness(2.0F);
		this.setBlockBounds(0F, 0F, 0F, 1F, 0.5F, 1F);
		GameRegistry.registerBlock(this, ModBlocks.getName(this.getUnlocalizedName()));
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, PART });
	}

	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z) {
		return ModItems.trough;
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	public int getMobilityFlag() {
		return 1;
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

	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);

		if (state.getValue(PART) == BlockTrough.EnumPartType.HEAD) {
			if (worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() != this) {
				worldIn.setBlockToAir(pos);
			}
		} else if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this) {
			worldIn.setBlockToAir(pos);

			if (!worldIn.isRemote) {
				this.dropBlockAsItem(worldIn, pos, state, 0);
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
		if (player.capabilities.isCreativeMode && state.getValue(PART) == BlockTrough.EnumPartType.HEAD) {
			BlockPos blockpos1 = pos.offset(((EnumFacing) state.getValue(FACING)).getOpposite());

			if (worldIn.getBlockState(blockpos1).getBlock() == this) {
				worldIn.setBlockToAir(blockpos1);
			}
		}
	}

	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		float f = 0.125F;

		EnumFacing rot = ((EnumFacing) state.getValue(FACING));
		if (state.getValue(PART) == EnumPartType.HEAD)
			rot = rot.getOpposite();

		if (rot != EnumFacing.SOUTH) {
			this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 0.5F, 1.0F);
			super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		}
		if (rot != EnumFacing.WEST) {
			this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		}
		if (rot != EnumFacing.NORTH) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, f);
			super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		}
		if (rot != EnumFacing.EAST) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 0.5F, 1.0F);
			super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		}

		this.setBlockBounds(0F, 0F, 0F, 1F, 0.5F, 1F);
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = this.getTileEntity(worldIn, pos);

		if (!(te instanceof TileEntityTrough)) {
			return false;
		}

		TileEntityTrough foodbox = (TileEntityTrough) te;
		ItemStack stackinfoodbox = foodbox.stack;
		ItemStack stackinhand = playerIn.getHeldItem();

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
		TileEntityTrough trough = (TileEntityTrough) getTileEntity(worldIn, pos);
		HungryAnimals.logger.info("breakBlock0");
		if (trough != null) {

			ItemStack itemstack = trough.stack;
			HungryAnimals.logger.info("breakBlock1");
			if (itemstack != null) {
				HungryAnimals.logger.info("breakBlock2");
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

		super.breakBlock(worldIn, pos, state);
	}

	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getHorizontal(meta & 3);
		return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(PART, (meta >> 2 == 1) ? EnumPartType.HEAD : EnumPartType.FOOT);
	}

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

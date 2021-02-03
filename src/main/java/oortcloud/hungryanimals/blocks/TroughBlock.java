package oortcloud.hungryanimals.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;

public class TroughBlock extends HorizontalBlock {
	public static final EnumProperty<EnumPartType> PART = EnumProperty.create("part", EnumPartType.class);

	private static final VoxelShape FLOOR = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
	private static final VoxelShape EAST = Block.makeCuboidShape(0.875D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
	private static final VoxelShape WEST = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 0.125D, 0.5D, 1.0D);
	private static final VoxelShape SOUTH = Block.makeCuboidShape(0.0D, 0.0D, 0.875D, 1.0D, 0.5D, 1.0D);
	private static final VoxelShape NORTH = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.125D);

	// East-side is opened
	private static final VoxelShape VOXEL_EAST = VoxelShapes.or(FLOOR, WEST, NORTH, SOUTH);
	private static final VoxelShape VOXEL_WEST = VoxelShapes.or(FLOOR, EAST, NORTH, SOUTH);
	private static final VoxelShape VOXEL_SOUTH = VoxelShapes.or(FLOOR, EAST, WEST, SOUTH);
	private static final VoxelShape VOXEL_NORTH = VoxelShapes.or(FLOOR, EAST, WEST, NORTH);

	private final Random random = new Random();

	protected TroughBlock() {
		super(Block.Properties
				.create(Material.WOOD)
				.harvestLevel(0)
				.harvestTool(ToolType.AXE)
				.hardnessAndResistance(2.0F));
		setRegistryName(Strings.blockTroughName);
		this.setDefaultState(this.stateContainer.getBaseState().with(PART, EnumPartType.FOOT));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		Direction direction = state.get(HORIZONTAL_FACING);
		Direction direction1 = state.get(PART) == EnumPartType.HEAD ? direction : direction.getOpposite();
		switch(direction1) {
			case NORTH:
				return VOXEL_SOUTH;
			case SOUTH:
				return VOXEL_NORTH;
			case WEST:
				return VOXEL_EAST;
			default:
				return VOXEL_WEST;
		}
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing == getDirectionToOther(stateIn.get(PART), stateIn.get(HORIZONTAL_FACING))) {
			return facingState.getBlock() == this && facingState.get(PART) != stateIn.get(PART) ? stateIn : Blocks.AIR.getDefaultState();
		} else {
			return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		}
	}

	/**
	 * Given a bed part and the direction it's facing, find the direction to move to get the other bed part
	 */
	private static Direction getDirectionToOther(EnumPartType part, Direction direction) {
		return part == EnumPartType.FOOT ? direction : direction.getOpposite();
	}

	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		super.harvestBlock(worldIn, player, pos, Blocks.AIR.getDefaultState(), te, stack);
	}

	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		EnumPartType bedpart = state.get(PART);
		BlockPos blockpos = pos.offset(getDirectionToOther(bedpart, state.get(HORIZONTAL_FACING)));
		BlockState blockstate = worldIn.getBlockState(blockpos);
		if (blockstate.getBlock() == this && blockstate.get(PART) != bedpart) {
			worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
			if (!worldIn.isRemote && !player.isCreative()) {
				ItemStack itemstack = player.getHeldItemMainhand();
				spawnDrops(state, worldIn, pos, (TileEntity)null, player, itemstack);
				spawnDrops(blockstate, worldIn, blockpos, (TileEntity)null, player, itemstack);
			}

			player.addStat(Stats.BLOCK_MINED.get(this));
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getPlacementHorizontalFacing();
		BlockPos blockpos = context.getPos();
		BlockPos blockpos1 = blockpos.offset(direction);
		return context.getWorld().getBlockState(blockpos1).isReplaceable(context) ? this.getDefaultState().with(HORIZONTAL_FACING, direction) : null;
	}

	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}

	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, PART);
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (!worldIn.isRemote) {
			BlockPos blockpos = pos.offset(state.get(HORIZONTAL_FACING));
			worldIn.setBlockState(blockpos, state.with(PART, EnumPartType.HEAD), 3);
			worldIn.notifyNeighbors(pos, Blocks.AIR);
			state.updateNeighbors(worldIn, pos, 3);
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return state.get(PART) == EnumPartType.FOOT;
	}

	@Override
	public TileEntity createTileEntity(World world, BlockState state) {
		return (state.get(PART) == EnumPartType.FOOT) ? new TileEntityTrough() : null;
	}

	public TileEntity getTileEntity(World world, BlockPos pos) {
		BlockState meta = world.getBlockState(pos);
		if (meta.getBlock() == this) {
			return (meta.get(PART) == EnumPartType.HEAD ? world.getTileEntity(pos.offset(((Direction) meta.get(FACING)).getOpposite()))
					: world.getTileEntity(pos));
		} else {
			return null;
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, EntityPlayer playerIn, EnumHand hand, Direction side, float hitX,
			float hitY, float hitZ) {
		TileEntity te = this.getTileEntity(worldIn, pos);

		if (!(te instanceof TileEntityTrough)) {
			return false;
		}

		TileEntityTrough foodbox = (TileEntityTrough) te;
		ItemStack stackinfoodbox = foodbox.stack;
		ItemStack stackinhand = playerIn.getHeldItem(hand);

		if (stackinhand.isEmpty()) {
			if (!stackinfoodbox.isEmpty()) {
				if (playerIn.inventory.addItemStackToInventory(stackinfoodbox)) {
					foodbox.stack = ItemStack.EMPTY;
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		if (stackinfoodbox.isEmpty()) {
			if (stackinhand.getCount() > 16) {
				foodbox.stack = stackinhand.splitStack(16);
			} else {
				foodbox.stack = stackinhand;
				playerIn.setHeldItem(hand, ItemStack.EMPTY);
			}
		} else if (stackinfoodbox.getItem() == stackinhand.getItem()) {
			if (stackinhand.getCount() + stackinfoodbox.getCount() > 16) {
				stackinfoodbox.setCount(16);
				stackinhand.grow(stackinfoodbox.getCount() - 16);
			} else {
				stackinfoodbox.grow(stackinhand.getCount());
				playerIn.setHeldItem(hand, ItemStack.EMPTY);
			}
		} else {
			return false;
		}

		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, BlockState state) {

		TileEntityTrough trough = (TileEntityTrough) worldIn.getTileEntity(pos);
		if (trough != null)
			dropStoredItems(worldIn, pos, trough);

		super.breakBlock(worldIn, pos, state);
	}

	private void dropStoredItems(World worldIn, BlockPos pos, TileEntityTrough trough) {
		if (trough != null) {
			ItemStack itemstack = trough.stack;
			if (!itemstack.isEmpty()) {
				float f = this.random.nextFloat() * 0.8F + 0.1F;
				float f1 = this.random.nextFloat() * 0.8F + 0.1F;
				float f2 = this.random.nextFloat() * 0.8F + 0.1F;

				while (itemstack.getCount() > 0) {
					int j1 = this.random.nextInt(3) + 3;

					if (j1 > itemstack.getCount()) {
						j1 = itemstack.getCount();
					}

					itemstack.shrink(j1);
					EntityItem entityitem = new EntityItem(worldIn, (double) ((float) pos.getX() + f), (double) ((float) pos.getY() + f1),
							(double) ((float) pos.getZ() + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

					CompoundNBT tag = itemstack.getTagCompound();
					if (tag != null) {
						entityitem.getItem().setTagCompound(tag.copy());
					}

					float f3 = 0.05F;
					entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
					entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
					entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
					worldIn.spawnEntity(entityitem);
				}
			}

		}
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

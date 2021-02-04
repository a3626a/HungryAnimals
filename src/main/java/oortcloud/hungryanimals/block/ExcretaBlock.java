package oortcloud.hungryanimals.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.potion.EffectInstance;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.materials.ModMaterials;
import oortcloud.hungryanimals.potion.ModPotions;

public class ExcretaBlock extends FallingBlock {

	public static final EnumProperty<EnumType> CONTENT = EnumProperty.create("content", ExcretaBlock.EnumType.class);

	private static final VoxelShape HEIGHT1 = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);
	private static final VoxelShape HEIGHT2 = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	private static final VoxelShape HEIGHT3 = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	private static final VoxelShape HEIGHT4 = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

	public static final float hardnessConstant = 0.5F;
	public static double fermetationProbability;
	public static double erosionProbability;
	public static double fertilizationProbability;
	public static double erosionProbabilityOnHay;
	public static double diseaseProbability;
	public static final double diseaseRadius = 3;

	public ExcretaBlock() {
		super(
				Block.Properties
						.create(ModMaterials.excreta)
						.harvestLevel(0)
						.harvestTool(ToolType.SHOVEL)
						.tickRandomly()
						.doesNotBlockMovement()
		);
		setDefaultState(this.stateContainer.getBaseState().with(CONTENT, EnumType.getValue(1, 0)));
		setRegistryName(Strings.blockExcretaName);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(CONTENT);
	}

	@Override
	@MethodsReturnNonnullByDefault
	@OnlyIn(Dist.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch(state.get(CONTENT).exc + state.get(CONTENT).man) {
			case 1 :
				return HEIGHT1;
			case 2 :
				return HEIGHT2;
			case 3 :
				return HEIGHT3;
			case 4 :
				return HEIGHT4;
		}
		return HEIGHT4;
	}

	@Override
	public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.get(CONTENT).exc + state.get(CONTENT).man >= 3;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(CONTENT, EnumType.getValue(1, 0));
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		worldIn.addBlockEvent(pos, this, 0, 1);
	}

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		worldIn.addBlockEvent(pos, this, 0, 1);
	}

	@Override
	public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.tick(state, worldIn, pos, null);

		if (!worldIn.isRemote) {
			if (worldIn.getBlockState(pos).getBlock() == this && worldIn.getBlockState(pos.down()).getBlock() == this) {
				BlockState metaTop = worldIn.getBlockState(pos);
				BlockState metaBot = worldIn.getBlockState(pos.down());
				this.stackBlock(worldIn, pos.down(), metaTop, metaBot, true);
			}
			if (worldIn.getBlockState(pos).getBlock() == this && worldIn.getBlockState(pos.up()).getBlock() == this) {
				BlockState metaTop = worldIn.getBlockState(pos.up());
				BlockState metaBot = worldIn.getBlockState(pos);
				this.stackBlock(worldIn, pos, metaTop, metaBot, true);
			}
		}

		return true;
	}

	@Override
	public float getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos) {
		if (blockState.getBlock() == this) {
			return ((blockState.get(CONTENT)).exc + (blockState.get(CONTENT)).man) * hardnessConstant;
		}
		return 0;
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random rand) {
		if (!worldIn.isRemote) {
			if (worldIn.getBlockState(pos).getBlock() == this)
				ferment(worldIn, pos, rand);

			if (worldIn.getBlockState(pos).getBlock() == this)
				disease(worldIn, pos, rand);

			if (worldIn.getBlockState(pos).getBlock() == this)
				dissolve(worldIn, pos, rand);
		}
	}

	private void dissolve(World world, BlockPos pos, Random random) {
		BlockState meta = world.getBlockState(pos);
		int man = meta.get(CONTENT).man;
		int exc = meta.get(CONTENT).exc;
		Block bottom = world.getBlockState(pos.down()).getBlock();
		if (bottom == ModBlocks.DIRT.get() || bottom == ModBlocks.GRASS_BLOCK.get() || bottom == ModBlocks.SAND.get()) {
			if (random.nextDouble() < ExcretaBlock.fertilizationProbability) {
				if (man > 0) {
					man--;
				} else if (exc > 0) {
					exc--;
				}

				if (man == 0 && exc == 0) {
					world.removeBlock(pos, false);
				} else {
					world.setBlockState(pos, meta.with(CONTENT, EnumType.getValue(exc, man)), 3);
				}

				if (bottom == ModBlocks.SAND.get()) {
					world.setBlockState(pos.down(), ModBlocks.DIRT.get().getDefaultState(), 3);
				} else if (bottom == ModBlocks.DIRT.get()) {
					world.setBlockState(pos.down(), ModBlocks.GRASS_BLOCK.get().getDefaultState(), 3);
				}
			}
		} else if (bottom == ModBlocks.FLOOR_COVER_HAY.get()) {
			if (random.nextDouble() < ExcretaBlock.erosionProbabilityOnHay) {
				if (man > 0) {
					man--;
				} else if (exc > 0) {
					exc--;
				}

				if (man == 0 && exc == 0) {
					world.removeBlock(pos, false);
				} else {
					world.setBlockState(pos, meta.with(CONTENT, EnumType.getValue(exc, man)), 3);
				}
			}
		} else if (world.isAirBlock(pos.up())) {
			if (random.nextDouble() < ExcretaBlock.erosionProbability) {
				if (exc > 0) {
					exc--;
				} else if (man > 0) {
					man--;
				}

				if (man == 0 && exc == 0) {
					world.removeBlock(pos, false);
				} else {
					world.setBlockState(pos, meta.with(CONTENT, EnumType.getValue(exc, man)), 3);
				}
			}
		}
	}

	private void disease(World world, BlockPos pos, Random random) {
		int exc = (world.getBlockState(pos).get(CONTENT)).exc;

		if (random.nextDouble() < ExcretaBlock.diseaseProbability / 3.0 * Math.max(0, exc - 1)) {
			Predicate<Entity> hungryAnimalSelector = entityIn -> {
				if (entityIn == null) {
					return false;
				}
				return entityIn.getCapability(ProviderHungryAnimal.CAP).isPresent();
			};

			for (Object i : world.getEntitiesWithinAABB(MobEntity.class, new AxisAlignedBB(pos.add(-diseaseRadius, -diseaseRadius, -diseaseRadius),
					pos.add(diseaseRadius + 1, diseaseRadius + 1, diseaseRadius + 1)), hungryAnimalSelector)) {
				((MobEntity) i).addPotionEffect(new EffectInstance(ModPotions.potionDisease, 24000, 0));
			}
		}

	}

	private void ferment(World world, BlockPos pos, Random random) {
		BlockState meta = world.getBlockState(pos);
		int exc = meta.get(CONTENT).exc;
		int man = meta.get(CONTENT).man;
		int firstExc = exc;

		boolean flag = false;
		for (int i = 0; i < firstExc; i++) {
			if (random.nextDouble() < ExcretaBlock.fermetationProbability) {
				world.setBlockState(pos, meta.with(CONTENT, EnumType.getValue(--exc, ++man)), 3);
				flag = true;
			}
		}
		if (flag) {
			for (Direction i : Direction.values()) {
				if (world.getBlockState(pos.offset(i)).getBlock() == ModBlocks.EXCRETA.get())
					ferment(world, pos.offset(i), random);
			}
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if (!worldIn.isRemote) {
			if (entityIn instanceof FallingBlockEntity) {
				FallingBlockEntity entityFall = (FallingBlockEntity) entityIn;
				BlockState stateFall = entityFall.getBlockState();

				if (stateFall.getBlock() == this && entityFall.fallTime > 1 && entityFall.isAlive()) {
					entityFall.remove();

					BlockState metaBot = worldIn.getBlockState(pos);

					this.stackBlock(worldIn, pos, stateFall, metaBot, false);
				}
			}
		}
		// TODO configurable
		int volume = state.get(CONTENT).exc+state.get(CONTENT).man;
		entityIn.setMotion(entityIn.getMotion().mul((1-0.2*volume), (1-0.2*volume), (1-0.2*volume)));
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
		int exc = state.get(CONTENT).exc;
		int man = state.get(CONTENT).man;
		if (exc + man >= 4)
			return false;

		if (!playerIn.getHeldItem(hand).isEmpty() && playerIn.getHeldItem(hand).getItem() == this.asItem()) {
			if (!playerIn.isCreative()) {
				playerIn.getHeldItemMainhand().shrink(1);
			}
			worldIn.setBlockState(pos, state.with(CONTENT, EnumType.getValue(exc + 1, man)), 3);

			return true;
		}

		return false;
	}

	private void stackBlock(World world, BlockPos pos, BlockState metaTop, BlockState metaBot, boolean deleteTopBlock) {
		int botExc = metaBot.get(CONTENT).exc;
		int botMan = metaBot.get(CONTENT).man;
		int topExc = metaTop.get(CONTENT).exc;
		int topMan = metaTop.get(CONTENT).man;

		if (botExc + botMan + topExc + topMan <= 4) {
			if (deleteTopBlock) {
				world.removeBlock(pos.up(), false);
			}
			world.setBlockState(pos, metaBot.with(CONTENT, EnumType.getValue(botExc + topExc, botMan + topMan)), 3);

		} else {
			if (botMan + topMan <= 4) {
				world.setBlockState(pos.up(), metaTop.with(CONTENT, EnumType.getValue(botExc + botMan + topExc + topMan - 4, 0)), 3);
				world.setBlockState(pos, metaBot.with(CONTENT, EnumType.getValue(4 - botMan - topMan, botMan + topMan)), 3);

			} else {
				if (botMan != 4) {
					world.setBlockState(pos.up(), metaBot.with(CONTENT, EnumType.getValue(botExc + topExc, botMan + topMan - 4)), 3);
					world.setBlockState(pos, metaBot.with(CONTENT, EnumType.getValue(0, 4)), 3);
				}
			}
		}
	}

	public static enum EnumType implements IStringSerializable {
		E1M0(0, 1, 0),
		E2M0(1, 2, 0),
		E3M0(2, 3, 0),
		E4M0(3, 4, 0),
		E0M1(4, 0, 1),
		E1M1(5, 1, 1),
		E2M1(6, 2, 1),
		E3M1(7, 3, 1),
		E0M2(8, 0, 2),
		E1M2(9, 1, 2),
		E2M2(10, 2, 2),
		E0M3(11, 0, 3),
		E1M3(12, 1, 3),
		E0M4(13, 0, 4);

		private static final ExcretaBlock.EnumType[] META_LOOKUP = new ExcretaBlock.EnumType[values().length];
		private final int meta;
		private final int exc;
		private final int man;

		private EnumType(int meta, int exc, int man) {
			this.meta = meta;
			this.exc = exc;
			this.man = man;
		}

		public int getMetadata() {
			return this.meta;
		}

		public int getExcreta() {
			return this.exc;
		}

		public int getManure() {
			return this.man;
		}

		public String toString() {
			return "(excreta = " + this.exc + ", manure = " + this.man + ")";
		}

		public static ExcretaBlock.EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		public String getName() {
			return this.exc + "_" + this.man;
		}

		public String getUnlocalizedName() {
			return this.exc + "_ " + this.man;
		}

		static {
			for (int i = 0; i < values().length; ++i) {
				ExcretaBlock.EnumType var = values()[i];
				META_LOOKUP[var.getMetadata()] = values()[i];
			}
		}

		public static EnumType getValue(int exc, int man) {
			if (exc + man > 4 || exc + man <= 0) {
				return values()[0];
			}
			int ind = 0;
			switch (man) {
			case 0:
				ind = exc - 1;
				break;
			case 1:
				ind = exc + 4;
				break;
			case 2:
				ind = exc + 8;
				break;
			case 3:
				ind = exc + 11;
				break;
			case 4:
				ind = exc + 13;
			}
			return values()[ind];
		}
	}

}
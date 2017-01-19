package oortcloud.hungryanimals.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.materials.ModMaterials;
import oortcloud.hungryanimals.potion.ModPotions;

public class BlockExcreta extends BlockFalling {

	public static final PropertyEnum CONTENT = PropertyEnum.create("content", BlockExcreta.EnumType.class);

	private static int[] metaToManure = { 1, 2, 3, 4, 0, 1, 2, 3, 0, 1, 2, 0, 1, 0, 0, 0 };
	private static int[] metaToExcreta = { 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 4, 0, 0 };

	public static final float hardnessConstant = 0.5F;
	public static final double defualt_fermetationProbability = 0.05;
	public static double fermetationProbability;
	public static final double defualt_erosionProbability = 0.025;
	public static double erosionProbability;
	public static final double defualt_fertilizationProbability = 0.2;
	public static double fertilizationProbability;
	public static final double defualt_erosionOnHayProbability = 0.5;
	public static double erosionOnHayProbability;
	public static final double defualt_diseaseProbability = 0.05;
	public static double diseaseProbability;
	public static final double diseaseRadius = 3;

	public BlockExcreta() {
		super(ModMaterials.excreta);
		setHarvestLevel("shovel", 0);
		setDefaultState(this.blockState.getBaseState().withProperty(CONTENT, EnumType.getValue(1, 0)));
		setUnlocalizedName(Strings.blockExcretaName);
		setCreativeTab(HungryAnimals.tabHungryAnimals);
		setTickRandomly(true);
		ModBlocks.register(this);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { CONTENT });
	}


	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	
	@Override
	public boolean isFullCube(IBlockState state) {
		// TODO isFullcube based on BlockStates
		//return super.isFullCube(state);
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		// TODO isFullcube based on BlockStates
		//return super.isOpaqueCube(state);
		return false;
	}


	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(CONTENT, EnumType.getValue(1, 0));
	}

	@Override
	public void onNeighborChange(IBlockAccess worldIn, BlockPos pos, BlockPos neighborBlock) {
		((World) worldIn).addBlockEvent(pos, this, 0, 1);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.addBlockEvent(pos, this, 0, 1);
	}

	
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.updateTick(worldIn, pos, state, null);

		if (worldIn.getBlockState(pos).getBlock() != this)
			return false;

		if (!worldIn.isRemote) {
			if (worldIn.getBlockState(pos.down()).getBlock() == this) {
				IBlockState metaTop = worldIn.getBlockState(pos);
				IBlockState metaBot = worldIn.getBlockState(pos.down());
				int manureBot = ((EnumType) metaBot.getValue(CONTENT)).man;
				int excretaBot = ((EnumType) metaBot.getValue(CONTENT)).exc;
				if (excretaBot + manureBot < 4) {
					this.stackBlock(worldIn, pos.down(), metaTop, metaBot, true);
				}
			} else if (worldIn.getBlockState(pos.up()).getBlock() == this) {
				IBlockState metaTop = worldIn.getBlockState(pos.up());
				IBlockState metaBot = worldIn.getBlockState(pos);
				int manureTop = ((EnumType) metaTop.getValue(CONTENT)).man;
				int manureBot = ((EnumType) metaBot.getValue(CONTENT)).man;
				if (manureTop > 0 && manureBot < 4) {
					this.stackBlock(worldIn, pos, metaTop, metaBot, true);
				}
			}
		}

		return true;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		int exc = ((EnumType) state.getValue(CONTENT)).exc;
		int man = ((EnumType) state.getValue(CONTENT)).man;
		ret.add(new ItemStack(ItemBlock.getItemFromBlock(this), exc));
		ret.add(new ItemStack(ModItems.manure, man));
		return ret;
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
		if (blockState.getBlock() == this) {
			return (((EnumType) blockState.getValue(CONTENT)).exc + ((EnumType) blockState.getValue(CONTENT)).man) * hardnessConstant;
		}
		return 0;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0, 0, 0, 1, 0.25F * (((EnumType) state.getValue(CONTENT)).exc + ((EnumType) state.getValue(CONTENT)).man), 1);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return null;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			if (worldIn.getBlockState(pos).getBlock() == this)
				fermentate(worldIn, pos, rand);

			if (worldIn.getBlockState(pos).getBlock() == this)
				disease(worldIn, pos, rand);

			if (worldIn.getBlockState(pos).getBlock() == this)
				dissolve(worldIn, pos, rand);
		}
	}

	private void dissolve(World world, BlockPos pos, Random random) {
		IBlockState meta = world.getBlockState(pos);
		int man = ((EnumType) meta.getValue(CONTENT)).man;
		int exc = ((EnumType) meta.getValue(CONTENT)).exc;
		Block bottom = world.getBlockState(pos.down()).getBlock();
		if (bottom == Blocks.DIRT || bottom == Blocks.GRASS || bottom == Blocks.SAND) {
			if (random.nextDouble() < this.fertilizationProbability) {
				if (man > 0) {
					man--;
				} else if (exc > 0) {
					exc--;
				}

				if (man == 0 && exc == 0) {
					world.setBlockToAir(pos);
				} else {
					world.setBlockState(pos, meta.withProperty(CONTENT, EnumType.getValue(exc, man)), 3);
				}

				if (bottom == Blocks.SAND) {
					world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState(), 3);
				} else if (bottom == Blocks.DIRT) {
					world.setBlockState(pos.down(), Blocks.GRASS.getDefaultState(), 3);
				}
			}
		} else if (bottom == ModBlocks.floorcover_hay) {
			if (random.nextDouble() < this.erosionOnHayProbability) {
				if (man > 0) {
					man--;
				} else if (exc > 0) {
					exc--;
				}

				if (man == 0 && exc == 0) {
					world.setBlockToAir(pos);
				} else {
					world.setBlockState(pos, meta.withProperty(CONTENT, EnumType.getValue(exc, man)), 3);
				}
			}
		} else if (world.isAirBlock(pos.up())) {
			if (random.nextDouble() < this.erosionProbability) {
				if (exc > 0) {
					exc--;
				} else if (man > 0) {
					man--;
				}

				if (man == 0 && exc == 0) {
					world.setBlockToAir(pos);
				} else {
					world.setBlockState(pos, meta.withProperty(CONTENT, EnumType.getValue(exc, man)), 3);
				}
			}
		}
	}

	private void disease(World world, BlockPos pos, Random random) {

		int exc = ((EnumType) world.getBlockState(pos).getValue(CONTENT)).exc;

		if (random.nextDouble() < this.diseaseProbability / 3.0 * Math.max(0, exc - 1)) {
			Predicate hungryAnimalSelector = new Predicate() {
				public boolean apply(Entity entityIn) {
					return (entityIn.getCapability(ProviderHungryAnimal.CAP, null) != null);
				}

				public boolean apply(Object p_apply_1_) {
					return this.apply((Entity) p_apply_1_);
				}
			};

			for (Object i : world.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(pos.add(-diseaseRadius, -diseaseRadius, -diseaseRadius), pos.add(diseaseRadius + 1, diseaseRadius + 1, diseaseRadius + 1)), hungryAnimalSelector)) {
				((EntityLiving) i).addPotionEffect(new PotionEffect(ModPotions.potionDisease, 24000, 1));
			}
		}

	}

	private void fermentate(World world, BlockPos pos, Random random) {
		IBlockState meta = world.getBlockState(pos);
		int exc = ((EnumType) meta.getValue(CONTENT)).exc;
		int man = ((EnumType) meta.getValue(CONTENT)).man;
		int firstExc = exc;

		boolean flag = false;
		for (int i = 0; i < firstExc; i++) {
			if (random.nextDouble() < this.fermetationProbability) {
				world.setBlockState(pos, meta.withProperty(CONTENT, EnumType.getValue(--exc, ++man)), 3);
				flag = true;
			}
		}
		if (flag) {
			for (EnumFacing i : EnumFacing.VALUES) {
				if (world.getBlockState(pos.offset(i)).getBlock() == ModBlocks.excreta)
					fermentate(world, pos.offset(i), random);
			}
		}

	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!worldIn.isRemote && entityIn instanceof EntityFallingBlock && ((EntityFallingBlock) entityIn).getBlock().getBlock() == this && ((EntityFallingBlock) entityIn).fallTime > 1 && !entityIn.isDead) {

			entityIn.setDead();

			IBlockState metaBot = worldIn.getBlockState(pos);
			IBlockState metaTop = ((EntityFallingBlock) entityIn).getBlock();

			this.stackBlock(worldIn, pos, metaTop, metaBot, false);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		int exc = ((EnumType) state.getValue(CONTENT)).exc;
		int man = ((EnumType) state.getValue(CONTENT)).man;
		if (exc + man >= 4)
			return false;

		if (playerIn.getHeldItemMainhand() != null && playerIn.getHeldItemMainhand().getItem().equals(ItemBlock.getItemFromBlock(this))) {
			if (!playerIn.capabilities.isCreativeMode) {
				playerIn.getHeldItemMainhand().stackSize--;
				if (playerIn.getHeldItemMainhand().stackSize == 0) {
					playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
				}
			}
			worldIn.setBlockState(pos, state.withProperty(CONTENT, EnumType.getValue(exc + 1, man)), 3);

			return true;
		}

		return false;
	}

	private void stackBlock(World world, BlockPos pos, IBlockState metaTop, IBlockState metaBot, boolean deleteTopBlock) {
		int botExc = ((EnumType) metaBot.getValue(CONTENT)).exc;
		int botMan = ((EnumType) metaBot.getValue(CONTENT)).man;
		int topExc = ((EnumType) metaTop.getValue(CONTENT)).exc;
		int topMan = ((EnumType) metaTop.getValue(CONTENT)).man;

		if (botExc + botMan + topExc + topMan <= 4) {
			if (deleteTopBlock) {
				world.setBlockToAir(pos.up());
			}
			world.setBlockState(pos, metaBot.withProperty(CONTENT, EnumType.getValue(botExc + topExc, botMan + topMan)), 3);

		} else {
			if (botMan + topMan <= 4) {
				world.setBlockState(pos.up(), metaTop.withProperty(CONTENT, EnumType.getValue(botExc + botMan + topExc + topMan - 4, 0)), 3);

				world.setBlockState(pos, metaBot.withProperty(CONTENT, EnumType.getValue(4 - botMan - topMan, botMan + topMan)), 3);

			} else {
				world.setBlockState(pos.up(), metaBot.withProperty(CONTENT, EnumType.getValue(botExc + topExc, botMan + topMan - 4)), 3);

				world.setBlockState(pos, metaBot.withProperty(CONTENT, EnumType.getValue(0, 4)), 3);
			}
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(CONTENT, EnumType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumType) state.getValue(CONTENT)).meta;
	}

	public static enum EnumType implements IStringSerializable {
		E1M0(0, 1, 0), E2M0(1, 2, 0), E3M0(2, 3, 0), E4M0(3, 4, 0), E0M1(4, 0, 1), E1M1(5, 1, 1), E2M1(6, 2, 1), E3M1(7, 3, 1), E0M2(8, 0, 2), E1M2(9, 1, 2), E2M2(10, 2, 2), E0M3(11, 0, 3), E1M3(12, 1, 3), E0M4(13, 0, 4);

		private static final BlockExcreta.EnumType[] META_LOOKUP = new BlockExcreta.EnumType[values().length];
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

		public static BlockExcreta.EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		public String getName() {
			return "(" + this.exc + "," + this.man + ")";
		}

		public String getUnlocalizedName() {
			return "(" + this.exc + ", " + this.man + ")";
		}

		static {
			for (int i = 0; i < values().length; ++i) {
				BlockExcreta.EnumType var = values()[i];
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
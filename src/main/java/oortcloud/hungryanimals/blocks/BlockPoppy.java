package oortcloud.hungryanimals.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.items.ModItems;

public class BlockPoppy extends BlockBush implements IGrowable {

	protected static int maxGrowthStage = 7;
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, maxGrowthStage);

	public BlockPoppy() {
		this.setCreativeTab(null);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.blockPoppyName);
		this.setTickRandomly(true);
		this.setHardness(0.0F);
		this.setStepSound(soundTypeGrass);
		this.disableStats();
		ModBlocks.register(this);
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { AGE });
	}

	@SideOnly(Side.CLIENT)
	public Item getItem(World worldIn, BlockPos pos) {
		return ModItems.poppyseed;
	}

	@Override
	public List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
		int age = ((Integer) state.getValue(AGE)).intValue();
		Random rand = world instanceof World ? ((World) world).rand : new Random();

		if (age == 7) {
			ret.add(new ItemStack(ModItems.poppycrop, 1, 0));
		} else if (6 >= age && age >= 4) {
			ret.add(new ItemStack(ItemBlock.getItemFromBlock(Blocks.red_flower), 1, 0));
		} else {
			ret.add(new ItemStack(ModItems.poppyseed, 1, 0));
		}

		return ret;
	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		return (worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos)) && worldIn.getBlockState(pos.down()).getBlock().canSustainPlant(worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
	}

	@Override
	protected boolean canPlaceBlockOn(Block parBlock) {
		return parBlock == Blocks.farmland;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);

		if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
			int i = ((Integer) state.getValue(AGE)).intValue();

			if (i < 7) {
				float f = getGrowthChance(this, worldIn, pos);

				if (rand.nextInt((int) (25.0F / f) + 1) == 0) {
					worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(i + 1)), 2);
				}
			}
		}
	}

	protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos) {
		float f = 1.0F;
		BlockPos blockpos1 = pos.down();

		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				float f1 = 0.0F;
				IBlockState iblockstate = worldIn.getBlockState(blockpos1.add(i, 0, j));

				if (iblockstate.getBlock().canSustainPlant(worldIn, blockpos1.add(i, 0, j), net.minecraft.util.EnumFacing.UP, (net.minecraftforge.common.IPlantable) blockIn)) {
					f1 = 1.0F;

					if (iblockstate.getBlock().isFertile(worldIn, blockpos1.add(i, 0, j))) {
						f1 = 3.0F;
					}
				}

				if (i != 0 || j != 0) {
					f1 /= 4.0F;
				}

				f += f1;
			}
		}

		BlockPos blockpos2 = pos.north();
		BlockPos blockpos3 = pos.south();
		BlockPos blockpos4 = pos.west();
		BlockPos blockpos5 = pos.east();
		boolean flag = blockIn == worldIn.getBlockState(blockpos4).getBlock() || blockIn == worldIn.getBlockState(blockpos5).getBlock();
		boolean flag1 = blockIn == worldIn.getBlockState(blockpos2).getBlock() || blockIn == worldIn.getBlockState(blockpos3).getBlock();

		if (flag && flag1) {
			f /= 2.0F;
		} else {
			boolean flag2 = blockIn == worldIn.getBlockState(blockpos4.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos5.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos5.south()).getBlock()
					|| blockIn == worldIn.getBlockState(blockpos4.south()).getBlock();

			if (flag2) {
				f /= 2.0F;
			}
		}

		return f;
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return ((Integer) state.getValue(AGE)).intValue() < 7;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		this.grow(worldIn, pos, state);
	}

	public void grow(World worldIn, BlockPos pos, IBlockState state) {
		int i = ((Integer) state.getValue(AGE)).intValue() + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);

		if (i > maxGrowthStage) {
			i = maxGrowthStage;
		}

		worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(i)), 2);
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
	}

	public int getMetaFromState(IBlockState state) {
		return ((Integer) state.getValue(AGE)).intValue();
	}

}

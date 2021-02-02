package oortcloud.hungryanimals.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.items.ModItems;

public class BlockNiterBed extends Block {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);

	public static double ripeningProbability;

	public BlockNiterBed() {
		super(Material.GROUND);
		setHarvestLevel("shovel", 0);
		setHardness(0.5F);
		
		setDefaultState(this.blockState.getBaseState().with(AGE, 0));
		setUnlocalizedName(References.MODID+"."+Strings.blockNiterBedName); 
		setRegistryName(Strings.blockNiterBedName);
		setCreativeTab(HungryAnimals.tabHungryAnimals);
		setTickRandomly(true);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { AGE });
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
		int age = (Integer) state.getValue(AGE);
		if (age < 7 && rand.nextDouble() < BlockNiterBed.ripeningProbability) {
			worldIn.setBlockState(pos, state.with(AGE, age + 1), 2);
		}
	}

	@Override
	public int quantityDropped(BlockState state, int fortune, Random random) {
		return (Integer) state.getValue(AGE) == 7 ? (3 + random.nextInt(3)) : 1;
	}

	@Override
	public Item getItemDropped(BlockState state, Random rand, int fortune) {
		return (Integer) state.getValue(AGE) == 7 ? ModItems.saltpeter : super.getItemDropped(state, rand, fortune);
	}

	@Override
	public BlockState getStateFromMeta(int meta) {
		return this.getDefaultState().with(AGE, Integer.valueOf(meta));
	}

	@Override
	public int getMetaFromState(BlockState state) {
		return ((Integer) state.getValue(AGE)).intValue();
	}
}

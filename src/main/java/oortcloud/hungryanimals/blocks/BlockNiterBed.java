package oortcloud.hungryanimals.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
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
	public static final double default_ripeningProbability = 0.1;

	public BlockNiterBed() {
		super(Material.GROUND);
		setHarvestLevel("shovel", 0);
		setHardness(0.5F);
		
		setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
		setUnlocalizedName(References.MODID+"."+Strings.blockNiterBedName); 
		setRegistryName(Strings.blockNiterBedName);
		setCreativeTab(HungryAnimals.tabHungryAnimals);
		setTickRandomly(true);
		//GameRegistry.register(this);
		//GameRegistry.register(new ItemBlock(this), getRegistryName());
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { AGE });
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		int age = (Integer) state.getValue(AGE);
		if (age < 7 && rand.nextDouble() < BlockNiterBed.default_ripeningProbability) {
			worldIn.setBlockState(pos, state.withProperty(AGE, age + 1), 2);
		}
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return (Integer) state.getValue(AGE) == 7 ? (3 + random.nextInt(3)) : 1;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return (Integer) state.getValue(AGE) == 7 ? ModItems.saltpeter : super.getItemDropped(state, rand, fortune);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((Integer) state.getValue(AGE)).intValue();
	}
}

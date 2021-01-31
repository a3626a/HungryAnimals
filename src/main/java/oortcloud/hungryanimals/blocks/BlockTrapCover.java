package oortcloud.hungryanimals.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;

public class BlockTrapCover extends Block {

	public BlockTrapCover() {
		super(Material.WOOD);
		setHarvestLevel("axe", 0);
		setHardness(2.0F);
		
		setUnlocalizedName(References.MODID+"."+Strings.blockTrapCoverName); 
		setRegistryName(Strings.blockTrapCoverName);
		setCreativeTab(HungryAnimals.tabHungryAnimals);
	}

	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(BlockState state) {
		return false;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
		if (!worldIn.isRemote && state.getBlock() == this) {
			worldIn.setBlockToAir(pos);
			spawnAsEntity(worldIn, pos, new ItemStack(Items.STICK, 2 + worldIn.rand.nextInt(4)));
			for (Direction i : Direction.HORIZONTALS) {
				worldIn.scheduleUpdate(pos.offset(i), this, 3);
			}
		}
	}

	@Override
	public void onLanded(World worldIn, Entity entityIn) {
		worldIn.scheduleUpdate(new BlockPos(entityIn).down(), this, 3);
	}

}

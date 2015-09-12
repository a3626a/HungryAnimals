package oortcloud.hungryanimals.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;

public class BlockTrapCover extends Block {

	public BlockTrapCover() {
		super(Material.wood);
		this.setHarvestLevel("axe", 0);
		this.setHardness(2.0F);

		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.blockTrapCoverName);
		this.setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModBlocks.register(this);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote && state.getBlock() == this) {
			worldIn.setBlockToAir(pos);
			spawnAsEntity(worldIn, pos, new ItemStack(Items.stick, 2 + worldIn.rand.nextInt(4)));
			for (EnumFacing i : EnumFacing.HORIZONTALS) {
				worldIn.scheduleUpdate(pos.offset(i), this, 3);
			}
		}
	}

	@Override
	public void onLanded(World worldIn, Entity entityIn) {
		worldIn.scheduleUpdate(new BlockPos(entityIn).down(), this, 3);
	}

}

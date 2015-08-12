package oortcloud.hungryanimals.items;

import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.tileentities.TileEntityCrankAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemCrankAnimal extends Item {

	private static final int[][] structure = new int[][] {{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,-1},{-1,1}};
	
	public ItemCrankAnimal() {
		this.setUnlocalizedName(References.RESOURCESPREFIX
				+ Strings.itemCrankAnimalName);
		this.setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(this);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		
		BlockPos setpos = pos.offset(side);
		
		boolean canPlace = true;
		if (!worldIn.isAirBlock(setpos)) {
			canPlace = false;
		}
		for(int i = 0 ; i < structure.length; i++) {
			if (!worldIn.isAirBlock(setpos.add(structure[i][0], 0, structure[i][1]))) {
				canPlace = false;
			}
		}
		
		if (canPlace) {
			worldIn.setBlockState(setpos, ModBlocks.crankAnimal.getDefaultState());
			((TileEntityCrankAnimal)worldIn.getTileEntity(setpos)).setPrimaryPos(setpos);
			for(int i = 0 ; i < structure.length; i++) {
				worldIn.setBlockState(setpos.add(structure[i][0], 0, structure[i][1]), ModBlocks.crankAnimal.getDefaultState());
				((TileEntityCrankAnimal)worldIn.getTileEntity(setpos.add(structure[i][0], 0, structure[i][1]))).setPrimaryPos(setpos);
			}
		}
		
		return canPlace;
	}
	
}

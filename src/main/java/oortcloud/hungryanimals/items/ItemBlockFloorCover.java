package oortcloud.hungryanimals.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockFloorCover extends ItemBlock {

	public ItemBlockFloorCover(Block block) {
		super(block);
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		return block.getBlockColor();
	}
	
}

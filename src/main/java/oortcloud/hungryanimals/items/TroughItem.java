package oortcloud.hungryanimals.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;

public class TroughItem extends BlockItem {
	public TroughItem(Block blockIn, Item.Properties properties) {
		super(blockIn, properties);
	}

	protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
		return context.getWorld().setBlockState(context.getPos(), state, 26);
	}
}

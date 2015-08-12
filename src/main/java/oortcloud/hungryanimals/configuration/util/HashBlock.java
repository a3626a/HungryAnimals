package oortcloud.hungryanimals.configuration.util;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

public class HashBlock {
	private IBlockState block;
	private boolean ignoreProperty;

	public HashBlock(Block block) {
		this(block.getDefaultState(), true);
	}

	public HashBlock(IBlockState block) {
		this(block, false);
	}

	public HashBlock(IBlockState block, boolean ignoreProperty) {
		this.block = block;
		this.ignoreProperty = ignoreProperty;
	}

	@Override
	public boolean equals(Object obj) {
		if (this.ignoreProperty && ((HashBlock) obj).ignoreProperty) {
			return block.getBlock() == ((HashBlock) obj).block.getBlock();
		} else if (!this.ignoreProperty && !((HashBlock) obj).ignoreProperty) {

			if (block.getBlock() != ((HashBlock) obj).block.getBlock())
				return false;

			for (Object i : block.getProperties().keySet()) {
				IProperty property = (IProperty) i;
				if (!block.getValue(property).equals(((HashBlock) obj).block.getValue(property)))
					return false;
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		if (ignoreProperty) {
			return block.getBlock().hashCode();
		} else {
			return block.hashCode();
		}
	}
}

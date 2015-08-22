package oortcloud.hungryanimals.recipes;

import java.util.ArrayList;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import oortcloud.hungryanimals.items.ModItems;

import com.google.common.collect.Lists;

public class RecipeConnectBelt implements IRecipe {

	public boolean matches(InventoryCrafting p_77569_1_, World worldIn) {
		ArrayList arraylist = Lists.newArrayList();

		for (int i = 0; i < p_77569_1_.getSizeInventory(); ++i) {
			ItemStack itemstack = p_77569_1_.getStackInSlot(i);

			if (itemstack != null && itemstack.getItem() == ModItems.belt) {
				arraylist.add(itemstack);
			}
		}

		return arraylist.size() > 1;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		ArrayList arraylist = Lists.newArrayList();
		ItemStack itemstack;

		int length = 0;
		for (int i = 0; i < p_77572_1_.getSizeInventory(); ++i) {
			itemstack = p_77572_1_.getStackInSlot(i);
			if (itemstack != null && itemstack.getItem() == ModItems.belt) {
				length += itemstack.getItemDamage();
				arraylist.add(itemstack);
			}
		}

		if (arraylist.size() > 1) {
			return new ItemStack(ModItems.belt, 1, length);
		}

		return null;
	}

	/**
	 * Returns the size of the recipe area
	 */
	public int getRecipeSize() {
		return 4;
	}

	public ItemStack getRecipeOutput() {
		return null;
	}

	public ItemStack[] getRemainingItems(InventoryCrafting p_179532_1_) {
		ItemStack[] aitemstack = new ItemStack[p_179532_1_.getSizeInventory()];

		for (int i = 0; i < aitemstack.length; ++i) {
			ItemStack itemstack = p_179532_1_.getStackInSlot(i);
			aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
		}

		return aitemstack;
	}
}

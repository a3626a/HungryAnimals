package oortcloud.hungryanimals.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryUtil {
	
	public static boolean interactInventory(EntityPlayer playerIn, IInventory tileEntity, int index) {
		ItemStack itemStackInventory=tileEntity.getStackInSlot(index);
		ItemStack itemStackPlayer=playerIn.getCurrentEquippedItem();
		if (itemStackInventory != null && itemStackPlayer == null) {
			playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, itemStackInventory);
			tileEntity.setInventorySlotContents(index, null);
			return true;
		}
		if (itemStackInventory == null && itemStackPlayer != null && tileEntity.isItemValidForSlot(index, itemStackPlayer)) {
			playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
			tileEntity.setInventorySlotContents(index, itemStackPlayer);
			return true;
		}
		if (itemStackInventory != null && itemStackPlayer != null && tileEntity.isItemValidForSlot(index, itemStackPlayer)) {
			if (itemStackInventory.isItemEqual(itemStackPlayer)) {
				int space = itemStackInventory.getMaxStackSize()-itemStackInventory.stackSize;
				if (space >= itemStackPlayer.stackSize) {
					itemStackInventory.stackSize+=itemStackPlayer.stackSize;
					playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
				} else {
					itemStackInventory.stackSize=itemStackInventory.getMaxStackSize();
					itemStackPlayer.stackSize-=space;
				}
				return true;
			}
			return false;
		}
		return false;
	}
	
}

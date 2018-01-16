package oortcloud.hungryanimals.entities.production;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;

public interface IProductionInteraction extends IProduction {
	public EnumActionResult interact(EntityInteract event, EnumHand hand, ItemStack itemstack);
}

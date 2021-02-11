package oortcloud.hungryanimals.entities.production;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;

public interface IProductionInteraction extends IProduction {
	public ActionResultType interact(EntityInteract event, Hand hand, @Nonnull ItemStack itemstack);
}

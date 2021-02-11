package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import oortcloud.hungryanimals.entities.production.IProduction;

public interface ICapabilityProducingAnimal {

	public void update();
	public ActionResultType interact(EntityInteract event, Hand hand, @Nonnull ItemStack itemstack);
	public Iterable<IProduction> getProductions();
	
}

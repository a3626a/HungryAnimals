package oortcloud.hungryanimals.entities.capability;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import oortcloud.hungryanimals.entities.production.IProduction;

public interface ICapabilityProducingAnimal {

	public void update();
	public EnumActionResult interact(EntityInteract event, EnumHand hand, @Nonnull ItemStack itemstack);
	public Iterable<IProduction> getProductions();
	
}

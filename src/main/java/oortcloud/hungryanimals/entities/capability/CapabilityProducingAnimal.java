package oortcloud.hungryanimals.entities.capability;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import oortcloud.hungryanimals.core.network.PacketEntityClient;
import oortcloud.hungryanimals.entities.production.IProduction;
import oortcloud.hungryanimals.entities.production.IProductionInteraction;
import oortcloud.hungryanimals.entities.production.IProductionTickable;
import oortcloud.hungryanimals.entities.production.ISyncable;

public class CapabilityProducingAnimal implements ICapabilityProducingAnimal {

	protected EntityAnimal entity;
	private List<IProductionInteraction> interactions;
	private List<IProductionTickable> tickables;
	private List<IProduction> productions;
	
	public CapabilityProducingAnimal() {}
	
	public CapabilityProducingAnimal(EntityAnimal entity, List<IProduction> productions) {
		this.entity = entity;
		this.interactions = new ArrayList<>();
		this.tickables = new ArrayList<>();
		this.productions = productions;
		
		for (IProduction i : productions) {
			if (i instanceof IProductionInteraction) {
				interactions.add((IProductionInteraction)i);
			}
			if (i instanceof IProductionTickable) {
				tickables.add((IProductionTickable)i);
			}
		}
	}
	
	@Override
	public void update() {
		for (IProductionTickable i : tickables) {
			i.update();
		}
	}

	@Override
	public EnumActionResult interact(EntityInteract event, EnumHand hand, @Nonnull ItemStack itemstack) {
		for (IProductionInteraction i : interactions) {
			EnumActionResult result = i.interact(event, hand, itemstack);
			if (result != EnumActionResult.PASS) {
				return result;
			}
		}
		return EnumActionResult.PASS;
	}

	@Override
	public Iterable<IProduction> getProductions() {
		return productions;
	}

	public void syncTo(EntityPlayerMP target) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			for (IProduction i : productions) {
				if (i instanceof ISyncable) {
					((ISyncable) i).syncTo(target);
				}
			}
		}
	}
	
	public void readFrom(PacketEntityClient message) {
		String name = message.getString();
		for (IProduction i : productions) {
			if (i instanceof ISyncable) {
				if (name.equals(i.getName())) {
					((ISyncable) i).readFrom(message);
					break;
				}
			}
		}
	}
	
}

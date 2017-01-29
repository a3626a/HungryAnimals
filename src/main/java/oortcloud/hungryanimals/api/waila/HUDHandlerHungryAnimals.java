package oortcloud.hungryanimals.api.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.entities.properties.handler.HungryAnimalManager;

@Optional.Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = "Waila")
public class HUDHandlerHungryAnimals implements IWailaEntityProvider {

	@Optional.Method(modid = "Waila")
    public static void callbackRegister(IWailaRegistrar register) {

		HUDHandlerHungryAnimals instance = new HUDHandlerHungryAnimals();

		register.registerBodyProvider(instance, EntityAnimal.class);
		register.registerNBTProvider(instance, EntityAnimal.class);
		
		/*
        register.addConfig(References.MODNAME, "option."+References.MODID+".showHunger");
        register.addConfig(References.MODNAME, "option."+References.MODID+".showTaming");
        register.addConfig(References.MODNAME, "option."+References.MODID+".showExcretion");
        */
    }

	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaBody(Entity entity, List<String> tips, IWailaEntityAccessor accessor, IWailaConfigHandler arg3) {
		
		EntityAnimal animal = (EntityAnimal)accessor.getEntity();
		
		if (!HungryAnimalManager.getInstance().isRegistered(animal.getClass()))			
			return tips;
		
		//Check this entity is supported by Hungry Animals
		//TODO add checking for Taming
		if (animal.getCapability(ProviderHungryAnimal.CAP, null) == null)
			return tips;
		
		NBTTagCompound tag = accessor.getNBTData();
		tag = tag.getCompoundTag(ExtendedPropertiesHungryAnimal.key);
		
		tips.add("Hunger: " + tag.getDouble("hunger")  + " / " + animal.getAttributeMap().getAttributeInstance(ModAttributes.hunger_max).getAttributeValue());
		tips.add("Excertion: " + (tag.getDouble("excretion")*100) + "%");
		tips.add("Taming: " + (tag.getDouble("excretion")>=1.0?"Friendly":tag.getDouble("excretion")>-0.1?"Neutral":"Wild"));
		
		return tips;
	}

	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaHead(Entity arg0, List<String> arg1, IWailaEntityAccessor arg2, IWailaConfigHandler arg3) {
		return arg1;
	}

	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaTail(Entity arg0, List<String> arg1, IWailaEntityAccessor arg2, IWailaConfigHandler arg3) {
		return arg1;
	}
	
	@Override
	@Optional.Method(modid = "Waila")
	public Entity getWailaOverride(IWailaEntityAccessor arg0, IWailaConfigHandler arg1) {
		return arg0.getEntity();
	}
	
	@Override
	@Optional.Method(modid = "Waila")
	public NBTTagCompound getNBTData(EntityPlayerMP arg0, Entity entity, NBTTagCompound tag, World arg3) {
		
		HungryAnimals.logger.info("Store Data");
		
		if (entity != null)
			entity.writeToNBT(tag);

        return tag;
	}
	
}

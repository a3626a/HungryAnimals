package oortcloud.hungryanimals.api.theoneprobe;

import javax.annotation.Nullable;

import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.properties.handler.HungryAnimalManager;

public class TOPCompatibility {

	private static boolean registered;

    public static void register() {
        if (registered)
            return;
        registered = true;
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "oortcloud.hungryanimals.api.theoneprobe.TOPCompatibility$GetTheOneProbe");
    }
	
    public static class GetTheOneProbe implements com.google.common.base.Function<ITheOneProbe, Void> {

        public static ITheOneProbe probe;

        @Nullable
        @Override
        public Void apply(ITheOneProbe theOneProbe) {
            probe = theOneProbe;
            HungryAnimals.logger.info("Enabled support for The One Probe");
            probe.registerEntityProvider(new IProbeInfoEntityProvider() {
				
				@Override
				public String getID() {
					return References.MODID+":hungryanimalsprovider";
				}
				
				@Override
				public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
					if (!(entity instanceof EntityAnimal))
						return;
						
					EntityAnimal animal = (EntityAnimal)entity;
					if (!HungryAnimalManager.getInstance().isRegistered(animal.getClass()))
						return;
					
					ICapabilityHungryAnimal capHungry = animal.getCapability(ProviderHungryAnimal.CAP, null);
					ICapabilityTamableAnimal capTaming = animal.getCapability(ProviderTamableAnimal.CAP, null);
					probeInfo.horizontal().text("HUNGER").progress((int)capHungry.getHunger(), (int)capHungry.getMaxHunger(), probeInfo.defaultProgressStyle().filledColor(0xFF0000FF).alternateFilledColor(0xFF0000FF).borderColor(0));
					if (capTaming.getTaming() >= 0) {
						probeInfo.horizontal().text("TAMING").progress((int)(Math.min(capTaming.getTaming(), 2.0)*100), 200, probeInfo.defaultProgressStyle().filledColor(0xFF00FF00).alternateFilledColor(0xFF00FF00).borderColor(0));
					} else {
						probeInfo.horizontal().text("TAMING").progress((int)(-Math.max(capTaming.getTaming(), -2.0)*100), 200, probeInfo.defaultProgressStyle().filledColor(0xFFFF0000).alternateFilledColor(0xFFFF0000).borderColor(0).showText(false));
					}
				}
			});
            return null;
        }
    }
}

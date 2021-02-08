package oortcloud.hungryanimals.configuration;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import oortcloud.hungryanimals.core.lib.References;

@Mod.EventBusSubscriber
public class ConfigurationEventHandler {
	@SubscribeEvent
	public static void onConfigChanged(OnConfigChangedEvent event) throws IllegalArgumentException, SecurityException {
		if (event.getModID().equals(References.MODID)) {
			ConfigurationHandler.syncPre();
			ConfigurationHandler.sync();
			ConfigurationHandler.syncPost();
		}
	}
}

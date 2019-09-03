package oortcloud.hungryanimals.configuration;

import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungryanimals.core.lib.References;

public class ConfigurationEventHandler {
	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (event.getModID().equals(References.MODID)) {
			ConfigurationHandler.syncPre();
			ConfigurationHandler.sync();
			ConfigurationHandler.syncPost();
		}
	}
}

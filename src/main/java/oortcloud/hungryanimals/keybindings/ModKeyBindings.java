package oortcloud.hungryanimals.keybindings;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModKeyBindings {
	
	public static KeyBinding keyRight = new KeyBinding("increase value", 205, "Hungry Animals Debug");
	public static KeyBinding keyLeft = new KeyBinding("decrease value", 203, "Hungry Animals Debug");
	public static KeyBinding keyUp = new KeyBinding("change selection", 200, "Hungry Animals Debug");
	public static KeyBinding keyDown = new KeyBinding("change selection", 208, "Hungry Animals Debug");
	
	public static void init() {
		ClientRegistry.registerKeyBinding(keyRight);
		ClientRegistry.registerKeyBinding(keyLeft);
		ClientRegistry.registerKeyBinding(keyUp);
		ClientRegistry.registerKeyBinding(keyDown);
	}
}

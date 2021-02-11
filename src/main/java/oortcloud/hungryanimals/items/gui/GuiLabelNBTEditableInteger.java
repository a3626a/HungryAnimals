package oortcloud.hungryanimals.items.gui;

import net.minecraft.client.gui.FontRenderer;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.ModPacketHandler;
import oortcloud.hungryanimals.core.network.PacketServerDGEditInt;

public class GuiLabelNBTEditableInteger extends GuiLabelNBTInteger implements IEditable {

	private int unit;
	private GuiLabelNBTInteger target;
	
	protected GuiLabelNBTEditableInteger(FontRenderer fontRenderer, String key, int unit, GuiLabelNBTInteger target) {
		super(fontRenderer, key);
		this.unit = unit;
		this.target = target;
	}

	public void increase() {
		PacketServerDGEditInt msg1 = new PacketServerDGEditInt(target.data, key, data + unit);
		ModPacketHandler.INSTANCE.sendToServer(msg1);
	}

	public void decrease() {
		PacketServerDGEditInt msg1 = new PacketServerDGEditInt(target.data, key, data - unit);
		ModPacketHandler.INSTANCE.sendToServer(msg1);
	}

}

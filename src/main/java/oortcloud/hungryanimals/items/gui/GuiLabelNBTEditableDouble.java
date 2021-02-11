package oortcloud.hungryanimals.items.gui;

import net.minecraft.client.gui.FontRenderer;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.ModPacketHandler;
import oortcloud.hungryanimals.core.network.PacketServerDGEditDouble;

public class GuiLabelNBTEditableDouble extends GuiLabelNBTDouble implements IEditable {

	private double unit;
	private GuiLabelNBTInteger target;
	
	protected GuiLabelNBTEditableDouble(FontRenderer fontRenderer, String key, double unit, GuiLabelNBTInteger target) {
		super(fontRenderer, key);
		this.unit = unit;
		this.target = target;
	}

	public void increase() {
		PacketServerDGEditDouble msg = new PacketServerDGEditDouble(target.data, key, data + unit);
		ModPacketHandler.INSTANCE.sendToServer(msg);
	}

	public void decrease() {
		PacketServerDGEditDouble msg = new PacketServerDGEditDouble(target.data, key, data - unit);
		ModPacketHandler.INSTANCE.sendToServer(msg);
	}

}

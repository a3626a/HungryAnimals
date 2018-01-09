package oortcloud.hungryanimals.items.gui;

import net.minecraft.client.gui.FontRenderer;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.PacketGeneralServer;
import oortcloud.hungryanimals.core.network.SyncIndex;

public class GuiLabelNBTEditableDouble extends GuiLabelNBTDouble implements IEditable {

	private double unit;
	private GuiLabelNBTInteger target;
	
	protected GuiLabelNBTEditableDouble(FontRenderer fontRenderer, String key, double unit, GuiLabelNBTInteger target) {
		super(fontRenderer, key);
		this.unit = unit;
		this.target = target;
	}

	public void increase() {
		PacketGeneralServer msg2 = new PacketGeneralServer(SyncIndex.ENTITYOVERLAY_EDIT_DOUBLE);
		msg2.setInt(target.data);
		msg2.setString(key);
		msg2.setDouble(data + unit);
		HungryAnimals.simpleChannel.sendToServer(msg2);
	}

	public void decrease() {
		PacketGeneralServer msg2 = new PacketGeneralServer(SyncIndex.ENTITYOVERLAY_EDIT_DOUBLE);
		msg2.setInt(target.data);
		msg2.setString(key);
		msg2.setDouble(data - unit);
		HungryAnimals.simpleChannel.sendToServer(msg2);
	}

}

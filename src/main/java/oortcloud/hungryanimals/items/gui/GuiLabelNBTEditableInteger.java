package oortcloud.hungryanimals.items.gui;

import net.minecraft.client.gui.FontRenderer;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.PacketGeneralServer;
import oortcloud.hungryanimals.core.network.SyncIndex;

public class GuiLabelNBTEditableInteger extends GuiLabelNBTInteger implements IEditable {

	private int unit;
	private GuiLabelNBTInteger target;
	
	protected GuiLabelNBTEditableInteger(FontRenderer fontRenderer, String key, int unit, GuiLabelNBTInteger target) {
		super(fontRenderer, key);
		this.unit = unit;
		this.target = target;
	}

	public void increase() {
		PacketGeneralServer msg1 = new PacketGeneralServer(SyncIndex.ENTITYOVERLAY_EDIT_INT);
		msg1.setInt(target.data);
		msg1.setString(key);
		msg1.setInt(data + unit);
		HungryAnimals.simpleChannel.sendToServer(msg1);
	}

	public void decrease() {
		PacketGeneralServer msg1 = new PacketGeneralServer(SyncIndex.ENTITYOVERLAY_EDIT_INT);
		msg1.setInt(target.data);
		msg1.setString(key);
		msg1.setInt(data - unit);
		HungryAnimals.simpleChannel.sendToServer(msg1);
	}

}

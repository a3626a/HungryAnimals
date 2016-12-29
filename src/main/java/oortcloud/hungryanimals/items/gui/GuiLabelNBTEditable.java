package oortcloud.hungryanimals.items.gui;

import net.minecraft.client.gui.FontRenderer;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.PacketGeneralServer;
import oortcloud.hungryanimals.core.network.SyncIndex;

public class GuiLabelNBTEditable extends GuiLabelNBT {

	private int intUnit;
	private double doubleUnit;
	private GuiLabelNBT id;
	
	protected GuiLabelNBTEditable(FontRenderer fontRenderer, String key, EnumFormat format, int intUnit, GuiLabelNBT id) {
		super(fontRenderer, key, format);
		this.intUnit = intUnit;
		this.id=id;
	}

	protected GuiLabelNBTEditable(FontRenderer fontRenderer, String key, EnumFormat format, double doubleUnit, GuiLabelNBT id) {
		super(fontRenderer, key, format);
		this.doubleUnit = doubleUnit;
		this.id=id;
	}

	public static GuiLabelNBTEditable createIntegerNBT(FontRenderer fontRenderer, String key, int intUnit, GuiLabelNBT id) {
		return new GuiLabelNBTEditable(fontRenderer, key, EnumFormat.INT, intUnit, id);
	}

	public static GuiLabelNBTEditable createDoubleNBT(FontRenderer fontRenderer, String key, double doubleUnit, GuiLabelNBT id) {
		return new GuiLabelNBTEditable(fontRenderer, key, EnumFormat.DOUBLE, doubleUnit, id);
	}

	public void increase() {
		switch (format) {
		case INT :
			PacketGeneralServer msg1 = new PacketGeneralServer(SyncIndex.ENTITYOVERLAY_EDIT_INT);
			msg1.setInt(id.intData);
			msg1.setString(key);
			msg1.setInt(intData+intUnit);
			HungryAnimals.simpleChannel.sendToServer(msg1);
			break;
		case DOUBLE :
			PacketGeneralServer msg2 = new PacketGeneralServer(SyncIndex.ENTITYOVERLAY_EDIT_DOUBLE);
			msg2.setInt(id.intData);
			msg2.setString(key);
			msg2.setDouble(doubleData+doubleUnit);
			HungryAnimals.simpleChannel.sendToServer(msg2);
			break;
		}
	}

	public void decrease() {
		switch (format) {
		case INT :
			PacketGeneralServer msg1 = new PacketGeneralServer(SyncIndex.ENTITYOVERLAY_EDIT_INT);
			msg1.setInt(id.intData);
			msg1.setString(key);
			msg1.setInt(intData-intUnit);
			HungryAnimals.simpleChannel.sendToServer(msg1);
			break;
		case DOUBLE :
			PacketGeneralServer msg2 = new PacketGeneralServer(SyncIndex.ENTITYOVERLAY_EDIT_DOUBLE);
			msg2.setInt(id.intData);
			msg2.setString(key);
			msg2.setDouble(doubleData-doubleUnit);
			HungryAnimals.simpleChannel.sendToServer(msg2);
			break;
		}
	}

}

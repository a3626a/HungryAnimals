package oortcloud.hungryanimals.items.gui;

import net.minecraft.client.gui.Gui;

public abstract class GuiPlacable extends Gui {

	protected int x;
	protected int y;

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	abstract public void update();
	abstract public void draw();
}

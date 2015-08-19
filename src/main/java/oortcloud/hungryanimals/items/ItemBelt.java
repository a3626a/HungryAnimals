package oortcloud.hungryanimals.items;

import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import net.minecraft.item.Item;

public class ItemBelt extends Item {

	public ItemBelt() {
		super();
		this.setCreativeTab(HungryAnimals.tabHungryAnimals);
		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemBeltName);
		this.setMaxStackSize(1);
		ModItems.register(this);
	}
	
}

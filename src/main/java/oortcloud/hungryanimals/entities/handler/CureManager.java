package oortcloud.hungryanimals.entities.handler;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.utils.HashItemType;

public class CureManager {
	private static CureManager INSTANCE;

	private Set<HashItemType> REGISTRY;
	
	private CureManager() {
		REGISTRY = new HashSet<HashItemType>();
	}
	
	public static CureManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CureManager();
		}
		return INSTANCE;
	}
	
	public boolean add(HashItemType cure) {
		return REGISTRY.add(cure);
	}
	
	public boolean isCure(ItemStack food) {
		if (REGISTRY.contains(new HashItemType(food.getItem()))) {
			return true;
		} else if (REGISTRY.contains(new HashItemType(food.getItem(), food.getItemDamage()))) {
			return true;
		} else {
			return false;
		}
	}
}

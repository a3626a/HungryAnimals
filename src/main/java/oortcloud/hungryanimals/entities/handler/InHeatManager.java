package oortcloud.hungryanimals.entities.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.utils.HashItemType;

public class InHeatManager {
	
	private static InHeatManager INSTANCE;

	private Map<HashItemType, Integer> REGISTRY;
	
	private InHeatManager() {
		REGISTRY = new HashMap<HashItemType, Integer>();
	}
	
	public static InHeatManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new InHeatManager();
		}
		return INSTANCE;
	}
	
	public Integer add(HashItemType key, int value) {
		return REGISTRY.put(key, value);
	}
	
	public int getDuration(ItemStack food) {
		HashItemType key;

		if (REGISTRY.containsKey(key = new HashItemType(food.getItem()))) {
			return REGISTRY.get(key);
		} else if (REGISTRY.containsKey(key = new HashItemType(food.getItem(), food.getItemDamage()))) {
			return REGISTRY.get(key);
		} else {
			return 0;
		}
	}
	
}

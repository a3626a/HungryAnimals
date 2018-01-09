package oortcloud.hungryanimals.entities.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class CureManager {
	private static CureManager INSTANCE;

	private List<Ingredient> REGISTRY;
	
	private CureManager() {
		REGISTRY = new ArrayList<Ingredient>();
	}
	
	public static CureManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CureManager();
		}
		return INSTANCE;
	}
	
	public boolean add(Ingredient cure) {
		return REGISTRY.add(cure);
	}
	
	public boolean isCure(ItemStack food) {
		for (Ingredient i : REGISTRY) {
			if (i.apply(food)) {
				return true;
			}
		}
		return false;
	}
}

package oortcloud.hungryanimals.entities.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class Cures {
	private static Cures INSTANCE;

	private List<Ingredient> REGISTRY;
	
	private Cures() {
		REGISTRY = new ArrayList<Ingredient>();
	}
	
	public static Cures getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Cures();
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

package oortcloud.hungryanimals.entities.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class InHeats {

	private static InHeats INSTANCE;

	private List<InHeatEntry> REGISTRY;

	public static class InHeatEntry {
		public Ingredient ingredient;
		public int duration;

		public InHeatEntry(Ingredient ingredient, int duration) {
			this.ingredient = ingredient;
			this.duration = duration;
		}
	}

	private InHeats() {
		REGISTRY = new ArrayList<InHeatEntry>();
	}

	public static InHeats getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new InHeats();
		}
		return INSTANCE;
	}

	public boolean add(Ingredient key, int value) {
		return REGISTRY.add(new InHeatEntry(key, value));
	}

	public int getDuration(ItemStack food) {
		for (InHeatEntry i : REGISTRY) {
			if (i.ingredient.apply(food)) {
				return i.duration;
			}
		}
		return 0;
	}

}

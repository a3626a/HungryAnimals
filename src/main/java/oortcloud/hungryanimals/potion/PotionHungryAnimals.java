package oortcloud.hungryanimals.potion;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionHungryAnimals extends Potion {

	public ResourceLocation texture;
	
	protected PotionHungryAnimals(ResourceLocation location, boolean badEffect, int potionColor) {
		super(badEffect, potionColor);
		texture = location;
	}

}

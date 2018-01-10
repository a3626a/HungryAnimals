package oortcloud.hungryanimals.entities.food_preferences;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;

public class FoodPreferences {
	
	private static FoodPreferences INSTANCE;

	public Map<Class<? extends EntityAnimal>, IFoodPreference<IBlockState>> REGISTRY_BLOCK;
	public Map<Class<? extends EntityAnimal>, IFoodPreference<ItemStack>> REGISTRY_ITEM;
	public Map<Class<? extends EntityAnimal>, IFoodPreferenceSimple<EntityLiving>> REGISTRY_ENTITY;
	
	private FoodPreferences() {
		REGISTRY_BLOCK = new HashMap<Class<? extends EntityAnimal>, IFoodPreference<IBlockState>>();
		REGISTRY_ITEM = new HashMap<Class<? extends EntityAnimal>, IFoodPreference<ItemStack>>();
		REGISTRY_ENTITY = new HashMap<Class<? extends EntityAnimal>, IFoodPreferenceSimple<EntityLiving>>();
	}
	
	public static FoodPreferences getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FoodPreferences();
		}
		return INSTANCE;
	}
	
}

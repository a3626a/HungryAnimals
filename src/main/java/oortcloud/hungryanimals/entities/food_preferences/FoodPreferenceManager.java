package oortcloud.hungryanimals.entities.food_preferences;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FoodPreferenceManager {
	
	private static FoodPreferenceManager INSTANCE;

	public Map<Class<? extends EntityAnimal>, IFoodPreference<IBlockState>> REGISTRY_BLOCK;
	public Map<Class<? extends EntityAnimal>, IFoodPreference<ItemStack>> REGISTRY_ITEM;
	
	private FoodPreferenceManager() {
		REGISTRY_BLOCK = new HashMap<Class<? extends EntityAnimal>, IFoodPreference<IBlockState>>();
		REGISTRY_ITEM = new HashMap<Class<? extends EntityAnimal>, IFoodPreference<ItemStack>>();
	}
	
	public static FoodPreferenceManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FoodPreferenceManager();
		}
		return INSTANCE;
	}
	
}

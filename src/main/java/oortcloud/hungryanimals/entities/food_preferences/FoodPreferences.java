package oortcloud.hungryanimals.entities.food_preferences;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.configuration.ConfigurationHandlerJSONAnimal;

public class FoodPreferences {
	
	private static FoodPreferences INSTANCE;

	public Map<Class<? extends EntityLiving>, IFoodPreference<IBlockState>> REGISTRY_BLOCK;
	public Map<Class<? extends EntityLiving>, IFoodPreference<ItemStack>> REGISTRY_ITEM;
	private Map<Class<? extends EntityLiving>, IFoodPreferenceSimple<EntityLiving>> REGISTRY_ENTITY;
	private Runnable registryEntityLoader;
	public Map<Class<? extends EntityLiving>, IFoodPreference<FluidStack>> REGISTRY_FLUID;
	
	private FoodPreferences() {
		REGISTRY_BLOCK = new HashMap<>();
		REGISTRY_ITEM = new HashMap<>();
		REGISTRY_FLUID = new HashMap<>();
	}
	
	public static FoodPreferences getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FoodPreferences();
		}
		return INSTANCE;
	}

	public Map<Class<? extends EntityLiving>, IFoodPreferenceSimple<EntityLiving>> getRegistryEntity() {
		if (REGISTRY_ENTITY == null) {
			REGISTRY_ENTITY = new HashMap<>();
			if (registryEntityLoader != null) {
				registryEntityLoader.run();
			} else {
				HungryAnimals.logger.warn("Food Preferences Entity Registry Loader is null.");
			}
		}
		
		return REGISTRY_ENTITY;
	}
	
	public void setRegistryEntityLoader(Runnable loader) {
		registryEntityLoader = loader;
	}
	
}

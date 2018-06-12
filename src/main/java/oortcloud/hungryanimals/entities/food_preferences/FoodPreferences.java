package oortcloud.hungryanimals.entities.food_preferences;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class FoodPreferences {
	
	private static FoodPreferences INSTANCE;

	public Map<Class<? extends EntityLiving>, IFoodPreference<IBlockState>> REGISTRY_BLOCK;
	public Map<Class<? extends EntityLiving>, IFoodPreference<ItemStack>> REGISTRY_ITEM;
	public Map<Class<? extends EntityLiving>, IFoodPreferenceSimple<EntityLiving>> REGISTRY_ENTITY;
	public Map<Class<? extends EntityLiving>, IFoodPreference<FluidStack>> REGISTRY_FLUID;
	
	private FoodPreferences() {
		REGISTRY_BLOCK = new HashMap<>();
		REGISTRY_ITEM = new HashMap<>();
		REGISTRY_ENTITY = new HashMap<>();
		REGISTRY_FLUID = new HashMap<>();
	}
	
	public static FoodPreferences getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FoodPreferences();
		}
		return INSTANCE;
	}
	
}

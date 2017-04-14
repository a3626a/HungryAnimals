package oortcloud.hungryanimals.entities.food_preferences;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState.HashBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;

public class HungryAnimalRegisterEvent extends Event {

	private final Class<? extends EntityAnimal> entity;

	public HungryAnimalRegisterEvent(Class<? extends EntityAnimal> entity) {
		this.entity = entity;
	}

	public Class<? extends EntityAnimal> getEntityClass() {
		return entity;
	}
	
	public static class FoodPreferenceBlockStateRegisterEvent extends HungryAnimalRegisterEvent {

		private final Map<HashBlockState, Double> map;
				
		public FoodPreferenceBlockStateRegisterEvent(Class<? extends EntityAnimal> entity, Map<HashBlockState, Double> map) {
			super(entity);
			this.map = map;
		}
		
		public void put(Block block, double hunger) {
			map.put(new HashBlockState(block), hunger);
		}
		
		public void put(IBlockState block, boolean ignoreProperty, double hunger) {
			map.put(new HashBlockState(block, ignoreProperty), hunger);
		}
		
		public Map<HashBlockState, Double> getMap() {
			return map;
		}
	}

	public static class FoodPreferenceItemStackRegisterEvent extends HungryAnimalRegisterEvent {

		private final Map<HashItemType, Double> map;
				
		public FoodPreferenceItemStackRegisterEvent(Class<? extends EntityAnimal> entity, Map<HashItemType, Double> map) {
			super(entity);
			this.map = map;
		}
		
		public void put(Item item, double hunger) {
			map.put(new HashItemType(item), hunger);
		}
		
		public void put(Item item, int damage, double hunger) {
			map.put(new HashItemType(item, damage), hunger);
		}
		
		public void put(ItemStack itemstack, boolean ignoreDamage, double hunger) {
			if (ignoreDamage) {
				map.put(new HashItemType(itemstack.getItem()), hunger);
			} else {
				map.put(new HashItemType(itemstack.getItem(), itemstack.getItemDamage()), hunger);
			}
		}
		
		public Map<HashItemType, Double> getMap() {
			return map;
		}
		
	}
	
}

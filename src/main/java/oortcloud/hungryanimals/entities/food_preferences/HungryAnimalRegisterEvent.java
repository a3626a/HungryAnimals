package oortcloud.hungryanimals.entities.food_preferences;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;
import oortcloud.hungryanimals.entities.event.EntityEventHandler.Pair;
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

		private final Map<HashBlockState, Pair<Double, Double>> map;
				
		public FoodPreferenceBlockStateRegisterEvent(Class<? extends EntityAnimal> entity, Map<HashBlockState, Pair<Double, Double>> map) {
			super(entity);
			this.map = map;
		}
		
		public void put(Block block, Pair<Double, Double> hunger) {
			map.put(new HashBlockState(block), hunger);
		}
		
		public void put(IBlockState block, boolean ignoreProperty, Pair<Double, Double> hunger) {
			map.put(new HashBlockState(block, ignoreProperty), hunger);
		}
		
		public Map<HashBlockState, Pair<Double, Double>> getMap() {
			return map;
		}
	}

	public static class FoodPreferenceItemStackRegisterEvent extends HungryAnimalRegisterEvent {

		private final Map<HashItemType, Pair<Double, Double>> map;
				
		public FoodPreferenceItemStackRegisterEvent(Class<? extends EntityAnimal> entity, Map<HashItemType, Pair<Double, Double>> map) {
			super(entity);
			this.map = map;
		}
		
		public void put(Item item, Pair<Double, Double> hunger) {
			map.put(new HashItemType(item), hunger);
		}
		
		public void put(Item item, int damage, Pair<Double, Double> hunger) {
			map.put(new HashItemType(item, damage), hunger);
		}
		
		public void put(ItemStack itemstack, boolean ignoreDamage, Pair<Double, Double> hunger) {
			if (ignoreDamage) {
				map.put(new HashItemType(itemstack.getItem()), hunger);
			} else {
				map.put(new HashItemType(itemstack.getItem(), itemstack.getItemDamage()), hunger);
			}
		}
		
		public Map<HashItemType, Pair<Double, Double>> getMap() {
			return map;
		}
		
	}
	
}

package oortcloud.hungryanimals.entities.food_preferences;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.eventhandler.Event;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState.HashBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;

public class FoodPreferenceRegisterEvent extends Event {

	private final Class<? extends EntityAnimal> entity;

	public FoodPreferenceRegisterEvent(Class<? extends EntityAnimal> entity) {
		this.entity = entity;
	}

	public Class<? extends EntityAnimal> getEntityClass() {
		return entity;
	}
	
	public static class FoodPreferenceBlockStateRegisterEvent extends FoodPreferenceRegisterEvent {

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
		
	}

	public static class FoodPreferenceItemStackRegisterEvent extends FoodPreferenceRegisterEvent {

		private final List<Tuple<HashItemType, Double>> list;
				
		public FoodPreferenceItemStackRegisterEvent(Class<? extends EntityAnimal> entity) {
			super(entity);
			list = new LinkedList<Tuple<HashItemType, Double>>();
		}
		
		public void put(Item item, double hunger) {
			list.add(new Tuple<HashItemType, Double>(new HashItemType(item), hunger));
		}
		
		public void put(Item item, int damage, double hunger) {
			list.add(new Tuple<HashItemType, Double>(new HashItemType(item, damage), hunger));
		}
		
		public void put(ItemStack itemstack, boolean ignoreDamage, double hunger) {
			if (ignoreDamage) {
				list.add(new Tuple<HashItemType, Double>(new HashItemType(itemstack.getItem()), hunger));
			} else {
				list.add(new Tuple<HashItemType, Double>(new HashItemType(itemstack.getItem(), itemstack.getItemDamage()), hunger));
			}
		}
		
	}
	
}

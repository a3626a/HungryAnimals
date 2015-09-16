package oortcloud.hungryanimals.entities.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.items.ModItems;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class EntityAIAvoidPlayer extends EntityAIAvoidEntity {
	/**
	 * Wild animals avoid players within certain range.
	 * 
	 */

	private static final Predicate predicate = new Predicate() {
		/**
		 * Select players
		 * Players who has "debug glass" in creative mode are ignored.
		 */
		
		public boolean apply(EntityPlayer player) {
			if (!player.capabilities.isCreativeMode)
				return true;
			for (int i = 0; i < 9; i++) {
				ItemStack itemStack = player.inventory.mainInventory[i];
				if (itemStack != null && itemStack.getItem() == ModItems.debugGlass)
					return false;
			}
			return true;
		}
		public boolean apply(Object obj) {
			return (obj instanceof EntityPlayer ? this.apply((EntityPlayer) obj) : false);
		}
	};

	private ExtendedPropertiesHungryAnimal property;
	
	public EntityAIAvoidPlayer(EntityCreature entity, ExtendedPropertiesHungryAnimal property, float radius, double farspeed, double nearspeed) {
		super(entity, EntityAIAvoidPlayer.predicate, radius, farspeed, nearspeed);
		this.property=property;
	}
	
	@Override
	public boolean shouldExecute() {
		return property.taming<-1 && super.shouldExecute();
	}

}

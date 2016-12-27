package oortcloud.hungryanimals.entities.ai;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.items.ModItems;

public class EntityAIAvoidPlayer extends EntityAIAvoidEntity<EntityPlayer> {
	/**
	 * Wild animals avoid players within certain range.
	 * 
	 */

	private static final Predicate<EntityPlayer> predicate = new Predicate<EntityPlayer>() {
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
		
	};

	private ExtendedPropertiesHungryAnimal property;
	
	public EntityAIAvoidPlayer(EntityCreature entity, ExtendedPropertiesHungryAnimal property, float radius, double farspeed, double nearspeed) {
		super(entity, EntityPlayer.class, predicate, radius, farspeed, nearspeed);
		this.property=property;
	}
	
	@Override
	public boolean shouldExecute() {
		return property.taming<-1 && super.shouldExecute();
	}

}

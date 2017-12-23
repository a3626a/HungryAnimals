package oortcloud.hungryanimals.entities.ai;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.capability.TamingLevel;
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
				ItemStack itemStack = player.inventory.mainInventory.get(i);
				if (itemStack != ItemStack.EMPTY && itemStack.getItem() == ModItems.debugGlass)
					return false;
			}
			return true;
		}
		
	};

	public EntityAIAvoidPlayer(EntityCreature entity, float radius, double farspeed, double nearspeed) {
		super(entity, EntityPlayer.class, predicate, radius, farspeed, nearspeed);
	}
	
	@Override
	public boolean shouldExecute() {
		return this.entity.getCapability(ProviderTamableAnimal.CAP, null).getTamingLevel() == TamingLevel.WILD && super.shouldExecute();
	}

}

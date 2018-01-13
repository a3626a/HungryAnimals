package oortcloud.hungryanimals.entities.ai;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;
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

	public static void parse(JsonElement jsonEle, AIContainer aiContainer) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("AI Avoid Player must be an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		
		JsonObject jsonObject = (JsonObject)jsonEle ;
		
		float radius = JsonUtils.getFloat(jsonObject, "radius");
		double farspeed = JsonUtils.getFloat(jsonObject, "farspeed");
		double nearspeed = JsonUtils.getFloat(jsonObject, "nearspeed");
		
		AIFactory factory =  (entity) -> new EntityAIAvoidPlayer(entity, radius, farspeed, nearspeed);
		aiContainer.getTask().after(EntityAISwimming.class)
		                     .before(EntityAIMateModified.class)
		                     .before(EntityAIMoveToTrough.class)
		                     .before(EntityAITemptIngredient.class)
		                     .before(EntityAITemptEdibleItem.class)
		                     .before(EntityAIMoveToEatItem.class)
		                     .before(EntityAIMoveToEatBlock.class)
		                     .before(EntityAIFollowParent.class)
		                     .put(factory);
	}
	
}

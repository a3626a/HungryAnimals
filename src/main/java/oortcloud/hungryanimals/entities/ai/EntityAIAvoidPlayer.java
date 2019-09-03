package oortcloud.hungryanimals.entities.ai;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.capability.TamingLevel;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.utils.Tamings;

public class EntityAIAvoidPlayer extends EntityAIAvoidEntity<EntityPlayer> {
	
	@Nullable
	private ICapabilityTamableAnimal cap;
	
	private static final Predicate<EntityPlayer> predicate = new Predicate<EntityPlayer>() {
		/**
		 * Select players
		 * Players who has "debug glass" in creative mode are ignored.
		 */
		public boolean apply(@Nullable EntityPlayer player) {
			if (player == null)
				return false;
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
		cap = entity.getCapability(ProviderTamableAnimal.CAP, null);
	}
	
	@Override
	public boolean shouldExecute() {
		return Tamings.getLevel(cap) == TamingLevel.WILD && super.shouldExecute();
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
		
		AIFactory factory =  (entity) -> {
			if (entity instanceof EntityCreature) {
				return new EntityAIAvoidPlayer((EntityCreature) entity, radius, farspeed, nearspeed);
			} else {
				HungryAnimals.logger.error("Animals which uses AI Avoid Player must extend EntityCreature. {} don't.", EntityList.getKey(entity));
				return null;
			}
		};
		aiContainer.getTask().after(EntityAISwimming.class)
		                     .before(EntityAIMateModified.class)
		                     .before(EntityAIMoveToTrough.class)
		                     .before(EntityAITemptIngredient.class)
		                     .before(EntityAITemptEdibleItem.class)
		                     .before(EntityAIMoveToEatItem.class)
		                     .before(EntityAIMoveToEatBlock.class)
		                     .before(EntityAIFollowParent.class)
		                     .before(EntityAIWanderAvoidWater.class)
		                     .put(factory);
	}
	
}

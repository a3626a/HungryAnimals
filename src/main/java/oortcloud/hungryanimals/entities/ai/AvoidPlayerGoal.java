package oortcloud.hungryanimals.entities.ai;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.ai.handler.AIContainer;
import oortcloud.hungryanimals.entities.ai.handler.AIFactory;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.capability.TamingLevel;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.utils.Tamings;

import java.util.function.Predicate;

public class AvoidPlayerGoal extends AvoidEntityGoal<PlayerEntity> {
	
	@Nullable
	private ICapabilityTamableAnimal cap;
	
	private static final Predicate<LivingEntity> predicate = new Predicate<LivingEntity>() {
		/**
		 * Select players
		 * Players who has "debug glass" in creative mode are ignored.
		 */
		public boolean test(@Nullable LivingEntity livingEntity) {
			if (livingEntity == null)
				return false;
			if (!(livingEntity instanceof PlayerEntity))
				return false;

			PlayerEntity player = (PlayerEntity)livingEntity;
			if (!player.abilities.isCreativeMode)
				return true;
			for (int i = 0; i < 9; i++) {
				ItemStack itemStack = player.inventory.mainInventory.get(i);
				if (itemStack != ItemStack.EMPTY && itemStack.getItem() == ModItems.DEBUG_GLASS.get())
					return false;
			}
			return true;
		}
	};
	
	public AvoidPlayerGoal(CreatureEntity entity, float radius, double farspeed, double nearspeed) {
		super(entity, PlayerEntity.class, radius, farspeed, nearspeed, predicate);
		cap = entity.getCapability(ProviderTamableAnimal.CAP).orElse(null);
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
		
		float radius = JSONUtils.getFloat(jsonObject, "radius");
		double farspeed = JSONUtils.getFloat(jsonObject, "farspeed");
		double nearspeed = JSONUtils.getFloat(jsonObject, "nearspeed");
		
		AIFactory factory =  (entity) -> {
			if (entity instanceof CreatureEntity) {
				return new AvoidPlayerGoal((CreatureEntity) entity, radius, farspeed, nearspeed);
			} else {
				HungryAnimals.logger.error("Animals which uses AI Avoid Player must extend CreatureEntity. {} don't.", entity.getType().getRegistryName());
				return null;
			}
		};
		aiContainer.getTask().after(SwimGoal.class)
		                     .before(MateModifiedGoal.class)
		                     .before(MoveToTroughGoal.class)
		                     .before(IngredientTemptGoal.class)
		                     .before(EdibleItemTemptGoal.class)
		                     .before(MoveToEatItemGoal.class)
		                     .before(MoveToEatBlockGoal.class)
		                     .before(EntityAIFollowParent.class)
		                     .before(WaterAvoidingRandomWalkingGoal.class)
		                     .put(factory);
	}
	
}

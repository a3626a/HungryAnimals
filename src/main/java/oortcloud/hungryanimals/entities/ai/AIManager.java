package oortcloud.hungryanimals.entities.ai;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraftforge.common.MinecraftForge;

public class AIManager {

	private static AIManager INSTANCE;

	public Map<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>> REGISTRY;
	public Map<String, Function<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>>> FACTORIES;

	private AIManager() {
		REGISTRY = new HashMap<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>>();
		FACTORIES = new HashMap<String, Function<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>>>();
	}

	public static AIManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AIManager();
		}
		return INSTANCE;
	}

	public void init() {
		FACTORIES.put("herbivore", (animal) -> {
			AIContainer aiContainer = new AIContainer(0);
			aiContainer.putLast((entity) -> new EntityAISwimming(entity ));
			aiContainer.putLast((entity) -> new EntityAIAvoidPlayer(entity, 16.0F, 1.0D, 2.0D));
			aiContainer.putLast((entity) -> new EntityAIMateModified(entity, 2.0D));
			aiContainer.putLast((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
			aiContainer.putLast((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
			aiContainer.putLast((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
			aiContainer.putLast((entity) -> new EntityAIMoveToEatBlock(entity, 1.0D));
			aiContainer.putLast((entity) -> new EntityAIWander(entity, 1.0D));
			aiContainer.putLast((entity) -> new EntityAIWatchClosest(entity, EntityPlayer.class, 6.0F));
			aiContainer.putLast((entity) -> new EntityAILookIdle(entity));
			aiContainer.removeAll();
			return aiContainer;
		});
		// TODO After JSON load
		MinecraftForge.EVENT_BUS.post(new AIContainerRegisterEvent(EntityChicken.class, chicken));
		REGISTRY.put(EntityChicken.class, chicken);
		MinecraftForge.EVENT_BUS.post(new AIContainerRegisterEvent(EntityCow.class, cow));
		REGISTRY.put(EntityCow.class, cow);
		MinecraftForge.EVENT_BUS.post(new AIContainerRegisterEvent(EntityMooshroom.class, mooshroom));
		REGISTRY.put(EntityMooshroom.class, mooshroom);

		FACTORIES.put("pig", (animal) -> {
			AIContainer aiContainer = new AIContainer(1);
			aiContainer.putLast((entity) -> new EntityAIAvoidPlayer(entity, 16.0F, 1.0D, 2.0D));
			aiContainer.putLast((entity) -> new EntityAIMateModified(entity, 2.0D));
			aiContainer.putLast((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
			aiContainer.putLast((entity) -> new EntityAITempt(entity, 1.5D, Items.CARROT_ON_A_STICK, false));
			aiContainer.putLast((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
			aiContainer.putLast((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
			aiContainer.putLast((entity) -> new EntityAIMoveToEatBlock(entity, 1.0D));
			aiContainer.putLast((entity) -> new EntityAIWander(entity, 1.0D));
			aiContainer.putLast((entity) -> new EntityAIWatchClosest(entity, EntityPlayer.class, 6.0F));
			aiContainer.putLast((entity) -> new EntityAILookIdle(entity));
			aiContainer.remove(EntityAITempt.class);
			aiContainer.remove(EntityAIFollowParent.class);
			aiContainer.remove(EntityAIWander.class);
			aiContainer.remove(EntityAIMate.class);
			aiContainer.remove(EntityAIPanic.class);
			aiContainer.remove(EntityAIWatchClosest.class);
			aiContainer.remove(EntityAILookIdle.class);
			return aiContainer;
		});
		MinecraftForge.EVENT_BUS.post(new AIContainerRegisterEvent(EntityPig.class, pig));
		REGISTRY.put(EntityPig.class, pig);

		FACTORIES.put("rabbit", (animal) -> {
			AIContainer aiContainer = new AIContainer(1);
			aiContainer.putLast((entity) -> new EntityAIAvoidPlayer(entity, 16.0F, 1.0D, 2.0D));
			aiContainer.putLast((entity) -> new EntityAIMateModified(entity, 2.0D));
			aiContainer.putLast((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
			aiContainer.putLast((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
			aiContainer.putLast((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
			aiContainer.putLast((entity) -> new EntityAIMoveToEatBlock(entity, 1.0D));
			aiContainer.putLast((entity) -> new EntityAIWander(entity, 1.0D));
			aiContainer.putLast((entity) -> new EntityAIWatchClosest(entity, EntityPlayer.class, 6.0F));
			aiContainer.putLast((entity) -> new EntityAILookIdle(entity));
			for (Class<?> i : EntityRabbit.class.getDeclaredClasses()) {
				if (i.isAssignableFrom(EntityAIBase.class))
					aiContainer.remove(i.asSubclass(EntityAIBase.class));
			}
			aiContainer.remove(EntityAITempt.class);
			aiContainer.remove(EntityAIFollowParent.class);
			aiContainer.remove(EntityAIWander.class);
			aiContainer.remove(EntityAIMate.class);
			aiContainer.remove(EntityAIPanic.class);
			aiContainer.remove(EntityAIWatchClosest.class);
			aiContainer.remove(EntityAILookIdle.class);
			return aiContainer;
		});
		MinecraftForge.EVENT_BUS.post(new AIContainerRegisterEvent(EntityRabbit.class, rabbit));
		REGISTRY.put(EntityRabbit.class, rabbit);

		FACTORIES.put("sheep", (animal) -> {
			AIContainer aiContainer = new AIContainer(1);
			aiContainer.putLast((entity) -> new EntityAIAvoidPlayer(entity, 16.0F, 1.0D, 2.0D));
			aiContainer.putLast((entity) -> new EntityAIMateModified(entity, 2.0D));
			aiContainer.putLast((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
			aiContainer.putLast((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
			aiContainer.putLast((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
			aiContainer.putLast((entity) -> new EntityAIMoveToEatBlock(entity, 1.0D));
			aiContainer.putLast((entity) -> new EntityAIWander(entity, 1.0D));
			aiContainer.putLast((entity) -> new EntityAIWatchClosest(entity, EntityPlayer.class, 6.0F));
			aiContainer.putLast((entity) -> new EntityAILookIdle(entity));
			aiContainer.remove(EntityAITempt.class);
			aiContainer.remove(EntityAIFollowParent.class);
			aiContainer.remove(EntityAIWander.class);
			aiContainer.remove(EntityAIMate.class);
			aiContainer.remove(EntityAIPanic.class);
			aiContainer.remove(EntityAIWatchClosest.class);
			aiContainer.remove(EntityAILookIdle.class);
			aiContainer.remove(EntityAIEatGrass.class);
			return aiContainer;
		});
		MinecraftForge.EVENT_BUS.post(new AIContainerRegisterEvent(EntitySheep.class, sheep));
		REGISTRY.put(EntitySheep.class, sheep);
	}

}

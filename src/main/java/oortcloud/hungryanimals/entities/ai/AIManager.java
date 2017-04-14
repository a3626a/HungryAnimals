package oortcloud.hungryanimals.entities.ai;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
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

	private AIManager() {
		REGISTRY = new HashMap<Class<? extends EntityAnimal>, IAIContainer<EntityAnimal>>();
	}
	
	public static AIManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AIManager();
		}
		return INSTANCE;
	}

	public void init() {
		AIContainer chicken = new AIContainer(1);
		chicken.putLast((entity) -> new EntityAIAvoidPlayer(entity, 16.0F, 1.0D, 2.0D));
		chicken.putLast((entity) -> new EntityAIMateModified(entity, 2.0D));
		chicken.putLast((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
		chicken.putLast((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
		chicken.putLast((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
		chicken.putLast((entity) -> new EntityAIMoveToEatBlockChicken(entity, 1.0D));
		chicken.putLast((entity) -> new EntityAIWander(entity, 1.0D));
		chicken.putLast((entity) -> new EntityAIWatchClosest(entity, EntityPlayer.class, 6.0F));
		chicken.putLast((entity) ->  new EntityAILookIdle(entity));
		chicken.remove(EntityAITempt.class);
		chicken.remove(EntityAIFollowParent.class);
		chicken.remove(EntityAIWander.class);
		chicken.remove(EntityAIMate.class);
		chicken.remove(EntityAIPanic.class);
		chicken.remove(EntityAIWatchClosest.class);
		chicken.remove(EntityAILookIdle.class);
		MinecraftForge.EVENT_BUS.post(new AIContainerRegisterEvent(EntityChicken.class, chicken));
		REGISTRY.put(EntityChicken.class, chicken);

		AIContainer cow = new AIContainer(1);
		cow.putLast((entity) -> new EntityAIAvoidPlayer(entity, 16.0F, 1.0D, 2.0D));
		cow.putLast((entity) -> new EntityAIMateModified(entity, 2.0D));
		cow.putLast((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
		cow.putLast((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
		cow.putLast((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
		cow.putLast((entity) -> new EntityAIMoveToEatBlock(entity, 1.0D));
		cow.putLast((entity) -> new EntityAIWander(entity, 1.0D));
		cow.putLast((entity) -> new EntityAIWatchClosest(entity, EntityPlayer.class, 6.0F));
		cow.putLast((entity) ->  new EntityAILookIdle(entity));
		cow.remove(EntityAITempt.class);
		cow.remove(EntityAIFollowParent.class);
		cow.remove(EntityAIWander.class);
		cow.remove(EntityAIMate.class);
		cow.remove(EntityAIPanic.class);
		cow.remove(EntityAIWatchClosest.class);
		cow.remove(EntityAILookIdle.class);
		MinecraftForge.EVENT_BUS.post(new AIContainerRegisterEvent(EntityCow.class, cow));
		REGISTRY.put(EntityCow.class, cow);
		
		AIContainer mooshroom = new AIContainer(1);
		mooshroom.putLast((entity) -> new EntityAIAvoidPlayer(entity, 16.0F, 1.0D, 2.0D));
		mooshroom.putLast((entity) -> new EntityAIMateModified(entity, 2.0D));
		mooshroom.putLast((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
		mooshroom.putLast((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
		mooshroom.putLast((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
		mooshroom.putLast((entity) -> new EntityAIMoveToEatBlock(entity, 1.0D));
		mooshroom.putLast((entity) -> new EntityAIWander(entity, 1.0D));
		mooshroom.putLast((entity) -> new EntityAIWatchClosest(entity, EntityPlayer.class, 6.0F));
		mooshroom.putLast((entity) ->  new EntityAILookIdle(entity));
		mooshroom.remove(EntityAITempt.class);
		mooshroom.remove(EntityAIFollowParent.class);
		mooshroom.remove(EntityAIWander.class);
		mooshroom.remove(EntityAIMate.class);
		mooshroom.remove(EntityAIPanic.class);
		mooshroom.remove(EntityAIWatchClosest.class);
		mooshroom.remove(EntityAILookIdle.class);
		MinecraftForge.EVENT_BUS.post(new AIContainerRegisterEvent(EntityMooshroom.class, mooshroom));
		REGISTRY.put(EntityMooshroom.class, mooshroom);
		
		AIContainer pig = new AIContainer(1);
		pig.putLast((entity) -> new EntityAIAvoidPlayer(entity, 16.0F, 1.0D, 2.0D));
		pig.putLast((entity) -> new EntityAIMateModified(entity, 2.0D));
		pig.putLast((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
		pig.putLast((entity) -> new EntityAITempt(entity, 1.5D, Items.CARROT_ON_A_STICK, false));
		pig.putLast((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
		pig.putLast((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
		pig.putLast((entity) -> new EntityAIMoveToEatBlock(entity, 1.0D));
		pig.putLast((entity) -> new EntityAIWander(entity, 1.0D));
		pig.putLast((entity) -> new EntityAIWatchClosest(entity, EntityPlayer.class, 6.0F));
		pig.putLast((entity) ->  new EntityAILookIdle(entity));
		pig.remove(EntityAITempt.class);
		pig.remove(EntityAIFollowParent.class);
		pig.remove(EntityAIWander.class);
		pig.remove(EntityAIMate.class);
		pig.remove(EntityAIPanic.class);
		pig.remove(EntityAIWatchClosest.class);
		pig.remove(EntityAILookIdle.class);
		MinecraftForge.EVENT_BUS.post(new AIContainerRegisterEvent(EntityPig.class, pig));
		REGISTRY.put(EntityPig.class, pig);

		AIContainer rabbit = new AIContainer(1);
		rabbit.putLast((entity) -> new EntityAIAvoidPlayer(entity, 16.0F, 1.0D, 2.0D));
		rabbit.putLast((entity) -> new EntityAIMateModified(entity, 2.0D));
		rabbit.putLast((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
		rabbit.putLast((entity) -> new EntityAITempt(entity, 1.5D, Items.CARROT_ON_A_STICK, false));
		rabbit.putLast((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
		rabbit.putLast((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
		rabbit.putLast((entity) -> new EntityAIMoveToEatBlock(entity, 1.0D));
		rabbit.putLast((entity) -> new EntityAIWander(entity, 1.0D));
		rabbit.putLast((entity) -> new EntityAIWatchClosest(entity, EntityPlayer.class, 6.0F));
		rabbit.putLast((entity) ->  new EntityAILookIdle(entity));
		for (Class<?> i : EntityRabbit.class.getDeclaredClasses()) {
			if (i.isAssignableFrom(EntityAIBase.class))
				rabbit.remove(i.asSubclass(EntityAIBase.class));
		}
		rabbit.remove(EntityAITempt.class);
		rabbit.remove(EntityAIFollowParent.class);
		rabbit.remove(EntityAIWander.class);
		rabbit.remove(EntityAIMate.class);
		rabbit.remove(EntityAIPanic.class);
		rabbit.remove(EntityAIWatchClosest.class);
		rabbit.remove(EntityAILookIdle.class);
		MinecraftForge.EVENT_BUS.post(new AIContainerRegisterEvent(EntityRabbit.class, rabbit));
		REGISTRY.put(EntityRabbit.class, rabbit);
		
		AIContainer sheep = new AIContainer(1);
		sheep.putLast((entity) -> new EntityAIAvoidPlayer(entity, 16.0F, 1.0D, 2.0D));
		sheep.putLast((entity) -> new EntityAIMateModified(entity, 2.0D));
		sheep.putLast((entity) -> new EntityAIMoveToTrough(entity, 1.0D));
		sheep.putLast((entity) -> new EntityAITemptEdibleItem(entity, 1.5D, false));
		sheep.putLast((entity) -> new EntityAIMoveToEatItem(entity, 1.5D));
		sheep.putLast((entity) -> new EntityAIMoveToEatBlock(entity, 1.0D));
		sheep.putLast((entity) -> new EntityAIWander(entity, 1.0D));
		sheep.putLast((entity) -> new EntityAIWatchClosest(entity, EntityPlayer.class, 6.0F));
		sheep.putLast((entity) ->  new EntityAILookIdle(entity));
		sheep.remove(EntityAITempt.class);
		sheep.remove(EntityAIFollowParent.class);
		sheep.remove(EntityAIWander.class);
		sheep.remove(EntityAIMate.class);
		sheep.remove(EntityAIPanic.class);
		sheep.remove(EntityAIWatchClosest.class);
		sheep.remove(EntityAILookIdle.class);
		sheep.remove(EntityAIEatGrass.class);
		MinecraftForge.EVENT_BUS.post(new AIContainerRegisterEvent(EntitySheep.class, sheep));
		REGISTRY.put(EntitySheep.class, sheep);
	}

}

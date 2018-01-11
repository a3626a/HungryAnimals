package oortcloud.hungryanimals.entities.ai;

import java.util.List;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class EntityAITemptIngredient extends EntityAITempt {

	private List<Ingredient> tempt;
	
	public EntityAITemptIngredient(EntityCreature entity, double speed, boolean scaredBy, List<Ingredient> items) {
		super(entity, speed, null, scaredBy);
		this.tempt = items;
	}
	
    protected boolean isTempting(ItemStack stack)
    {
    	for (Ingredient i : tempt) {
    		if (i.apply(stack))
    			return true;
    	}
    	return false;
    }
	
}

package oortcloud.hungryanimals.api.jei.production;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.production.IProductionJEI;

public class RecipeWrapperProductionEgg implements IRecipeWrapper {

	private IProductionJEI recipe;
	private Class<? extends EntityLiving> entityClass;
	private IJeiHelpers jeiHelpers;

	public RecipeWrapperProductionEgg(IJeiHelpers jeiHelpers, Class<? extends EntityLiving> entityClass,
			IProductionJEI production) {
		this.jeiHelpers = jeiHelpers;
		this.entityClass = entityClass;
		this.recipe = production;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ItemStack spawnEgg = new ItemStack(Items.SPAWN_EGG);
		ItemMonsterPlacer.applyEntityIdToItemStack(spawnEgg, EntityList.getKey(entityClass));
		ingredients.setInput(ItemStack.class, spawnEgg);
		recipe.getIngredients(jeiHelpers, ingredients);
	}

}

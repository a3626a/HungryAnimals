package oortcloud.hungryanimals.api.jei.production;

import com.google.common.collect.Lists;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.MobEntity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.production.IProductionJEI;

public class RecipeWrapperProductionShear implements IRecipeWrapper {

	private IProductionJEI recipe;
	private Class<? extends MobEntity> entityClass;
	private IJeiHelpers jeiHelpers;

	public RecipeWrapperProductionShear(IJeiHelpers jeiHelpers, Class<? extends MobEntity> entityClass,
			IProductionJEI production) {
		this.jeiHelpers = jeiHelpers;
		this.entityClass = entityClass;
		this.recipe = production;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		recipe.getIngredients(jeiHelpers, ingredients);
		ItemStack spawnEgg = new ItemStack(Items.SPAWN_EGG);
		ItemMonsterPlacer.applyEntityIdToItemStack(spawnEgg, EntityList.getKey(entityClass));
		ingredients.getInputs(ItemStack.class).add(0, Lists.newArrayList(spawnEgg));
		
	}

}

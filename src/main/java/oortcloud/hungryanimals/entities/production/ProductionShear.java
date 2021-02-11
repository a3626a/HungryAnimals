package oortcloud.hungryanimals.entities.production;

import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import oortcloud.hungryanimals.api.jei.production.RecipeCategoryProductionShear;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.production.condition.Conditions;
import oortcloud.hungryanimals.entities.production.utils.IRange;
import oortcloud.hungryanimals.entities.production.utils.RangeConstant;
import oortcloud.hungryanimals.entities.production.utils.RangeRandom;

/**
 * This Production is different from IShearable
 * 
 * @author LeeChangHwan
 *
 */
public class ProductionShear extends ProductionInteraction {

	private ItemStack input;
	private ItemStack output;
	private int damage;
	private Predicate<MobEntity> condition;
	private boolean disableSound;

	public ProductionShear(String name, MobEntity animal, IRange delay, ItemStack tool, ItemStack wool, int damage,
			Predicate<MobEntity> condition, boolean disableSound) {
		super(name, animal, delay);
		this.input = tool;
		this.output = wool;
		this.damage = damage;
		this.condition = condition;
		this.disableSound = disableSound;
	}

	@Override
	public ActionResultType interact(EntityInteract event, Hand hand, @Nonnull ItemStack itemstack) {
		PlayerEntity player = event.getPlayerEntity();
		if (canProduce()) {
			if (condition.apply(animal)) {
				if (!input.isEmpty() && itemstack.getItem() == input.getItem()) {
					if (!animal.getEntityWorld().isRemote) {
						animal.entityDropItem(output.copy(), 1.0F);
						itemstack.damageItem(damage, player);
						if (!disableSound) {
							animal.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
						}
					}
					resetCooldown();
					return ActionResultType.SUCCESS;
				}
			}
		}
		return ActionResultType.PASS;
	}

	public static Function<MobEntity, IProduction> parse(JsonElement jsonEle) {
		JsonObject jsonObj = jsonEle.getAsJsonObject();

		String name = JSONUtils.getString(jsonObj, "name");
		IRange delay;
		JsonElement jsonDelay = jsonObj.get("delay");
		if (jsonDelay.isJsonObject()) {
			JsonObject jsonObjDelay = jsonDelay.getAsJsonObject();
			int min = JSONUtils.getInt(jsonObjDelay, "min");
			int max = JSONUtils.getInt(jsonObjDelay, "max");
			delay = new RangeRandom(min, max);
		} else {
			delay = new RangeConstant(jsonDelay.getAsInt());
		}
		int damage = JSONUtils.getInt(jsonObj, "damage");
		Predicate<MobEntity> condition = Conditions.parse(JSONUtils.getJsonObject(jsonObj, "condition"));
		boolean disableSound = JSONUtils.getBoolean(jsonObj, "disable_sound");
		ItemStack input = CraftingHelper.getItemStack(JSONUtils.getJsonObject(jsonObj, "input"),
				new JsonContext(References.MODID));
		ItemStack output = CraftingHelper.getItemStack(JSONUtils.getJsonObject(jsonObj, "output"),
				new JsonContext(References.MODID));
		return new ProductionFactory() {
			@Override
			public IProduction apply(MobEntity animal) {
				return new ProductionShear(name, animal, delay, input, output, damage, condition, disableSound);
			}

			@Override
			public void getIngredients(IJeiHelpers jeiHelpers, IIngredients ingredients) {
				ingredients.setInput(ItemStack.class, input);
				ingredients.setOutput(ItemStack.class, output);
			}
			
			@Override
			public String getCategoryUid() {
				return RecipeCategoryProductionShear.UID;
			}
		};
	}

	@Override
	public String getMessage() {
		if (condition.apply(animal)) {
			return super.getMessage();
		} else {
			return null;
		}
	}

}

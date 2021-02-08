package oortcloud.hungryanimals.entities.production;

import java.util.function.Function;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.entity.MobEntity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import oortcloud.hungryanimals.api.jei.production.RecipeCategoryProductionEgg;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.production.condition.Conditions;
import oortcloud.hungryanimals.entities.production.utils.IRange;
import oortcloud.hungryanimals.entities.production.utils.RangeConstant;
import oortcloud.hungryanimals.entities.production.utils.RangeRandom;

public class ProductionEgg implements IProductionTickable, IProductionTOP {

	private int cooldown;
	private IRange delay;
	private ItemStack stack;
	private Predicate<MobEntity> condition;
	private boolean disableSound;
	protected MobEntity animal;

	private String name;

	public ProductionEgg(String name, MobEntity animal, IRange delay, ItemStack stack,
			Predicate<MobEntity> condition, boolean disableSound) {
		this.name = name;
		this.delay = delay;
		this.animal = animal;
		this.stack = stack;
		this.condition = condition;
		this.disableSound = disableSound;
		resetCooldown();
	}

	@Override
	public void update() {
		cooldown--;

		if (canProduce()) {
			if (condition.apply(animal)) {
				produce();
				resetCooldown();
			}
		}
	}

	private boolean canProduce() {
		return cooldown <= 0;
	}

	private void resetCooldown() {
		cooldown = delay.get(animal);
	}

	private void produce() {
		if (!animal.getEntityWorld().isRemote) {
			if (!disableSound) {
				animal.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F,
						(animal.getRNG().nextFloat() - animal.getRNG().nextFloat()) * 0.2F + 1.0F);
			}
			animal.entityDropItem(stack.copy(), 0);
		}
	}

	@Override
	public INBT writeNBT() {
		return new NBTTagInt(cooldown);
	}

	@Override
	public void readNBT(INBT nbt) {
		cooldown = ((NBTTagInt) nbt).getInt();
	}

	@Override
	public String getName() {
		return name;
	}

	public int getCooldown() {
		return cooldown;
	}

	@Override
	public String getMessage() {
		if (condition.apply(animal)) {
			if (cooldown < 0) {
				return String.format("%s now", name);
			}
			return String.format("%s after %d seconds", name, cooldown);
		} else {
			return null;
		}
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
		ItemStack stack = CraftingHelper.getItemStack(JSONUtils.getJsonObject(jsonObj, "output"),
				new JsonContext(References.MODID));
		Predicate<MobEntity> condition = Conditions.parse(JSONUtils.getJsonObject(jsonObj, "condition"));
		boolean disableSound = JSONUtils.getBoolean(jsonObj, "disable_sound");

		return new ProductionFactory() {
			@Override
			public IProduction apply(MobEntity animal) {
				return new ProductionEgg(name, animal, delay, stack, condition, disableSound);
			}

			@Override
			public void getIngredients(IJeiHelpers jeiHelpers, IIngredients ingredients) {
				ingredients.setOutput(ItemStack.class, stack);
			}
			
			@Override
			public String getCategoryUid() {
				return RecipeCategoryProductionEgg.UID;
			}
		};
	}

}

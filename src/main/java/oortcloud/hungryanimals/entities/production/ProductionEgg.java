package oortcloud.hungryanimals.entities.production;

import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.production.utils.IRange;
import oortcloud.hungryanimals.entities.production.utils.RangeConstant;
import oortcloud.hungryanimals.entities.production.utils.RangeRandom;

public class ProductionEgg implements IProductionTickable, IProductionTOP {

	private int cooldown;
	private IRange delay;
	private ItemStack stack;
	private boolean shouldAdult;
	private boolean disableSound;
	protected EntityAnimal animal;

	private String name;

	public ProductionEgg(String name, EntityAnimal animal, IRange delay, ItemStack stack, boolean shouldAdult, boolean disableSound) {
		this.name = name;
		this.delay = delay;
		this.animal = animal;
		this.stack = stack;
		this.shouldAdult = shouldAdult;
		this.disableSound = disableSound;
		resetCooldown();
	}

	@Override
	public void update() {
		cooldown--;

		if (canProduce()) {
			if (!shouldAdult || !animal.isChild()) {
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
				animal.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (animal.getRNG().nextFloat() - animal.getRNG().nextFloat()) * 0.2F + 1.0F);
			}
			animal.entityDropItem(stack.copy(), 0);
		}
	}

	@Override
	public NBTBase writeNBT() {
		return new NBTTagInt(cooldown);
	}

	@Override
	public void readNBT(NBTBase nbt) {
		cooldown = ((NBTTagInt) nbt).getInt();
	}

	@Override
	public String getName() {
		return name;
	}

	public static Function<EntityAnimal, IProduction> parse(JsonElement jsonEle) {
		JsonObject jsonObj = jsonEle.getAsJsonObject();

		String name = JsonUtils.getString(jsonObj, "name");
		IRange delay;
		JsonElement jsonDelay = jsonObj.get("delay");
		if (jsonDelay.isJsonObject()) {
			JsonObject jsonObjDelay = jsonDelay.getAsJsonObject();
			int min = JsonUtils.getInt(jsonObjDelay, "min");
			int max = JsonUtils.getInt(jsonObjDelay, "max");
			delay = new RangeRandom(min, max);
		} else {
			delay = new RangeConstant(jsonDelay.getAsInt());
		}
		ItemStack stack = CraftingHelper.getItemStack(JsonUtils.getJsonObject(jsonObj, "output"), new JsonContext(References.MODID));
		boolean shouldAdult = JsonUtils.getBoolean(jsonObj, "should_adult");
		boolean disableSound = JsonUtils.getBoolean(jsonObj, "disable_sound");

		return (animal) -> new ProductionEgg(name, animal, delay, stack, shouldAdult, disableSound);
	}

	public int getCooldown() {
		return cooldown;
	}

	@Override
	public String getMessage() {
		if (cooldown < 0) {
			return String.format("%s now", I18n.format(name));
		}
		return String.format("%s after %d seconds", I18n.format(name), cooldown);
	}
	
}

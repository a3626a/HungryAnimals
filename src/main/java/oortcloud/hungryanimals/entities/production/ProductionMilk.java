package oortcloud.hungryanimals.entities.production;

import java.util.function.Function;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.production.condition.Conditions;
import oortcloud.hungryanimals.entities.production.utils.IRange;
import oortcloud.hungryanimals.entities.production.utils.RangeConstant;
import oortcloud.hungryanimals.entities.production.utils.RangeRandom;

public class ProductionMilk extends ProductionInteraction {

	private ItemStack emptyBucket;
	private ItemStack filledBucket;
	private Predicate<EntityAgeable> condition;
	private boolean disableSound;

	public ProductionMilk(String name, EntityAnimal animal, IRange delay, ItemStack emptyBucket, ItemStack filledBucket, Predicate<EntityAgeable> condition,
			boolean disableSound) {
		super(name, animal, delay);
		this.emptyBucket = emptyBucket;
		this.filledBucket = filledBucket;
		this.condition = condition;
		this.disableSound = disableSound;
	}

	@Override
	public EnumActionResult interact(EntityInteract event, EnumHand hand, ItemStack itemstack) {
		EntityPlayer player = event.getEntityPlayer();
		if (canProduce()) {
			if (condition.apply(animal)) {
				if (itemstack.isItemEqual(emptyBucket)) {
					if (!animal.getEntityWorld().isRemote) {
						if (!disableSound) {
							player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
						}
						itemstack.shrink(1);

						if (itemstack.isEmpty()) {
							player.setHeldItem(hand, filledBucket.copy());
						} else if (!player.inventory.addItemStackToInventory(filledBucket.copy())) {
							player.dropItem(filledBucket.copy(), false);
						}
					}
					resetCooldown();
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.PASS;
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
		Predicate<EntityAgeable> condition = Conditions.parse(JsonUtils.getJsonObject(jsonObj, "condition"));
		boolean disableSound = JsonUtils.getBoolean(jsonObj, "disable_sound");
		ItemStack input = CraftingHelper.getItemStack(JsonUtils.getJsonObject(jsonObj, "input"), new JsonContext(References.MODID));
		ItemStack output = CraftingHelper.getItemStack(JsonUtils.getJsonObject(jsonObj, "output"), new JsonContext(References.MODID));

		return (animal) -> new ProductionMilk(name, animal, delay, input, output, condition, disableSound);
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

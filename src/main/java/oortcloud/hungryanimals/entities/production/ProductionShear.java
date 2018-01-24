package oortcloud.hungryanimals.entities.production;

import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
import oortcloud.hungryanimals.entities.production.utils.IRange;
import oortcloud.hungryanimals.entities.production.utils.RangeConstant;
import oortcloud.hungryanimals.entities.production.utils.RangeRandom;

/**
 * This Production is different from IShearable
 * @author LeeChangHwan
 *
 */
public class ProductionShear extends ProductionInteraction {

	private ItemStack input;
	private ItemStack output;
	private int damage;
	private boolean shouldAdult;
	private boolean disableSound;

	public ProductionShear(String name, EntityAnimal animal, IRange delay, ItemStack tool, ItemStack wool, int damage, boolean shouldAdult,
			boolean disableSound) {
		super(name, animal, delay);
		this.input = tool;
		this.output = wool;
		this.damage = damage;
		this.shouldAdult = shouldAdult;
		this.disableSound = disableSound;
	}

	@Override
	public EnumActionResult interact(EntityInteract event, EnumHand hand, ItemStack itemstack) {
		EntityPlayer player = event.getEntityPlayer();
		if (canProduce()) {
			if (!shouldAdult || !animal.isChild()) {
				if (itemstack.isItemEqual(input)) {
					animal.entityDropItem(output.copy(), 1.0F);
					itemstack.damageItem(damage, player);
					if (!disableSound) {
						animal.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
					}
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
		int damage = JsonUtils.getInt(jsonObj, "damage");
		boolean shouldAdult = JsonUtils.getBoolean(jsonObj, "should_adult");
		boolean disableSound = JsonUtils.getBoolean(jsonObj, "disable_sound");
		ItemStack input = CraftingHelper.getItemStack(JsonUtils.getJsonObject(jsonObj, "input"), new JsonContext(References.MODID));
		ItemStack output = CraftingHelper.getItemStack(JsonUtils.getJsonObject(jsonObj, "output"), new JsonContext(References.MODID));

		return (animal) -> new ProductionShear(name, animal, delay, input, output, damage, shouldAdult, disableSound);
	}
}


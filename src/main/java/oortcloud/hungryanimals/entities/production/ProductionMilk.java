package oortcloud.hungryanimals.entities.production;

import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import oortcloud.hungryanimals.core.lib.References;

public class ProductionMilk implements IProductionInteraction, IProductionTickable {

	private int cooldown;
	private int delay;
	private ItemStack emptyBucket;
	private ItemStack filledBucket;
	protected EntityAnimal animal;

	String name;
	
	public ProductionMilk(String name, EntityAnimal animal, int delay, ItemStack emptyBucket, ItemStack filledBucket) {
		this.name = name;
		this.animal = animal;
		this.delay = delay;
		this.emptyBucket = emptyBucket;
		this.filledBucket = filledBucket;
	}

	@Override
	public EnumActionResult interact(EntityInteract event, EnumHand hand, ItemStack itemstack) {
		EntityPlayer player = event.getEntityPlayer();

		if (canProduce()) {
			if (itemstack.isItemEqual(emptyBucket)) {
				player.setHeldItem(hand, filledBucket.copy());
				resetCooldown();
			}
		}
		return EnumActionResult.PASS;
	}

	@Override
	public void update() {
		cooldown--;
	}

	private boolean canProduce() {
		return cooldown <= 0;
	}

	private void resetCooldown() {
		cooldown = delay;
	}

	@Override
	public NBTBase writeNBT() {
		return new NBTTagInt(cooldown);
	}

	@Override
	public void readNBT(NBTBase nbt) {
		cooldown = ((NBTTagInt)nbt).getInt();
	}

	@Override
	public String getName() {
		return name;
	}
	
	public static Function<EntityAnimal, IProduction> parse(JsonElement jsonEle) {
		JsonObject jsonObj = jsonEle.getAsJsonObject();
		
		String name = JsonUtils.getString(jsonObj, "name");
		int delay = JsonUtils.getInt(jsonObj, "delay");
		ItemStack input = CraftingHelper.getItemStack(JsonUtils.getJsonObject(jsonObj, "input"), new JsonContext(References.MODID));
		ItemStack output = CraftingHelper.getItemStack(JsonUtils.getJsonObject(jsonObj, "output"), new JsonContext(References.MODID));
		
		return (animal) -> new ProductionMilk(name, animal, delay, input, output);
	}
	
}

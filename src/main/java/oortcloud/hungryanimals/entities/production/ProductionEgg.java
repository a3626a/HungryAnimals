package oortcloud.hungryanimals.entities.production;

import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import oortcloud.hungryanimals.core.lib.References;

public class ProductionEgg implements IProductionTickable {

	private int cooldown;
	private int delay;
	private ItemStack stack;
	protected EntityAnimal animal;
	
	private String name;
	
	public ProductionEgg(String name, EntityAnimal animal, int delay, ItemStack stack) {
		this.name = name;
		this.delay = delay;
		this.animal = animal;
		this.stack = stack;
	}
	
	@Override
	public void update() {
		cooldown--;
		
		if (canProduce()) {
			produce();
			resetCooldown();
		}
	}
	
	private boolean canProduce() {
		return cooldown <= 0;
	}

	private void resetCooldown() {
		cooldown = delay;
	}
	
	private void produce() {
		animal.entityDropItem(stack.copy(), 0);
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
		ItemStack stack = CraftingHelper.getItemStack(JsonUtils.getJsonObject(jsonObj, "output"), new JsonContext(References.MODID));
		
		return (animal) -> new ProductionEgg(name, animal, delay, stack);
	}
	
}

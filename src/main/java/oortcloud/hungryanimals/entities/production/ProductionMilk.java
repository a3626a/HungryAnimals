package oortcloud.hungryanimals.entities.production;

import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.JsonUtils;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.network.PacketEntityClient;
import oortcloud.hungryanimals.core.network.SyncIndex;

public class ProductionMilk implements IProductionInteraction, IProductionTickable, ISyncable {

	private int cooldown;
	private int delay;
	private ItemStack emptyBucket;
	private ItemStack filledBucket;
	protected EntityAnimal animal;

	private boolean prevCanProduce;
	
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
				player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
				itemstack.shrink(1);

				if (itemstack.isEmpty()) {
					player.setHeldItem(hand, filledBucket.copy());
				} else if (!player.inventory.addItemStackToInventory(filledBucket.copy())) {
					player.dropItem(filledBucket.copy(), false);
				}

				resetCooldown();
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.PASS;
	}

	@Override
	public void update() {
		cooldown--;
		boolean currCanProduce = canProduce();
		if (currCanProduce != prevCanProduce) {
			sync();
		}
		prevCanProduce = currCanProduce;
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
		cooldown = ((NBTTagInt) nbt).getInt();
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

	public void sync() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			WorldServer world = (WorldServer) animal.getEntityWorld();
			for (EntityPlayer i : world.getEntityTracker().getTrackingPlayers(animal)) {
				PacketEntityClient packet = new PacketEntityClient(SyncIndex.PRODUCTION_SYNC, animal);
				packet.setString(getName());
				packet.setInt(cooldown);
				HungryAnimals.simpleChannel.sendTo(packet, (EntityPlayerMP) i);
			}
		}
	}

	public void syncTo(EntityPlayerMP target) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			PacketEntityClient packet = new PacketEntityClient(SyncIndex.PRODUCTION_SYNC, animal);
			packet.setString(getName());
			packet.setInt(cooldown);
			HungryAnimals.simpleChannel.sendTo(packet, target);
		}
	}

	@Override
	public void readFrom(PacketEntityClient message) {
		cooldown = message.getInt();
	}
	
}

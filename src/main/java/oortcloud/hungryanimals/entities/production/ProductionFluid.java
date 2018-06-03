package oortcloud.hungryanimals.entities.production;

import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.JsonUtils;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.PacketEntityClient;
import oortcloud.hungryanimals.core.network.SyncIndex;
import oortcloud.hungryanimals.entities.production.condition.Conditions;

public class ProductionFluid implements IProductionInteraction, IProductionTickable, ISyncable, IProductionTOP {

	private String name;
	private EntityAnimal animal;
	private FluidTank tank;
	private FluidStack fluid;
	private Predicate<EntityAgeable> condition;

	private int prevAmount;

	public ProductionFluid(String name, EntityAnimal animal, Predicate<EntityAgeable> condition, FluidStack fluid) {
		this.name = name;
		this.animal = animal;
		tank = new FluidTank(1000);
		this.fluid = fluid;
		this.condition = condition;
	}

	@Override
	public NBTBase writeNBT() {
		return tank.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void readNBT(NBTBase nbt) {
		tank.readFromNBT((NBTTagCompound) nbt);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getMessage() {
		if (condition.apply(animal)) {
			return String.format("%s : %d / %d", I18n.format(name), tank.getFluidAmount(), tank.getCapacity());
		} else {
			return null;
		}
	}

	public void sync() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			WorldServer world = (WorldServer) animal.getEntityWorld();
			for (EntityPlayer i : world.getEntityTracker().getTrackingPlayers(animal)) {
				PacketEntityClient packet = new PacketEntityClient(SyncIndex.PRODUCTION_SYNC, animal);
				packet.setString(getName());
				packet.setTag(tank.writeToNBT(new NBTTagCompound()));
				HungryAnimals.simpleChannel.sendTo(packet, (EntityPlayerMP) i);
			}
		}
	}

	public void syncTo(EntityPlayerMP target) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			PacketEntityClient packet = new PacketEntityClient(SyncIndex.PRODUCTION_SYNC, animal);
			packet.setString(getName());
			packet.setTag(tank.writeToNBT(new NBTTagCompound()));
			HungryAnimals.simpleChannel.sendTo(packet, target);
		}
	}

	@Override
	public void readFrom(PacketEntityClient message) {
		tank.readFromNBT(message.getTag());
	}

	@Override
	public void update() {
		if (condition.apply(animal)) {
			tank.fill(fluid, true);

			int currAmount = tank.getFluidAmount();
			if (prevAmount != currAmount) {
				sync();
				prevAmount = currAmount;
			}
		}
	}

	@Override
	public EnumActionResult interact(EntityInteract event, EnumHand hand, @Nonnull ItemStack itemstack) {
		EntityPlayer player = event.getEntityPlayer();

		ItemStack heldItem = player.getHeldItem(hand);
		if (!heldItem.isEmpty()) {
			IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if (playerInventory != null) {
				FluidActionResult fluidActionResult = FluidUtil.tryFillContainerAndStow(heldItem, tank, playerInventory, Integer.MAX_VALUE, player);
				if (fluidActionResult.isSuccess()) {
					player.setHeldItem(hand, fluidActionResult.getResult());
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.PASS;

	}

	public static Function<EntityAnimal, IProduction> parse(JsonElement jsonEle) {
		JsonObject jsonObj = jsonEle.getAsJsonObject();

		String name = JsonUtils.getString(jsonObj, "name");
		Predicate<EntityAgeable> condition = Conditions.parse(JsonUtils.getJsonObject(jsonObj, "condition"));
		Fluid fluid = FluidRegistry.getFluid(JsonUtils.getString(jsonObj, "fluid"));
		int amount = JsonUtils.getInt(jsonObj, "amount");

		return (animal) -> new ProductionFluid(name, animal, condition, new FluidStack(fluid, amount));
	}

}

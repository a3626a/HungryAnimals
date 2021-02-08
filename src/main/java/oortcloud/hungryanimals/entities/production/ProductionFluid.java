package oortcloud.hungryanimals.entities.production;

import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
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
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.api.jei.production.RecipeCategoryProductionFluid;
import oortcloud.hungryanimals.core.network.PacketClientSyncProducing;
import oortcloud.hungryanimals.core.network.PacketClientSyncProducingFluid;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.production.condition.Conditions;

public class ProductionFluid
		implements IProductionInteraction, IProductionTickable, ISyncable, IProductionTOP {

	private String name;
	private MobEntity animal;
	@Nonnull
	private FluidTank tank;
	private Fluid fluid;
	private double amount;
	private double weight;
	private Predicate<MobEntity> condition;

	private int prevAmount;

	public ProductionFluid(String name, MobEntity animal, Predicate<MobEntity> condition, Fluid fluid,
			int capacity, double amount, double weight) {
		this.name = name;
		this.animal = animal;
		tank = new FluidTank(capacity);
		this.fluid = fluid;
		this.amount = amount;
		this.weight = weight;
		this.condition = condition;
	}

	@Override
	public INBT writeNBT() {
		return tank.writeToNBT(new CompoundNBT());
	}

	@Override
	public void readNBT(INBT nbt) {
		tank.readFromNBT((CompoundNBT) nbt);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getMessage() {
		if (condition.apply(animal)) {
			return String.format("%s : %d / %d", name, tank.getFluidAmount(), tank.getCapacity());
		} else {
			return null;
		}
	}

	public void sync() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			WorldServer world = (WorldServer) animal.getEntityWorld();
			for (PlayerEntity i : world.getEntityTracker().getTrackingPlayers(animal)) {
				PacketClientSyncProducingFluid packet = new PacketClientSyncProducingFluid(animal, getName(), tank);
				HungryAnimals.simpleChannel.sendTo(packet, (ServerPlayerEntity) i);
			}
		}
	}

	public void syncTo(ServerPlayerEntity target) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			PacketClientSyncProducingFluid packet = new PacketClientSyncProducingFluid(animal, getName(), tank);
			HungryAnimals.simpleChannel.sendTo(packet, target);
		}
	}

	@Override
	public void readFrom(PacketClientSyncProducing message) {
		tank = ((PacketClientSyncProducingFluid) message).tank;
	}

	@Override
	public void update() {
		if (condition.apply(animal)) {
			IAttributeInstance fluid_amount = animal.getEntityAttribute(ModAttributes.fluid_amount);
			if (fluid_amount != null) {
				int inserted = tank.fill(new FluidStack(fluid, (int) (fluid_amount.getAttributeValue() * amount)),
						true);

				ICapabilityHungryAnimal capHungry = animal.getCapability(ProviderHungryAnimal.CAP, null);
				if (capHungry != null) {
					IAttributeInstance fluid_weight = animal.getEntityAttribute(ModAttributes.fluid_weight);
					if (fluid_weight != null) {
						capHungry.addWeight(-inserted * fluid_weight.getAttributeValue() * weight / 1000.0);
					} else {
						HungryAnimals.logger.warn("{} doesn't have attribute {}. Weight consumption is skipped", animal,
								ModAttributes.fluid_weight);
					}
				}
			} else {
				HungryAnimals.logger.warn("{} doesn't have attribute {}. Fluid production canceled", animal,
						ModAttributes.fluid_amount);
			}

			int currAmount = tank.getFluidAmount();
			if (prevAmount != currAmount) {
				sync();
				prevAmount = currAmount;
			}
		}
	}

	@Override
	public EnumActionResult interact(EntityInteract event, EnumHand hand, @Nonnull ItemStack itemstack) {
		PlayerEntity player = event.getPlayerEntity();

		ItemStack heldItem = player.getHeldItem(hand);
		if (!heldItem.isEmpty()) {
			IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if (playerInventory != null) {
				FluidActionResult fluidActionResult = FluidUtil.tryFillContainerAndStow(heldItem, tank, playerInventory,
						Integer.MAX_VALUE, player, true);
				if (fluidActionResult.isSuccess()) {
					player.setHeldItem(hand, fluidActionResult.getResult());
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.PASS;

	}

	public IFluidHandler getFluidHandler() {
		return tank;
	}

	public static Function<MobEntity, IProduction> parse(JsonElement jsonEle) {
		JsonObject jsonObj = jsonEle.getAsJsonObject();

		String name = JsonUtils.getString(jsonObj, "name");
		Predicate<MobEntity> condition = Conditions.parse(JsonUtils.getJsonObject(jsonObj, "condition"));
		Fluid fluid = FluidRegistry.getFluid(JsonUtils.getString(jsonObj, "fluid"));
		int capacity = JsonUtils.getInt(jsonObj, "capacity");
		double amount = JsonUtils.getFloat(jsonObj, "amount");
		double weight = JsonUtils.getFloat(jsonObj, "weight");

		return new ProductionFactory() {
			@Override
			public IProduction apply(MobEntity animal) {
				return new ProductionFluid(name, animal, condition, fluid, capacity, amount, weight);
			}

			@Override
			public void getIngredients(IJeiHelpers jeiHelpers, IIngredients ingredients) {
				ingredients.setOutput(FluidStack.class, new FluidStack(fluid, 1000));
			}
			
			@Override
			public String getCategoryUid() {
				return RecipeCategoryProductionFluid.UID;
			}
		};
	}

}

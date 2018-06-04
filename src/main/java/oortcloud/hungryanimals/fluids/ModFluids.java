package oortcloud.hungryanimals.fluids;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;

////////////////////////////////
////CHOONSTER-MINECRAFT-MODS////
////////////////////////////////

public class ModFluids {

	public static final Set<Fluid> FLUIDS = new HashSet<>();
	public static final Set<IFluidBlock> MOD_FLUID_BLOCKS = new HashSet<>();
	
	public static final Fluid milk = 
			createFluid(Strings.fluidMilkName, true, true,
					fluid -> fluid.setLuminosity(10).setDensity(800).setViscosity(1500),
					fluid -> new BlockFluidFinite(fluid, new MaterialLiquid(MapColor.WHITE_STAINED_HARDENED_CLAY)));

	public static void init() {
	}

	private static <T extends Block & IFluidBlock> Fluid createFluid(String name, boolean hasFlowIcon, boolean hasBlock, Consumer<Fluid> fluidPropertyApplier,
			Function<Fluid, T> blockFactory) {
		final String texturePrefix = "blocks/";

		final ResourceLocation still = new ResourceLocation(References.MODID, texturePrefix + name + "_still");
		final ResourceLocation flowing = hasFlowIcon ? new ResourceLocation(References.MODID,texturePrefix + name + "_flow") : still;

		Fluid fluid = new Fluid(name, still, flowing);
		final boolean useOwnFluid = FluidRegistry.registerFluid(fluid);

		if (useOwnFluid) {
			fluidPropertyApplier.accept(fluid);
			if (hasBlock) {
				T fluidBlock = blockFactory.apply(fluid);
				MOD_FLUID_BLOCKS.add(fluidBlock);
			}
		} else {
			fluid = FluidRegistry.getFluid(name);
		}

		FLUIDS.add(fluid);
		
		return fluid;
	}
	
	@Mod.EventBusSubscriber
	public static class RegistrationHandler {

		/**
		 * Register this mod's fluid {@link Block}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();

			for (final IFluidBlock fluidBlock : MOD_FLUID_BLOCKS) {
				final Block block = (Block) fluidBlock;
				block.setRegistryName(References.MODID, "fluid." + fluidBlock.getFluid().getName());
				block.setUnlocalizedName(References.MODID+"."+fluidBlock.getFluid().getUnlocalizedName());
				block.setCreativeTab(HungryAnimals.tabHungryAnimals);
				registry.register(block);
			}
		}
	}
}

////////////////////////////////
////CHOONSTER-MINECRAFT-MODS////
////////////////////////////////

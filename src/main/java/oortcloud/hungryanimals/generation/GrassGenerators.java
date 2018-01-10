package oortcloud.hungryanimals.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber
public class GrassGenerators {

	private static GrassGenerators INSTACNE;

	private Map<Integer, List<GrassGenerator>> generators;
	private List<GrassGenerator> defaults;

	public GrassGenerators() {
		generators = new HashMap<Integer, List<GrassGenerator>>();
		defaults = new ArrayList<GrassGenerator>();
	}

	public static GrassGenerators getInstance() {
		if (INSTACNE == null) {
			INSTACNE = new GrassGenerators();
		}
		return INSTACNE;
	}

	public boolean register(@Nullable Biome biome, GrassGenerator generator) {
		if (biome == null) {
			return defaults.add(generator);
		} else {
			int id = Biome.getIdForBiome(biome);
			if (!generators.containsKey(id)) {
				generators.put(id, new ArrayList<GrassGenerator>());
			}
			return generators.get(id).add(generator);
		}
	}
	
	@SubscribeEvent
	public static void onRandomTick(WorldTickEvent event) {
		if (event.side == Side.CLIENT)
			return;

		if (event.phase == Phase.END) {
			WorldServer world = (WorldServer) event.world;
			if (world.getWorldTime() % 200 == 0) {
				ArrayList<BlockPos> newPoses = new ArrayList<BlockPos>();
				ArrayList<IBlockState> newStates = new ArrayList<IBlockState>();
				if (!world.isRemote) {
					for (Chunk chunk : ((ChunkProviderServer) world.getChunkProvider()).getLoadedChunks()) {
						int x = chunk.x << 4;
						int z = chunk.z << 4;
						int Randx = world.rand.nextInt(16);
						int Randz = world.rand.nextInt(16);
						int h = chunk.getHeightValue(Randx, Randz);
						BlockPos pos = new BlockPos(x + Randx, h, z + Randz);
						Biome biome = world.getBiome(pos);
						int biomeID = Biome.getIdForBiome(biome);
						
						List<GrassGenerator> target;
						if (getInstance().generators.containsKey(biomeID)) {
							target = getInstance().generators.get(biomeID);
						} else {
							target = getInstance().defaults;
						}
						
						for (GrassGenerator i : target) {
							if (i.condition.canGrassGrow(world, pos)) {
								newPoses.add(pos);
								newStates.add(i.states.get(world.rand.nextInt(i.states.size())));
								break;
							}
						}
					}
					for (int i = 0; i < newPoses.size(); i++) {
						BlockPos newPos = newPoses.get(i);
						IBlockState newState = newStates.get(i);
						world.setBlockState(newPos, newState, 3);
					}
				}
			}
		}
	}

}

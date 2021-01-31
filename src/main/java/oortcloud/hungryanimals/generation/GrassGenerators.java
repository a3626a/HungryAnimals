package oortcloud.hungryanimals.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraftforge.common.BiomeDictionary;
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

	private List<BiomeDictionaryEntry> dictionaryGenerators;
	private Map<Integer, GrassGenerator> biomeGenerators;
	private GrassGenerator defaultGenerator;

	public static class BiomeDictionaryEntry {
		public List<BiomeDictionary.Type> types;
		public GrassGenerator generator;

		public BiomeDictionaryEntry(List<BiomeDictionary.Type> types, GrassGenerator generator) {
			this.types = types;
			this.generator = generator;
		}
	}

	public GrassGenerators() {
		dictionaryGenerators = new ArrayList<>();
		biomeGenerators = new HashMap<>();
	}

	public static GrassGenerators getInstance() {
		if (INSTACNE == null) {
			INSTACNE = new GrassGenerators();
		}
		return INSTACNE;
	}

	public boolean registerByTypeName(List<String> types, GrassGenerator generator) {
		List<BiomeDictionary.Type> typesDict;
		typesDict = types.stream().map((i)-> {
			// TODO prevent getType to create new biome type.
			return BiomeDictionary.Type.getType(i);
		}).collect(Collectors.toList());
		return registerByType(typesDict, generator);
	}
	
	public boolean registerByType(List<BiomeDictionary.Type> types, GrassGenerator generator) {
		return dictionaryGenerators.add(new BiomeDictionaryEntry(types, generator));
	}

	public GrassGenerator registerByBiome(@Nullable Biome biome, GrassGenerator generator) {
		if (biome == null) {
			GrassGenerator old = defaultGenerator;
			defaultGenerator = generator;
			return old;
		} else {
			int id = Biome.getIdForBiome(biome);
			return biomeGenerators.put(id, generator);
		}
	}

	private GrassGenerator getGrassGenerator(Biome biome) {
		if (biome == null) {
			return defaultGenerator;
		}
		
		// PERFORMANCE issue here?
		for (BiomeDictionaryEntry i : dictionaryGenerators) {
			if (BiomeDictionary.getTypes(biome).containsAll(i.types)) {
				return i.generator;
			}
		}
		
		int biomeID = Biome.getIdForBiome(biome);
		if (getInstance().biomeGenerators.containsKey(biomeID)) {
			return biomeGenerators.get(biomeID);
		} else {
			return defaultGenerator;
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
				ArrayList<BlockState> newStates = new ArrayList<BlockState>();
				if (!world.isRemote) {
					for (Chunk chunk : ((ChunkProviderServer) world.getChunkProvider()).getLoadedChunks()) {
						int x = chunk.x << 4;
						int z = chunk.z << 4;
						int Randx = world.rand.nextInt(16);
						int Randz = world.rand.nextInt(16);
						int h = chunk.getHeightValue(Randx, Randz);
						BlockPos pos = new BlockPos(x + Randx, h, z + Randz);
						Biome biome = world.getBiome(pos);
						GrassGenerator target = getInstance().getGrassGenerator(biome);
						if (target != null) {
							if (world.isAirBlock(pos) && target.condition.canGrassGrow(world, pos)) {
								newPoses.add(pos);
								newStates.add(target.states.get(world.rand.nextInt(target.states.size())));
							}
						}
					}
					for (int i = 0; i < newPoses.size(); i++) {
						BlockPos newPos = newPoses.get(i);
						BlockState newState = newStates.get(i);
						world.setBlockState(newPos, newState, 3);
					}
				}
			}
		}
	}

}

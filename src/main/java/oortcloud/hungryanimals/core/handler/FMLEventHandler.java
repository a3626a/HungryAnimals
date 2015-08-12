package oortcloud.hungryanimals.core.handler;

import java.util.ArrayList;

import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import oortcloud.hungryanimals.configuration.ConfigurationHandler;
import oortcloud.hungryanimals.core.lib.References;

public class FMLEventHandler {

	public static double grassProbability;
	public static final double default_grassProbability = 0.1;
	
	@SubscribeEvent
	public void onRandomTick(WorldTickEvent event) {
		if (event.phase == Phase.END) {
			WorldServer world = (WorldServer) event.world;
			if (world.getWorldTime() % 200 == 0) {
				ArrayList<BlockPos> list = new ArrayList<BlockPos>();
				if (!world.isRemote) {
					for (Object i : ((ChunkProviderServer) world
							.getChunkProvider()).func_152380_a()) {
						if (world.rand.nextDouble() < grassProbability) {
							Chunk chunk = (Chunk) i;
							int x = chunk.xPosition << 4;
							int z = chunk.zPosition << 4;
							int Randx = world.rand.nextInt(16);
							int Randz = world.rand.nextInt(16);
							int h = chunk.getHeight(Randx, Randz);
							BlockPos pos = new BlockPos(x,h,z);
							if (canGrassGrow(world, pos.add(Randx,0,Randz))) {
								list.add(pos.add(Randx,0,Randz));
							}
						}
					}
					for (BlockPos i : list) {
						if (world.isAirBlock(i))
							world.setBlockState(i, Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS), 2);
					}
				}
			}
		}
	}

	public boolean canGrassGrow(World world, BlockPos pos) {
		boolean flag1 = world.getBlockState(pos.down()) == Blocks.grass.getDefaultState()
				&& world.isAirBlock(pos);
		boolean flag2 = true;
		for (int i = -1 ; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (!world.getChunkProvider().chunkExists((pos.getX()+i)>>4, (pos.getZ()+j)>>4)) {
					flag2 = false;
					break;
				} else if (world.getBlockState(pos.add(i, 0, j)).getBlock() == Blocks.tallgrass) {
					flag2 = false;
				}
			}
		}
		
		return flag1&&flag2;
	}
	
}

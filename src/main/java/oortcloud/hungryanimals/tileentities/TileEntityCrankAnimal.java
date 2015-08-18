package oortcloud.hungryanimals.tileentities;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.energy.PowerNetwork;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;

public class TileEntityCrankAnimal extends TileEntityPowerTransporter {

	public static double powerProduction = 5;
	private static double powerCapacity = PowerNetwork.powerUnit * 10;
	private BlockPos primaryPos;

	private EntityAnimal leashedAnimal;

	private UUID leashedAnimalUUID;
	@SideOnly(Side.CLIENT)
	private int leashedAnimalID;

	public TileEntityCrankAnimal() {
		super();
		super.powerCapacity=TileEntityCrankAnimal.powerCapacity;
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			leashedAnimalID = -1;
		}
	}

	public void setLeashed(EntityPlayer player, World worldIn) {
		double d0 = 7.0D;
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();
		List list = worldIn.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0,
				(double) k + d0));
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			EntityAnimal entityliving = (EntityAnimal) iterator.next();

			if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == player) {

				ExtendedPropertiesHungryAnimal property = (ExtendedPropertiesHungryAnimal) entityliving.getExtendedProperties(Strings.extendedPropertiesKey);

				// if (property != null && property.taming >= 1)
				if (property != null) {
					leashedAnimal = entityliving;
					property.ai_crank.crankAnimal = this;
				}
			}
		}

	}

	public EntityAnimal getLeashedAnimal() {
		return leashedAnimal;
	}

	@Override
	public void update() {
		if (!isPrimary())
			return;

		super.update();
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			if (leashedAnimal != null) {
				ExtendedPropertiesHungryAnimal property = (ExtendedPropertiesHungryAnimal) leashedAnimal.getExtendedProperties(Strings.extendedPropertiesKey);
				if (property != null) {
					double angleDifference = property.ai_crank.getAngleDifference();
					this.getPowerNetwork().producePower(property.crank_production*(1-Math.abs(90-angleDifference)/90.0));
					property.subHunger(property.crank_food_consumption);
				}
				
			}
			if (leashedAnimal == null && leashedAnimalUUID != null) {
				for (Object i : worldObj.loadedEntityList) {
					if (((Entity) i).getUniqueID().equals(leashedAnimalUUID)) {
						EntityAnimal entity = (EntityAnimal) i;
						ExtendedPropertiesHungryAnimal property = (ExtendedPropertiesHungryAnimal) entity.getExtendedProperties(Strings.extendedPropertiesKey);
						if (property != null) {
							leashedAnimal = entity;
							property.ai_crank.crankAnimal = this;
						}
					}
				}
				leashedAnimalUUID = null;
			}
		}
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			if (leashedAnimal == null && leashedAnimalID != -1) {
				EntityAnimal entity = (EntityAnimal) worldObj.getEntityByID(leashedAnimalID);
				if (entity != null) {
					ExtendedPropertiesHungryAnimal property = (ExtendedPropertiesHungryAnimal) entity.getExtendedProperties(Strings.extendedPropertiesKey);
					if (property != null) {
						leashedAnimal = entity;
						property.ai_crank.crankAnimal = this;
					}
				}
			}
			leashedAnimalID = -1;
		}

	}

	public boolean isPrimary() {
		if (primaryPos == null)
			return false;
		return primaryPos.equals(pos);
	}

	public void setPrimaryPos(BlockPos pos) {
		primaryPos = pos;
	}

	public BlockPos getPrimaryPos() {
		return primaryPos;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setLong("primaryPos", primaryPos.toLong());
		if (leashedAnimal != null)
			compound.setString("leashedAnimalUUID", leashedAnimal.getUniqueID().toString());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		primaryPos = BlockPos.fromLong(compound.getLong("primaryPos"));
		if (compound.hasKey("leashedAnimalUUID"))
			leashedAnimalUUID = UUID.fromString(compound.getString("leashedAnimalUUID"));
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setLong("primaryPos", primaryPos.toLong());
		if (leashedAnimal != null)
			compound.setInteger("leashedAnimalID", leashedAnimal.getEntityId());
		return new S35PacketUpdateTileEntity(getPos(), getBlockMetadata(), compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound compound = pkt.getNbtCompound();
		primaryPos = BlockPos.fromLong(compound.getLong("primaryPos"));
		if (compound.hasKey("leashedAnimalID"))
			leashedAnimalID = compound.getInteger("leashedAnimalID");
	}

}

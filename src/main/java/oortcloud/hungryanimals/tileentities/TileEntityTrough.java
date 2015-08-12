package oortcloud.hungryanimals.tileentities;

import java.util.List;

import org.omg.PortableServer.IdUniquenessPolicy;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.core.network.PacketTileEntityServer;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;

public class TileEntityTrough extends TileEntity implements IUpdatePlayerListBox {

	public ItemStack stack;

	private static int period = 20 * 5;
	private static double radius = 8;

	@SideOnly(Side.CLIENT)
	public float[][] random;

	public TileEntityTrough() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			random = new float[16][2];
			for (int i = 0; i < 16; i++) {
				random[i][0] = (float) (0.05 * Math.random());
				random[i][1] = (float) (0.05 * Math.random());
			}
		}

	}

	@Override
	public void update() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER
				&& this.worldObj.getWorldTime() % this.period == 0
				&& this.stack != null) {
			List list = this.worldObj.getEntitiesWithinAABB(EntityAnimal.class,
					new AxisAlignedBB(this.pos.add(-radius, -radius, -radius),this.pos.add(radius+1,radius+1,radius+1)));
			for (Object i : list) {

				ExtendedPropertiesHungryAnimal property = (ExtendedPropertiesHungryAnimal) ((EntityAnimal) i)
						.getExtendedProperties(Strings.extendedPropertiesKey);

				if (property != null && property.taming >= 1
						&& property.canEatFood(this.stack)) {
					property.ai_moveToFoodbox.x = this.pos.getX();
					property.ai_moveToFoodbox.y = this.pos.getY();
					property.ai_moveToFoodbox.z = this.pos.getZ();
				}
			}
		}

	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (stack != null) {
			NBTTagCompound tag = new NBTTagCompound();
			stack.writeToNBT(tag);
			compound.setTag("foodbox", tag);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("foodbox")) {
			NBTTagCompound tag = (NBTTagCompound) compound.getTag("foodbox");
			stack = ItemStack.loadItemStackFromNBT(tag);
		}
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound compound = pkt.getNbtCompound();
		if (compound.hasKey("foodbox")) {
			NBTTagCompound tag = (NBTTagCompound) compound.getTag("foodbox");
			stack = ItemStack.loadItemStackFromNBT(tag);
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound compound = new NBTTagCompound();
		if (stack != null) {
			NBTTagCompound tag = new NBTTagCompound();
			stack.writeToNBT(tag);
			compound.setTag("foodbox", tag);
		}
		return new S35PacketUpdateTileEntity(getPos(), getBlockMetadata(),
				compound);
	}
	
}

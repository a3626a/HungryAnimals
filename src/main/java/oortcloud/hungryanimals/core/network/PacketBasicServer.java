package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketBasicServer implements IMessage{
	public ByteBuf data;
	public int index;
	
	public PacketBasicServer() {
		this(0);
	}

	public PacketBasicServer(int index) {
		data = Unpooled.buffer(256);
		this.setInt(index);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		data = Unpooled.buffer();
		while (buf.readableBytes()>0) {
			data.writeByte(buf.readByte());
		}
		this.index = data.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		while (data.readableBytes()>0) {
			buf.writeByte(data.readByte());
		}
	}
	
	public void setInt(int value) {
		data.writeInt(value);
	}
	
	public void setIntArray(int[] array) {
		this.setInt(array.length);
		for (int i : array) {
			this.setInt(i);
		}
	}
	
	public int getInt() {
		return data.readInt();
	}
	
	public int[] getIntArray(){
		int length = this.getInt();
		int[] ret = new int[length];
		for (int i = 0 ; i < length; i++) {
			ret[i] = this.getInt();
		}
		return ret;
	}
	
	
	public void setFloat(float value) {
		data.writeFloat(value);
	}
	
	public void setFloatArray(float[] array) {
		this.setInt(array.length);
		for (float i : array) {
			this.setFloat(i);
		}
	}
	
	public float getFloat() {
		return data.readFloat();
	}
	
	public float[] getFloatArray(){
		int length = this.getInt();
		float[] ret = new float[length];
		for (int i = 0 ; i < length; i++) {
			ret[i] = this.getFloat();
		}
		return ret;
	}
	
	
	public void setDouble(double value) {
		data.writeDouble(value);
	}
	
	public void setDoubleArray(double[] array) {
		this.setInt(array.length);
		for (double i : array) {
			this.setDouble(i);
		}
	}
	
	public double getDouble() {
		return data.readDouble();
	}
	
	public double[] getDoubleArray(){
		int length = this.getInt();
		double[] ret = new double[length];
		for (int i = 0 ; i < length; i++) {
			ret[i] = this.getDouble();
		}
		return ret;
	}
	
	public void setBoolean(boolean value) {
		data.writeByte((value?1:0));
	}

	public void setBooleanArray(boolean[] array) {
		this.setInt(array.length);
		for (boolean i : array) {
			this.setBoolean(i);
		}
	}
	
	public boolean getBoolean() {
		byte ret = data.readByte();
		return (ret==1?true:false);
	}
	
	public boolean[] getBooleanArray(){
		int length = this.getInt();
		boolean[] ret = new boolean[length];
		for (int i = 0 ; i < length; i++) {
			ret[i] = this.getBoolean();
		}
		return ret;
	}
	
	public void setString(String value) {
		char[] temp = ((String) value).toCharArray();
		this.setInt((Integer) temp.length);
		for (int i = 0; i < temp.length; i++) {
			data.writeChar(temp[i]);
		}
	}
	
	public void setStringArray(String[] array) {
		this.setInt(array.length);
		for (String i : array) {
			this.setString(i);
		}
	}
	
	public String getString() {
		int length = this.getInt();
		char[] ret = new char[length];
		for (int i = 0; i < length; i++) {
			ret[i] = data.readChar();
		}
		return new String(ret);
	}
	
	public String[] getStringArray(){
		int lengthString = this.getInt();
		String[] retString = new String[lengthString];
		
		for (int i = 0 ; i < lengthString; i++) {
			
			int length = this.getInt();
			char[] ret = new char[length];
			for (int j = 0; j < length; j++) {
				ret[j] = data.readChar();
			}
			
			retString[i] = new String(ret);
		}
		return retString;
	}
	
	public void setItemStack(ItemStack stack) {
		ByteBufUtils.writeItemStack(data, stack);
	}
	
	public void setItemStackArray(ItemStack[] stacks) {
		setInt(stacks.length);
		for (int i = 0 ; i < stacks.length; i++) {
			setItemStack(stacks[i]);
		}
	}
	
	public ItemStack getItemStack() {
		return ByteBufUtils.readItemStack(data);
	}
	
	public ItemStack[] getItemStackArray() {
		int length = getInt();
		ItemStack[] ret = new ItemStack[length];
		for (int i = 0; i < length; i++) {
			ret[i] = getItemStack();
		}
		return ret;
	}
}

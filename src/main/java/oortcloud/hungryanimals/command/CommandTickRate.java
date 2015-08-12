package oortcloud.hungryanimals.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;

public class CommandTickRate implements ICommand {

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

	@Override
	public String getName() {
		return "tick";
	}

	@Override
	public List getAliases() {
		
		ArrayList alias = new ArrayList<String>();
		alias.add("tick");
		alias.add("t");
		return alias;
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender sender) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args,
			BlockPos pos) {
		return null;
	}
	
	@Override
	public void execute(ICommandSender sender, String[] args)
			throws CommandException {
		/*
		if (arr.length == 1) {
			MinecraftServer.tick = Integer.parseInt(arr[0]);
			ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation(
					"Set Tick Rate to " + MinecraftServer.tick, new Object[0]);
			player.addChatMessage(chatcomponenttranslation1);
		}
		*/
	}

}

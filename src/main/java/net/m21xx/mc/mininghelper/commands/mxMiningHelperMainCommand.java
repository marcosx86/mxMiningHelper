package net.m21xx.mc.mininghelper.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.m21xx.mc.mininghelper.mxMiningHelper;

public class mxMiningHelperMainCommand implements CommandExecutor {

	private mxMiningHelper vInstance;
	
	public mxMiningHelperMainCommand(mxMiningHelper pInstance) {		
		vInstance = pInstance;
	}
	
	public boolean onCommand(CommandSender pSender, Command pCommand, String pLabel, String[] pArgs) {		
		Player lPlayer = null;
		
		if (pSender instanceof Player) {			
			lPlayer = (Player) pSender;
		}
		
		if (pArgs.length == 0) {			
			if (lPlayer == null) {
				pSender.sendMessage("This command is supposed to be used by a player.");
				return false;
			}
			
			pSender.sendMessage(ChatColor.RED + "Dummy MiningHelper command. Not implemented yet.");
			
			return true;
		}
		
		pSender.sendMessage(ChatColor.RED + "Dummy MiningHelper command. Not implemented yet.");
		
		return false;
	}

}

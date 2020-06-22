package fraktalsk.FSMP.Guilds.Commands;

import org.bukkit.command.CommandSender;

import fraktalsk.FSMP.Guilds.GuildCommand;
import fraktalsk.FSMP.Guilds.Main;
import net.md_5.bungee.api.ChatColor;

public class GuildCommand_Help extends GuildCommand_Core{
	
	public GuildCommand_Help(GuildCommand gc, CommandSender sender, String[] args) {
		
		//full init not needed
		main = gc.main;
		utils = gc.utils;
		
		int n = 0;
		if (args.length == 1) n = 1;
		else n = utils.isInt(args[1]);	
		if (n <= 0) n = 1;
		else if (n > 3) n = 3;
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7----&f&l&9 FSMP-Guilds &r&o" + Main.VERSION + "&r&7 " + Integer.toString(n) + "/3 ----"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Use /guild help <n> to go to page n\n"));
		sender.sendMessage(utils.helpSection(n));
	}

}

package fraktalx.FraktalGuilds.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import fraktalx.FraktalGuilds.Guild;
import fraktalx.FraktalGuilds.GuildCommand;
import fraktalx.FraktalGuilds.GuildError;

public class GuildCommand_List extends GuildCommand_Core {
	
	public GuildCommand_List (GuildCommand gc, CommandSender sender, String[] args) {
		init(sender, gc.main, gc.utils);
	
		if(main.guilds.size() == 0) {
			utils.errorMessage(sender, GuildError.NoGuildsExist);
		} else {
			for (Guild g : main.guilds) {
				String output = "";
				String q = (utils.isOnline(g.findLeaderID())) ? utils.UUIDtoPlayer(g.findLeaderID()).getName() : utils.getOffline(g.findLeaderID()).getName();
				output += ChatColor.translateAlternateColorCodes('&', g.getFormattedName() + "&r&l&8 (" + q + "'s)&r\n");						
				sender.sendMessage(output);
			}
		}
	}

}

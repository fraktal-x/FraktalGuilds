package fraktalx.FraktalGuilds.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fraktalx.FraktalGuilds.Guild;
import fraktalx.FraktalGuilds.GuildCommand;

public class GuildCommand_Debug extends GuildCommand_Core {
	
	public GuildCommand_Debug (GuildCommand gc, CommandSender sender, String[] args) {
		init(sender, gc.main, gc.utils);
		
		List<Player> players = utils.getAllPlayers();
		for(Player p : players) {
			String output = p.getName() + " ";
			Guild g = utils.getGuild(p);
			if(g != null) {
				output += g.getFormattedName() + " " + Integer.toString(g.playerList.get(p.getUniqueId().toString()));
			}
			sender.sendMessage(output);
		}
	}

}

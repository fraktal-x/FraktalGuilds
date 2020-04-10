package fraktalx.FraktalGuilds.Commands;

import org.bukkit.command.CommandSender;

import fraktalx.FraktalGuilds.GuildCommand;
import fraktalx.FraktalGuilds.GuildError;

public class GuildCommand_Disband extends GuildCommand_Core{
	
	public GuildCommand_Disband(GuildCommand gc, CommandSender sender, String[] args) {
		init(sender, gc.main, gc.utils);
		
		if (!isPlayer) {
			utils.errorMessage(sender, GuildError.NotPlayer);
		} else if (!inGuild) {
			utils.errorMessage(sender, GuildError.NotInGuild);
		} else if (permission != 2) {
			utils.errorMessage(sender, GuildError.NoPermission);
		} else {
			guild.selfDestruct();
			main.writeData();
		}
	}

}

package fraktalsk.FSMP.Guilds.Commands;

import org.bukkit.command.CommandSender;

import fraktalsk.FSMP.Guilds.GuildCommand;
import fraktalsk.FSMP.Guilds.GuildError;

public class GuildCommand_Leave extends GuildCommand_Core {
	
	public GuildCommand_Leave (GuildCommand gc, CommandSender sender, String[] args) {
		init(sender, gc.main, gc.utils);
		
		if (!isPlayer) {
			utils.errorMessage(sender, GuildError.NotPlayer);
		} else if (!inGuild) {
			utils.errorMessage(sender, GuildError.NotInGuild);
		} else if (permission == 2){
			utils.errorMessage(sender, GuildError.CannotLeave);
		} else {
			guild.removePlayer(player);
		}
	}

}

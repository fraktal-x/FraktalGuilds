package fraktalsk.FSMP.Guilds.Commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import fraktalsk.FSMP.Guilds.GuildCommand;
import fraktalsk.FSMP.Guilds.GuildError;

public class GuildCommand_Tell extends GuildCommand_Core {
	
	public GuildCommand_Tell (GuildCommand gc, CommandSender sender, String[] args) {
		init(sender, gc.main, gc.utils);
		
		if (!isPlayer) {
			utils.errorMessage(sender, GuildError.NotPlayer);
		} else if (!inGuild) {
			utils.errorMessage(sender, GuildError.NotInGuild);
		} else if (args.length < 2) {
			utils.errorMessage(sender, GuildError.IncorrectUsageTell);
		} else {
			guild.broadcastToAllMembers(guild.getTeamFormattedPlayerName(player) +": " + String.join(" ", Arrays.copyOfRange(args, 1, args.length)));						
		}
	}

}

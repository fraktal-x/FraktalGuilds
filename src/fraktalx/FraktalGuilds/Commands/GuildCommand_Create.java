package fraktalx.FraktalGuilds.Commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import fraktalx.FraktalGuilds.GuildCommand;
import fraktalx.FraktalGuilds.GuildError;

public class GuildCommand_Create extends GuildCommand_Core {
	
	public GuildCommand_Create (GuildCommand gc, CommandSender sender, String[] args) {
		
		init(sender, gc.main, gc.utils);
		
		if (args.length == 1) {
			utils.errorMessage(sender, GuildError.IncorrectUsageCreate);
		} else if (!isPlayer) {
			utils.errorMessage(sender, GuildError.NotPlayer);
		} else if (inGuild) {
			utils.errorMessage(sender, GuildError.AlreadyInGuild);
		} else if (main.guildNames.contains(String.join(" ", Arrays.copyOfRange(args, 1, args.length)))) {
			utils.errorMessage(sender, GuildError.GuildAlreadyExistsWithName);
		} else if (utils.getAllNames().contains(args[1].trim())) {
			utils.errorMessage(sender, GuildError.GuildCannotEqualPlayerName);
		} else {
			utils.createGuild(String.join(" ", Arrays.copyOfRange(args, 1, args.length)).trim(), player);
			utils.generateGuildNames();
			main.writeData();
		}
	}
}

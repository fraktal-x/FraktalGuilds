package fraktalsk.FSMP.Guilds.Commands;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import fraktalsk.FSMP.Guilds.GuildCommand;
import fraktalsk.FSMP.Guilds.GuildError;

public class GuildCommand_Rename extends GuildCommand_Core {
	
	public GuildCommand_Rename (GuildCommand gc, CommandSender sender, String[] args) {
		init(sender, gc.main, gc.utils);
		
		if (!isPlayer) {
			utils.errorMessage(sender, GuildError.NotPlayer);
		} else if (!inGuild) {
			utils.errorMessage(sender, GuildError.NotInGuild);
		} else if (args.length < 2) {
			utils.errorMessage(sender, GuildError.IncorrectUsageRename);
		} else if (permission != 2) {
			utils.errorMessage(sender, GuildError.NoPermission);
		} else if (main.guildNames.contains(String.join(" ", Arrays.copyOfRange(args, 1, args.length)))) {
			utils.errorMessage(sender, GuildError.GuildAlreadyExistsWithName);
		} else {
			String pName = player.getName();
			guild.guildName = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).trim();
			utils.generateGuildNames();
			guild.broadcastToAllMembers(ChatColor.translateAlternateColorCodes('&', "&l" + pName + "&r&7&o renamed the guild to &r" + guild.getFormattedName() + "&r&7&o."));
			main.writeData();
		}
	}

}

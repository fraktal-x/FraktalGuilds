package fraktalx.FraktalGuilds.Commands;

import org.bukkit.command.CommandSender;

import fraktalx.FraktalGuilds.GuildCommand;
import fraktalx.FraktalGuilds.GuildError;
import net.md_5.bungee.api.ChatColor;

public class GuildCommand_Colour extends GuildCommand_Core {
	
	public GuildCommand_Colour(GuildCommand gc, CommandSender sender, String[] args) {
		
		init(sender, gc.main, gc.utils);
		
		if (!isPlayer) {
			utils.errorMessage(sender, GuildError.NotPlayer);
		} else if (!inGuild) {
			utils.errorMessage(sender, GuildError.NotInGuild);
		} else if (args.length < 1) {
			utils.errorMessage(sender, GuildError.IncorrectUsageColour);
		} else if (permission < 1) {
			utils.errorMessage(sender, GuildError.NoPermission);
		} else {
			int colour = utils.isInt(args[1]);
			if (colour < 0 || colour > 15) {
				utils.errorMessage(sender, GuildError.IncorrectUsageColour);													
			} else {
				guild.setGuildColor(utils.chatColours[Integer.parseInt(args[1].trim())]);
				guild.broadcastToAllMembers(ChatColor.translateAlternateColorCodes('&', guild.colours[guild.getPermission(player)] + player.getName() + " &r&7&orecoloured the guild to " + guild.getFormattedName()));
				main.writeData();
			}
		}
	}
}

package fraktalx.FraktalGuilds.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fraktalx.FraktalGuilds.GuildCommand;
import fraktalx.FraktalGuilds.GuildError;
import net.md_5.bungee.api.ChatColor;

public class GuildCommand_Resign extends GuildCommand_Core {
	
	public GuildCommand_Resign (GuildCommand gc, CommandSender sender, String[] args) {
		init(sender, gc.main, gc.utils);
		
		if(!isPlayer) {
			utils.errorMessage(sender, GuildError.NotPlayer);
		} else if (!inGuild) {
			utils.errorMessage(sender, GuildError.NotInGuild);
		} else if (permission != 2) {
			utils.errorMessage(sender, GuildError.NoPermission);
		} else if (guild.playerList.size() == 1) {
			guild.removePlayer(player);						
		} else if (args.length < 2) {
			utils.errorMessage(sender, GuildError.IncorrectUsageResign);
		} else {
			try {
				Player target = Bukkit.getPlayer(args[1].trim());
				if(!(guild.playerList.containsKey(target.getUniqueId().toString()))) {
					utils.errorMessage(sender, GuildError.TargetNotInGuild);
				} else if (player == target) {
					utils.errorMessage(sender, GuildError.CannotPerformActionOnSelf);
				} else {
					guild.setPermission(player, 1);
					guild.setPermission(target, 2);
					String pName = player.getName();
					String tName = target.getName();
					guild.broadcastToAllMembers(ChatColor.translateAlternateColorCodes('&', "&l" + pName + "&r&7&o resigned as &r" + guild.colours[2] + guild.levels[2] + "&r&7&o"));
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l" + tName + "&r&7&o became the " + guild.colours[2] + guild.levels[2] + "&r&7&o of &r" + guild.getFormattedName() + "&r&7&o."));
					main.writeData();
				}
			}
			catch(NullPointerException c) {
				utils.errorMessage(sender, GuildError.NoPlayerWithNameExists);
			}
		}
	}

}

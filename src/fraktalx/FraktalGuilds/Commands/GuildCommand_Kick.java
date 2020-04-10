package fraktalx.FraktalGuilds.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fraktalx.FraktalGuilds.GuildCommand;
import fraktalx.FraktalGuilds.GuildError;
import net.md_5.bungee.api.ChatColor;

public class GuildCommand_Kick extends GuildCommand_Core {
	
	public GuildCommand_Kick (GuildCommand gc, CommandSender sender, String[] args) {
		init(sender, gc.main, gc.utils);
		
		if (!isPlayer) {
			utils.errorMessage(sender, GuildError.NotPlayer); 	
		} else if (!inGuild) {
			utils.errorMessage(sender, GuildError.NotInGuild); 
		} else if (args.length < 2) {
			utils.errorMessage(sender, GuildError.IncorrectUsageKick);
		} else {
			try {
				Player target = Bukkit.getPlayer(args[1].trim());
				if (!(guild.playerList.containsKey(target.getUniqueId().toString()))) {
					utils.errorMessage(sender, GuildError.TargetNotInGuild);
				} else if(player == target) {
					utils.errorMessage(sender, GuildError.CannotPerformActionOnSelf);
				} else if (guild.getPermission(target) >= permission) {
					utils.errorMessage(sender, GuildError.NoPermission); 
				} else {
					String pName = player.getName();
					String tName = target.getName();
					guild.broadcastToAllMembers(ChatColor.translateAlternateColorCodes('&', "&l" + pName + "&r&7&o kicked &r&l"+ tName + "&r&7&o from the guild."));
					guild.removePlayer(target);
				}
			}
			catch(NullPointerException c) {
				utils.errorMessage(sender, GuildError.NoPlayerWithNameExists);
			}
		}
	}

}

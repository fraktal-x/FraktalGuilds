package fraktalx.FraktalGuilds.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fraktalx.FraktalGuilds.GuildCommand;
import fraktalx.FraktalGuilds.GuildError;
import net.md_5.bungee.api.ChatColor;

public class GuildCommand_Promote  extends GuildCommand_Core {
	
	public GuildCommand_Promote (GuildCommand gc, CommandSender sender, String[] args) {
		init(sender, gc.main, gc.utils);
		
		if (!isPlayer) {
			utils.errorMessage(sender, GuildError.NotPlayer);
		} else if (!inGuild) {
			utils.errorMessage(sender, GuildError.NotInGuild);
		} else if (args.length < 2) {
			utils.errorMessage(sender, GuildError.IncorrectUsagePromote);
		} else if (permission != 2) {
			utils.errorMessage(sender, GuildError.NoPermission);
		} else {
			try {
				Player target = Bukkit.getPlayer(args[1].trim());
				if (!(guild.playerList.containsKey(target.getUniqueId().toString()))) {
					utils.errorMessage(sender, GuildError.TargetNotInGuild);
				} else if(player == target) {
					utils.errorMessage(sender, GuildError.CannotPerformActionOnSelf);
				} else if (guild.getPermission(target) == 1) {
					utils.errorMessage(sender, GuildError.CannotPromote);
				} else {
					int newPermission = (guild.getPermission(target) + 1);
					String pName = player.getName();
					String tName = target.getName();
					guild.setPermission(target, newPermission); 
					guild.broadcastToAllMembers(ChatColor.translateAlternateColorCodes('&', "&l" + pName + "&r&7&o promoted &r&l"+ tName+ "&r&7&o to &r" + guild.colours[newPermission] + guild.levels[newPermission]) + "&r&7&o.");
					main.writeData();
				}
			}
			catch(NullPointerException c) {
				utils.errorMessage(sender, GuildError.NoPlayerWithNameExists);
			}					
		}
	}

}

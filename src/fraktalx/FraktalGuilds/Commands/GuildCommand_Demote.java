package fraktalx.FraktalGuilds.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fraktalx.FraktalGuilds.GuildCommand;
import fraktalx.FraktalGuilds.GuildError;

public class GuildCommand_Demote extends GuildCommand_Core{
	
	public GuildCommand_Demote(GuildCommand gc, CommandSender sender, String[] args) {
		
		init(sender, gc.main, gc.utils);
		
		if (!isPlayer) {
			utils.errorMessage(sender, GuildError.NotPlayer);
		} else if (!inGuild) {
			utils.errorMessage(sender, GuildError.NotInGuild);
		} else if (args.length < 2) {
			utils.errorMessage(sender, GuildError.IncorrectUsageDemote);
		} else if (permission != 2) {
			utils.errorMessage(sender, GuildError.NoPermission);
		} else {
			try {
				Player target = Bukkit.getPlayer(args[1].trim());
				if (!(guild.playerList.containsKey(target.getUniqueId().toString()))) {
					utils.errorMessage(sender, GuildError.TargetNotInGuild);
				} else if(player == target) {
					utils.errorMessage(sender, GuildError.CannotPerformActionOnSelf);
				} else if (guild.getPermission(target) == 0) {
					utils.errorMessage(sender, GuildError.CannotDemote);
				} else {
					int newPermission = (guild.getPermission(target) - 1);
					String pName = player.getName();
					String tName = target.getName();
					guild.setPermission(target, newPermission); 
					
					//success enum?
					guild.broadcastToAllMembers(ChatColor.translateAlternateColorCodes('&', "&l" + pName + "&r&7&o demoted &r&l"+ tName+ "&r&7&o to &r" + guild.colours[newPermission] + guild.levels[newPermission]) + "&r&7&o.");							
					main.writeData();
				}
			} 
			catch(NullPointerException c) {
				utils.errorMessage(sender, GuildError.NotInGuild);
			}						
		}
		
	}

}

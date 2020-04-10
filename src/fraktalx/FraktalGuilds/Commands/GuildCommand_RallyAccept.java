package fraktalx.FraktalGuilds.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import fraktalx.FraktalGuilds.GuildCommand;
import fraktalx.FraktalGuilds.GuildError;
import net.md_5.bungee.api.ChatColor;

public class GuildCommand_RallyAccept  extends GuildCommand_Core {
	
	public GuildCommand_RallyAccept (GuildCommand gc, CommandSender sender, String[] args) {
		init(sender, gc.main, gc.utils);
		
		if(!isPlayer) {
			utils.errorMessage(sender, GuildError.NotPlayer);
		} else if (!inGuild) {
			utils.errorMessage(sender, GuildError.NotInGuild);
		} else {
			try {
				if(utils.isOnline(args[1]) && guild.playerList.containsKey(args[1])) {
					Player target = Bukkit.getPlayer(args[3]);
					if (main.cooldowns.containsKey(target.getName())) {
						if(main.cooldowns.get(target.getName()) < System.currentTimeMillis()) {
							utils.errorMessage(sender, GuildError.RallyExpired);
						} else {
							//Bukkit.broadcastMessage(target.getWorld().getName());
							player.teleport(new Location(target.getLocation().getWorld(), utils.isInt(args[4]), utils.isInt(args[5]), utils.isInt(args[6])), PlayerTeleportEvent.TeleportCause.COMMAND);									
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&oYou have teleported to &r&l" + guild.colours[guild.getPermission(target)] + guild.levels[guild.getPermission(target)] +"&r&l " + target.getName() + "'s&r&7&o rally point."));
							target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l" + guild.colours[permission] + guild.levels[permission] + "&r&l " + player.getName() + "&r&7&o has teleported to your rally point."));					
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&oA fatal error has occurred [02]. Contact fraktalx."));
					}
				} else {
					utils.errorMessage(sender, GuildError.RallyOwnerNotFound);
				}
			}
			catch(NullPointerException c) {
				utils.errorMessage(sender, GuildError.NoPlayerWithNameExists);
			}
		}
	}

}

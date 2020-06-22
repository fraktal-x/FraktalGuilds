package fraktalsk.FSMP.Guilds.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fraktalsk.FSMP.Guilds.GuildCommand;
import fraktalsk.FSMP.Guilds.GuildError;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class GuildCommand_Rally extends GuildCommand_Core {
	
	public GuildCommand_Rally (GuildCommand gc, CommandSender sender, String[] args) {
		init(sender, gc.main, gc.utils);
		
		if(!isPlayer) {
			utils.errorMessage(sender, GuildError.NotPlayer);
		} else if (!inGuild) {
			utils.errorMessage(sender, GuildError.NotInGuild);
		} else if (permission < 1) {
			utils.errorMessage(sender, GuildError.NoPermission);
		} else if (main.cooldowns.containsKey(player.getName()) && main.cooldowns.get(player.getName()) > System.currentTimeMillis()) {
			utils.errorMessage(sender, GuildError.RallyCooldown);
		} else {						
			int x = player.getLocation().getBlockX();
			int y = player.getLocation().getBlockY();
			int z = player.getLocation().getBlockZ();
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&oYou created a rally point here. It will expire in 60 seconds."));
			TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&l" + guild.colours[permission] + player.getName() + "&r&7&o created a rally point.&r&l&f.\nClick here to teleport."));
			List<Player> players = guild.getAllOnlineMembersExcept(player);
			for (Player target : players) {
				message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild rallyaccept " + player.getUniqueId().toString() + " " +target.getName() + " " + player.getName() + " " + Integer.toString(x) + " " + Integer.toString(y) + " " + Integer.toString(z)));
				target.spigot().sendMessage(message);
			}
			main.cooldowns.put(player.getName(), (utils.rallyCooldown * 1000) + System.currentTimeMillis());
		}
	}

}

package fraktalsk.FSMP.Guilds.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fraktalsk.FSMP.Guilds.GuildCommand;
import fraktalsk.FSMP.Guilds.GuildError;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class GuildCommand_Invite extends GuildCommand_Core {
	
	public GuildCommand_Invite (GuildCommand gc, CommandSender sender, String[] args) {
		init(sender, gc.main, gc.utils);
		
		if (!isPlayer) {
			utils.errorMessage(sender, GuildError.NotPlayer);
		} else if (!inGuild) {
			utils.errorMessage(sender, GuildError.NotInGuild);
		} else if (args.length < 2) {
			utils.errorMessage(sender, GuildError.IncorrectUsageInvite);
		} else if (permission < 1) {
			utils.errorMessage(sender, GuildError.NoPermission);
		} else {
			try {
				Player target = Bukkit.getPlayer(args[1].trim());
				if (utils.getGuild(target) != null) {
					utils.errorMessage(sender, GuildError.TargetAlreadyInGuild);
				} else {
					String pName = player.getName();
					String tName = target.getName();
					guild.broadcastToAllMembers(ChatColor.translateAlternateColorCodes('&', "&l" + pName + "&r&7&o invited &r&l"+ tName + "&r&7&o to the guild."));
					TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&l" + player.getName() + "&r&7&o invited you to &r" + guild.getFormattedName() + "&r&l&f.\nClick here to join."));
					message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild inviteaccept " + guild.guildName));
					target.spigot().sendMessage(message);
					main.cooldowns.put(target.getName(), (utils.inviteCooldown * 1000) + System.currentTimeMillis());
				}							
			}
			catch(NullPointerException c) {
				utils.errorMessage(sender, GuildError.NoPlayerWithNameExists); 
			}						
		}		
	}

}

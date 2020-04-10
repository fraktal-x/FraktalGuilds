package fraktalx.FraktalGuilds.Commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import fraktalx.FraktalGuilds.Guild;
import fraktalx.FraktalGuilds.GuildCommand;
import fraktalx.FraktalGuilds.GuildError;
import net.md_5.bungee.api.ChatColor;

public class GuildCommand_InviteAccept  extends GuildCommand_Core {
	
	public GuildCommand_InviteAccept (GuildCommand gc, CommandSender sender, String[] args) {
		init(sender, gc.main, gc.utils);
		
		if (!isPlayer) {
			utils.errorMessage(sender, GuildError.NotPlayer);
		} else if (args.length < 2) {
			utils.errorMessage(sender, GuildError.IncorrectArgs);
		} else if (!main.cooldowns.containsKey(player.getName())) {
			utils.errorMessage(sender, GuildError.NoInviteFound);
		} else if (main.cooldowns.get(player.getName()) < System.currentTimeMillis()){
			utils.errorMessage(sender, GuildError.InviteExpired);
			main.cooldowns.remove(player.getName());
		} else {						
			try {
				main.cooldowns.remove(player.getName());
				Guild target = utils.getGuildFromName(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));						
				target.addPlayer(player, 0);
				main.writeData();
			}
			catch(NullPointerException c) {
				utils.errorMessage(sender, GuildError.GuildNoLongerExists);
			}
			catch(ClassCastException c) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&c&oA fatal error has occurred [01]. Please contact fraktalx."));
			}			
		}
	}

}

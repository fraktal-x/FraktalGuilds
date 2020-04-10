package fraktalx.FraktalGuilds.Commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import fraktalx.FraktalGuilds.Guild;
import fraktalx.FraktalGuilds.GuildCommand;
import fraktalx.FraktalGuilds.GuildError;
import net.md_5.bungee.api.ChatColor;

public class GuildCommand_Delete extends GuildCommand_Core{
	
	public GuildCommand_Delete(GuildCommand gc, CommandSender sender, String[] args) {
		
		init(sender, gc.main, gc.utils);
		
		if(!sender.hasPermission("guild.delete.use")) {
			utils.errorMessage(sender, GuildError.NoPermission);
		} else if (args.length < 1) {
			utils.errorMessage(sender, GuildError.IncorrectUsageDelete);
		} else {
			try {
				Guild g = utils.getGuildFromName(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
				g.selfDestruct();
				main.writeData();
				
				//success enum
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&oThe selected guild has been deleted."));
			}
			catch(NullPointerException c) {
				utils.errorMessage(sender, GuildError.NoGuildWithNameExists);
			}																	
		}
	}
}

package fraktalsk.FSMP.Guilds.Commands;

import java.util.Iterator;

import org.bukkit.command.CommandSender;

import fraktalsk.FSMP.Guilds.Guild;
import fraktalsk.FSMP.Guilds.GuildCommand;
import fraktalsk.FSMP.Guilds.GuildError;
import net.md_5.bungee.api.ChatColor;

public class GuildCommand_Wipe extends GuildCommand_Core {
	
	public GuildCommand_Wipe (GuildCommand gc, CommandSender sender, String[] args) {
		init(sender, gc.main, gc.utils);
		
		if(!sender.hasPermission("guild.wipe.use")) {
			utils.errorMessage(sender, GuildError.NoPermission);
		} else if (main.guilds.size() == 0){
			utils.errorMessage(sender, GuildError.NoGuildsExist);
		} else {				
			Iterator<Guild> itr = main.guilds.iterator();
			while (itr.hasNext()) {
				Guild g = itr.next();
				itr.remove();
				g.selfDestruct();
			}
			main.emptyConfig();
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&oAll guilds have been deleted."));
		}
	}

}

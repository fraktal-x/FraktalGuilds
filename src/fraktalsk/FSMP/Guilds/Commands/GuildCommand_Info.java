package fraktalsk.FSMP.Guilds.Commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fraktalsk.FSMP.Guilds.Guild;
import fraktalsk.FSMP.Guilds.GuildCommand;
import fraktalsk.FSMP.Guilds.GuildError;

public class GuildCommand_Info extends GuildCommand_Core{
	
	public GuildCommand_Info (GuildCommand gc, CommandSender sender, String[] args) {
		init(sender, gc.main, gc.utils);
		
		if(args.length < 2) {
			utils.errorMessage(sender, GuildError.IncorrectUsageInfo);
		} else {
			Guild g = utils.getGuildFromName(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
			if (g != null) {
				sender.sendMessage(utils.getInfo(null, g));
			} else {
				try {
					Player p = Bukkit.getPlayer(args[1].trim());
					g = utils.getGuild(p);
					sender.sendMessage(utils.getInfo(p, g));
				}
				catch(NullPointerException c) {
					utils.errorMessage(sender, GuildError.NoNameExists);;							
				}
			}												
		}
	}

}

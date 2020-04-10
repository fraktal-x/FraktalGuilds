package fraktalx.FraktalGuilds.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fraktalx.FraktalGuilds.Guild;
import fraktalx.FraktalGuilds.Main;
import fraktalx.FraktalGuilds.Utils;

public class GuildCommand_Core {
	public Main main;
	public Utils utils;
	
	boolean isPlayer = false;
	boolean inGuild = false;
	Player player = null;
	Guild guild = null;
	int permission = 0;
	
	public void init(CommandSender sender, Main _main, Utils _utils) {
		main = _main;
		utils = _utils;
		
		if(sender instanceof Player) {
			isPlayer = true;
			player = (Player) sender;
			guild = utils.getGuild(player);
			if(guild != null) {
				inGuild = true;
				permission = guild.getPermission(player);
			}				
		}
	}
}

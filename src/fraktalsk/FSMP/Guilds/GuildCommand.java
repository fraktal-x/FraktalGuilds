package fraktalsk.FSMP.Guilds;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fraktalsk.FSMP.Guilds.Commands.*;

public class GuildCommand implements CommandExecutor {

	public Main main;
	public Utils utils;	
	
	public GuildCommand(Main _main) {
		main = _main;
		utils = _main.utils;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String tag, String[] args) {
		if (tag.equalsIgnoreCase("guild")) {
			if (args.length == 0) 
			{
				utils.errorMessage(sender, GuildError.IncorrectArgs);
				return true;
			}			
							
			switch(args[0]) {
				case "color":
				case "colour":					
					new GuildCommand_Colour(this, sender, args);
					break;
										
				case "create":
					new GuildCommand_Create(this, sender, args);			
					break;
					
				case "delete":
					new GuildCommand_Delete(this, sender, args);
					break;
					
				case "demote":
					new GuildCommand_Demote(this, sender, args);
					break;
									
				case "disband":
					new GuildCommand_Disband(this, sender, args);
					break;
					
				case "help":
					new GuildCommand_Help(this, sender, args);
					break;
										
				case "info":
					new GuildCommand_Info(this, sender, args);
					break;
										
				case "invite":
					new GuildCommand_Invite(this, sender, args);		
					break;
										
				case "kick":
					new GuildCommand_Kick(this, sender, args);
					break;
								
				case "list":
					new GuildCommand_List(this, sender, args);
					break;
								
				case "leave":
					new GuildCommand_Leave(this, sender, args);
					break;
										
				case "promote":
					new GuildCommand_Promote(this, sender, args);
					break;
					
				case "rally":
					new GuildCommand_Rally(this, sender, args);
					break;
					
				case "rename":
					new GuildCommand_Rename(this, sender, args);
					break;
					
				case "resign":
					new GuildCommand_Resign(this, sender, args);
					break;
										
				case "tell":
					new GuildCommand_Tell(this, sender, args);
					break;
					
				case "wipe":
					new GuildCommand_Wipe(this, sender,args);
					break;
					
				case "inviteaccept":
					new GuildCommand_InviteAccept(this, sender, args);
					break;
					
				case "rallyaccept":
					new GuildCommand_RallyAccept(this, sender, args);
					break;				
				
				//for debug
				case "debug":
					if(Main.inDebugMode) {
						new GuildCommand_Debug(this, sender, args);
					}
					break;
				
				default:
					utils.errorMessage(sender, GuildError.IncorrectArgs);
			}
			return true;
		}
		return false;
	}
		
}

package fraktalx.FraktalGuilds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;


public class AutoTabComplete implements TabCompleter{
	
	List<String> arguments = new ArrayList<String>();

	public List<String> onTabComplete(CommandSender sender, Command cmd, String tag, String[] args){
		if(arguments.isEmpty()) {
			arguments.add("colour");
			arguments.add("create");
			arguments.add("demote");
			arguments.add("delete");
			arguments.add("disband");
			arguments.add("help");			
			arguments.add("info");
			arguments.add("invite");
			arguments.add("kick");
			arguments.add("list");
			arguments.add("leave");
			arguments.add("promote");
			arguments.add("resign");
			arguments.add("rally");	
			arguments.add("rename");		
			arguments.add("tell");						
		}
		
		List<String> result = new ArrayList<String>();
		if(args.length == 1) {
			for (String a : arguments) {
				if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
					result.add(a);
				}			
			}
			return result;
		}
		
		return null;
	}

}

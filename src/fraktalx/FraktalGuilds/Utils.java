 package fraktalx.FraktalGuilds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Utils {
	public ChatColor[] chatColours = new ChatColor[]{ChatColor.BLACK,
													 ChatColor.DARK_BLUE,
													 ChatColor.DARK_GREEN,
													 ChatColor.DARK_AQUA,
													 ChatColor.DARK_RED,
													 ChatColor.DARK_PURPLE,
													 ChatColor.GOLD,
													 ChatColor.GRAY,
													 ChatColor.DARK_GRAY,
													 ChatColor.BLUE,
													 ChatColor.GREEN,
													 ChatColor.AQUA,
													 ChatColor.RED,
													 ChatColor.LIGHT_PURPLE,
													 ChatColor.YELLOW,
													 ChatColor.WHITE};
	private String[] helpText = new String[] {
			"&l/guild colour <0-15>&r:&o Changes the text colour of the team&r",
			"&l/guild create <name>&r:&o Creates a guild and adds you to it&r",
			"&l/guild delete <name>&r:&o Kicks all members of a guild&r",
			"&l/guild demote <player>&r:&o Demotes admins to members&r",
			"&l/guild disband <player>&r:&o Disbands a guild&r",
			"&l/guild help <page>&r:&o Shows the help page&r",
			"&l/guild info <guild|player>&r:&o Gives info of a player or guild&r",
			"&l/guild invite <player>&r:&o Shows the invite page&r",
			"&l/guild kick <player>&r:&o Removes player from guild&r",																
			"&l/guild list&r:&o Lists all guilds and owners&r",
			"&l/guild leave&r:&o Leaves the current guild&r",
			"&l/guild promote <player>&r:&o Promotes members to admins&r",
			"&l/guild resign <player>&r:&o Makes the target player the new leader&r",
			"&l/guild rally &r:&o Creates a new rally point&r",
			"&l/guild rename <name>&r:&o Renames the guild to the new name&r",
			"&l/guild tell <message>&r:&o Sends a message to your guild&r"
	};
	
	public Main main;
	
	public int inviteCooldown = 60;
	public int rallyCooldown = 60;
	
	
	public Utils(Main _main) {
		main = _main;
	}
	
	
	public Guild getGuild(Player _player) {
		String uuid = _player.getUniqueId().toString();
		for(Guild guild : main.guilds) {
			if (guild.playerList.containsKey(uuid)) {
				return guild;
			}
		}
		return null;
	}
	
	
	public void createGuild(String _name, Player _player) {		
		Guild guild = new Guild(main, _name);
		guild.addPlayer(_player, 2);
		main.guilds.add(guild);
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l" + _player.getName() + "&r&7&o created &r" + guild.getFormattedName() + "&r&7&o."));
	}
	
	
	public void generateGuildNames() {
		main.guildNames.clear();
		for (Guild guild : main.guilds) {
			main.guildNames.add(guild.guildName);
		}
	}
	
	
	public Guild getGuildFromName(String s) {
		for(Guild guild : main.guilds) {
			if (guild.guildName.equalsIgnoreCase(s)) {
				return guild;
			}
		}
		return null;
	}	
	
	
	public Player UUIDtoPlayer(String s) {
		return Bukkit.getPlayer(UUID.fromString(s));
	}
		
	
	public List<Player> getAllPlayers(){
		List<Player> value = new ArrayList<Player>();
		for (Object o : Bukkit.getOnlinePlayers().toArray()) {
			value.add((Player) o);
		}
		return value;
	}
		
	
	public List<String> getAllUUIDs(){
		List<String> value = new ArrayList<String>(); 
		for (Player p : getAllPlayers()) {
			value.add(p.getUniqueId().toString());
		}
		return value;
	}
		
	
	public List<String> getAllNames(){
		List<String> value = new ArrayList<>();
		for (Player p : getAllPlayers()) {
			value.add(p.getName());
		}
		for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
			value.add((p.getName()));
		}
		return value;
	}


	public boolean isOnline(String s) {
		return getAllUUIDs().contains(s);
	}
	
		
	public OfflinePlayer getOffline(String s) {
		return Bukkit.getOfflinePlayer(UUID.fromString(s));
	}
	
		
	public int CCtoInt(ChatColor c) {
		int j = 0;
		for(int i = 0; i < chatColours.length; i++) {
			if (c == chatColours[i]) {
				j = i;
			}
		}
		return j;
	}
	
	
	public String getInfo(Player p, Guild g) {
		//if about guild and not player p == null, g != null
		//if about player with no guild p != null, g == null
		//if about player with guild p, g != null
		if (g == null) {
			return p.getName() + " - no guild";
		} else if (p == null) {
			boolean b = main.utils.isOnline(g.findLeaderID());
			String q = (b) ? "online" : "offline";
			String name = (b) ? Bukkit.getPlayer(UUID.fromString(g.findLeaderID())).getName() : main.utils.getOffline(g.findLeaderID()).getName();
			return g.getFormattedName() + 
					ChatColor.translateAlternateColorCodes('&', " - &r&l" + Integer.toString(g.playerList.size()) + " member(s), " + Integer.toString(g.getAllOnlineMembers().size())) + " online, " + "Leader: " + name + " (" + q +			
					")\n" + String.join(", ", g.getAllOnlineMemberNames()); 
		} else {

			return p.getName() + " " + g.getFormattedPlayerName(p);
		}
	}
	
	
	public String helpSection(int page) {
		switch (page) {
			case -1:
			case 0:
			case 1:
				return ChatColor.translateAlternateColorCodes('&', String.join("\n", Arrays.copyOfRange(helpText, 0, 6)));
			case 2:
				return ChatColor.translateAlternateColorCodes('&', String.join("\n", Arrays.copyOfRange(helpText, 6, 12)));
			case 3:
				return ChatColor.translateAlternateColorCodes('&', String.join("\n", Arrays.copyOfRange(helpText, 12, helpText.length)));
			default:
				return "";
		}		
	}
	
	
	

	
	public int isInt(String s) {
		try {
			int a = Integer.parseInt(s.trim());
			return a;
		}
		catch (NumberFormatException e) {
			return -1;
		}		
	}
		
	
	public void errorMessage(CommandSender sender, GuildError e) {
		String output = "";
		switch (e) {
			case NotPlayer: 
				output = "&c&oYou must be a player to execute that command.";
				break;
				
			case CannotPerformActionOnSelf: 
				output = "&c&oYou cannot perform that action to yourself.";
				break;
				
			case TargetNotInGuild: 
				output = "&c&oThat player is not in your guild!";
				break;
				
			case TargetAlreadyInGuild: 
				output = "&c&oThat player is already in a guild!";
				break;
				
			case CannotDemote: 
				Guild pg = getGuild((Player) sender);
				output = "&c&oYou cannot demote &r" + pg.colours[0] + pg.levels[0] + "s&r&c&o." ;
				break;
				
			case CannotPromote: 
				Guild dg = getGuild((Player) sender);
				output = "&c&oYou cannot promote &r" + dg.colours[1] + dg.levels[1] + "s&r&c&o. Resign first." ;
				break;
				
			case CannotLeave: 
				Guild lg = getGuild((Player) sender);
				output = lg.colours[2] + lg.levels[2] + "s&r&c&omust resign first." ;
				break;
								
			case NotInGuild:
				output = "&c&oYou are not in a guild!";
				break;
				
			case AlreadyInGuild:
				output = "&c&oYou are already in a guild!"; 
				break;
								
			case NoPermission:
				output = "&c&oYou do not have permission to do that.";
				break;
								
			case NoPlayerWithNameExists:
				output = "&c&oNo online player exists with that name.";
				break;
				
			case NoGuildWithNameExists:
				output = "&c&oNo guild exists with that name.";
				break;
				
			case NoNameExists:
				output = "&c&oThat player/guild does not exist, or is not online.";
				break;
				
			case NoGuildsExist:
				output = "&c&oNo guilds exist at the moment!";
				break;
			
			case GuildAlreadyExistsWithName:
				output = "&c&oA guild already exists with that name!";
				break;
				
			case GuildCannotEqualPlayerName:
				output = "&c&oGuilds cannot have the same name as players!";
				break;
				
			case RallyCooldown:
				String t = Long.toString(Math.round(Math.ceil((main.cooldowns.get(((Player) sender).getName()) - System.currentTimeMillis()) / 1000f)));
				output = "&c&oYou must wait " + t + " seconds before creating another rally point.";
				break;
				
			case RallyExpired:
				output = "&c&oThis rally point has expired.";
				break;
				
			case RallyOwnerNotFound:
				output = "&c&oThe owner of the rally point is no longer online or in the faction.";
				break;
				
			case NoInviteFound:
				output = "&c&oYou have not been invited to join a guild.";
				break;
				
			case InviteExpired:
				output = "&c&oThis invite has expired.";
				break;
				
			case GuildNoLongerExists:
				output = "&c&oThe guild you are trying to join no longer exists.";
				break;
								
			case IncorrectArgs:
				output = "&c&oIncorrect arguments for /guild. Please use /guild help.";
				break;
				
			case IncorrectUsageColour:
				output = "&c&ousage: /guild colour <0-15>";
				break;
				
			case IncorrectUsageCreate:
				output = "&c&oUsage: /guild create <name>";
				break;
				
			case IncorrectUsageDelete:
				output = "&c&oUsage: /guild delete <name>";
				break;
				
			case IncorrectUsageDemote:
				output = "&c&oUsage: /guild demote <player>";
				break;
				
			case IncorrectUsageInfo:
				output = "&c&oUsage: /guild info <guild|player>";
				break;
				
			case IncorrectUsageInvite:
				output = "&c&oUsage: /guild invite <player>";
				break;
				
			case IncorrectUsageKick:
				output = "&c&oUsage: /guild kick <player>";
				break;
				
			case IncorrectUsagePromote:
				output = "&c&oUsage: /guild promote <player>";
				break;
			
			case IncorrectUsageRename:
				output = "&c&oUsage: /guild rename <name>";
				break;
				
			case IncorrectUsageResign:
				output = "&c&oUsage: /guild resign <player>";
				break;
				
			case IncorrectUsageTell:
				output = "&c&ousage: /guild tell \"message\"";
				break;
				
			default:
				output += "&c&oIncorrect arguments for /guild. Please use /guild help.";

		}
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', output));
	}
	
}

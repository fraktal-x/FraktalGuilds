package fraktalx.FraktalGuilds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Guild {

	public String guildName;
	public ChatColor guildColour = ChatColor.WHITE;
	
	//public String leaderName;
	public Map<String, Integer> playerList = new HashMap<String, Integer>();
	//1 = member, 2 = admin, 3 = leader
	public Main main;
	public String[] levels = new String[] {"Member", "Admin", "Leader"};
	public ChatColor[] colours = new ChatColor[] {ChatColor.WHITE, ChatColor.RED, ChatColor.GOLD};
	
	
	//chat colours are:
	//aqua, black, blue, dark_aqua, dark_blue, dark_gray, dark_green, dark_purple, dark_red, gold, gray, green, light_purple, red, white, yellow
	//these are in an array in the Main class to prevent excess overhead, access them by index instead of a switch statement
	
	//constructor for guild
	public Guild(Main _main, String _name) {
		main = _main;
		guildName = _name;
	}
	
	//adds a player's UUID as String and permission to the guild
	public void addPlayer(Player player, int level) {
		playerList.put(player.getUniqueId().toString(), level);
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l" + player.getName() + "&r&7&o joined &r" + getFormattedName() + "&r&7&o."));
	}
	
	//removes a player from the guild
	//if the new guild size is 0, delete the guild
	public void removePlayer(Player player) {
		playerList.remove(player.getUniqueId().toString());
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l" + player.getName() + "&r&7&o left &r" + getFormattedName() + "&r&7&o."));
		
		if (playerList.size() == 0) {
			selfDestruct();
			main.guilds.remove(this);
			main.generateGuildNames();
		} 
	}
	
	
	public void setGuildColor(ChatColor colour) {
		this.guildColour = colour;
	}
	
	//returns the formatted name of the team
	public String getFormattedName() {
		return ChatColor.translateAlternateColorCodes('&', guildColour + "&l[" + guildName + "]&r");
	}
	
	//returns the formatted name of the player as [Team | Role]
	public String getFormattedPlayerName(Player player) {		
		ChatColor c = colours[getPermission(player) - 1];
		String l = levels[getPermission(player) - 1];
		return ChatColor.translateAlternateColorCodes('&', guildColour + "&l[" + guildName + "&r | " + c + l + guildColour + "&l]&r");
	}
	
	//returns the formatted name of the player as [Player | to Team]
	public String getTeamFormattedPlayerName(Player player) {
		ChatColor c = colours[getPermission(player) - 1];
		return ChatColor.translateAlternateColorCodes('&', "&l[" + c + player.getName() + "&r | to " + guildColour + guildName + "&l]&r");
	}
	
	//returns the formatted name of the player as [Team | Player]
	public String getChatFormattedPlayerName(Player player) {
		ChatColor c = colours[getPermission(player) - 1];
		String l = player.getName();
		return ChatColor.translateAlternateColorCodes('&', guildColour + "&l[" + guildName + "&r | " + c + l + guildColour + "&l]&r");
	}
	
	//adds the player to the list of players with permission <level>
	public void setPermission(Player player, int level) {
		playerList.put(player.getUniqueId().toString(), level);
	}
	
	//returns the current permission of the given player
	public int getPermission(Player player) {
		return playerList.get(player.getUniqueId().toString());
	}
	
	//get a list of all online members
	public void broadcastToAllMembers(String message) {
		List<Player> players = getAllOnlineMembers();
		for(Player player : players) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));			
		}	
	}
	
	public void broadcastToAllMembersExcept(String message, Player _player) {
		List<Player> players = getAllOnlineMembersExcept(_player);
		for(Player player : players) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));			
		}	
	}
	
	
	public List<Player> getAllMembers() {
		List<Player> value = new ArrayList<Player>();
		Object[] uuids = playerList.keySet().toArray();
		for(Object uuid : uuids) {
			value.add(main.UUIDtoPlayer(uuid.toString()));
		}
		return value;
	}
	
	//get every player in the guild
	//get a list of online players
	//for every item that is in both, add to list and return
	@SuppressWarnings("unchecked")
	public List<Player> getAllOnlineMembers(){
		List<Player> value = new ArrayList<>();
		List<Player> members = getAllMembers();
		List<Object> onlines = (List<Object>) Bukkit.getOnlinePlayers();
		for (Player player : members ) {
			if(onlines.contains(player)) {
				value.add(player);
			}
		}
		return value;
	}
	
	public List<Player> getAllOnlineMembersExcept(Player p){
		List<Player> value = getAllOnlineMembers();
		value.remove(p);
		return value;
	}
	
	//returns a formatted list of every online member
	public List<String> getAllOnlineMemberNames(){
		List<Player> players = getAllOnlineMembers();
		List<String> value = new ArrayList<String>();
		for(Player player : players) {
			value.add(ChatColor.RESET + "" + colours[getPermission(player) - 1] + player.getName());
		}
		return value;
	}
	
	//remove every player from the guild
	public void selfDestruct() {
		playerList.clear();
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', getFormattedName() + "&r&7&o was disbanded."));
		//main.readData();
	}
	
	//find the member with permission 3
	//if there is none, destroy the guild
	public String findLeaderID() {
		for (String entry : playerList.keySet()) {
			if (playerList.get(entry) == 3) {
				return entry;
			}
		}
		selfDestruct();
		return "";
	}
			
}

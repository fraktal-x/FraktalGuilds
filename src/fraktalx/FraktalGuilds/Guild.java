package fraktalx.FraktalGuilds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Guild {

	public String guildName;
	public ChatColor guildColour = ChatColor.WHITE;
	
	public Map<String, Integer> playerList = new HashMap<String, Integer>();
	//0 = member, 1 = admin, 2 = leader
	public Main main;
	public String[] levels = new String[] {"Member", "Admin", "Leader"};
	public ChatColor[] colours = new ChatColor[] {ChatColor.WHITE, ChatColor.RED, ChatColor.GOLD};
	
	public Guild(Main _main, String _name) {
		main = _main;
		guildName = _name;
	}
	
	public void addPlayer(Player player, int level) {
		playerList.put(player.getUniqueId().toString(), level);
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l" + player.getName() + "&r&7&o joined &r" + getFormattedName() + "&r&7&o."));
	}
	
	
	public void removePlayer(Player player) {
		playerList.remove(player.getUniqueId().toString());
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l" + player.getName() + "&r&7&o left &r" + getFormattedName() + "&r&7&o."));
		main.writeData();
		if (playerList.size() == 0) {
			selfDestruct();
		} 
	}
		
	public void setGuildColor(ChatColor colour) {
		this.guildColour = colour;
	}
	
	
	//[Guild]
	public String getFormattedName() {
		return ChatColor.translateAlternateColorCodes('&', guildColour + "&l[" + guildName + "]&r");
	}
	
	
	//returns the formatted name of the player as [Team | Role]
	public String getFormattedPlayerName(Player player) {		
		ChatColor c = colours[getPermission(player)];
		String l = levels[getPermission(player)];
		return ChatColor.translateAlternateColorCodes('&', guildColour + "&l[" + guildName + "&r | " + c + l + guildColour + "&l]&r");
	}
	
	
	//returns the formatted name of the player as [Player | to Team]
	public String getTeamFormattedPlayerName(Player player) {
		ChatColor c = colours[getPermission(player) ];
		return ChatColor.translateAlternateColorCodes('&', "&l[" + c + player.getName() + "&r | to " + guildColour + guildName + "&l]&r");
	}
	
	
	//returns the formatted name of the player as [Team | Player]
	public String getChatFormattedPlayerName(Player player) {
		ChatColor c = colours[getPermission(player)];
		String l = player.getName();
		return ChatColor.translateAlternateColorCodes('&', guildColour + "&l[" + guildName + "&r | " + c + l + guildColour + "&l]&r");
	}
	
	
	public void setPermission(Player player, int level) {
		playerList.put(player.getUniqueId().toString(), level);
	}
	
	
	public int getPermission(Player player) {
		return playerList.get(player.getUniqueId().toString());
	}
	
	
	//get every member
	public List<Player> getAllMembers() {
		List<Player> value = new ArrayList<Player>();
		List<Object> uuids = playerList.keySet().stream().collect(Collectors.toList());
		uuids.forEach((uuid) -> value.add(main.utils.UUIDtoPlayer(uuid.toString())));
		return value;
	}
	
	
	public List<Player> getAllOnlineMembers(){
		Set<Player> members = getAllMembers().stream().collect(Collectors.toSet());
		Set<Object> onlines = Bukkit.getOnlinePlayers().stream().collect(Collectors.toSet());
		members.retainAll(onlines);		
		return members.stream().collect(Collectors.toList());
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
		players.forEach((player) -> value.add(ChatColor.RESET + "" + colours[getPermission(player)] + player.getName()));
		return value;
	}
	
	
	public void broadcastToAllMembers(String message) {
		getAllOnlineMembers().forEach((player) -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
	}
	
	
	public void broadcastToAllMembersExcept(String message, Player _player) {
		getAllOnlineMembersExcept(_player).forEach((player) -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
	}
	
	
	//remove every player from the guild
	public void selfDestruct() {
		playerList.clear();
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', getFormattedName() + "&r&7&o was disbanded."));
		main.guilds.remove(this);
		main.utils.generateGuildNames();
		main.writeData();
	}
	

	public String findLeaderID() {
		for (String entry : playerList.keySet()) {
			if (playerList.get(entry) == 2) {
				return entry;
			}
		}
		selfDestruct();
		return "";
	}
	
	
			
}

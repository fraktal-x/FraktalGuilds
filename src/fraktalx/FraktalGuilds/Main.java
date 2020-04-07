package fraktalx.FraktalGuilds;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	
	//array for chat colours to make them easier to access quickly
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
	
	public List<Guild> guilds = new ArrayList<>();
	public List<String> guildNames = new ArrayList<>();
	public Map<String, Long> cooldowns = new HashMap<String, Long>();	
	public String VERSION = "v1.0";

	
	//when the server starts
	//set the executor of /guild commands
	//register this to the event listener
	//check if there are any online players and add them to the player map
	//check if the config files exist
	//broadcast that the plugin is active
	@Override
	public void onEnable() {
		this.getCommand("guild").setExecutor(new GuildCommand(this));
		this.getCommand("guild").setTabCompleter(new AutoTabComplete());
		Bukkit.getServer().getPluginManager().registerEvents(this, this);		
		if (this.getConfig().contains("data") && this.getConfig().contains("cl")) {
			this.readData();
		}		
		Bukkit.getServer().getConsoleSender().sendMessage((ChatColor.translateAlternateColorCodes('&', "&f&l&9FraktalGuilds &r&o" + VERSION + "&r activated!")));
	}
	
	//write the data to the config files
	//broadcast that the plugin is deactivated
	@Override
	public void onDisable() {
		writeData();
		Bukkit.getServer().getConsoleSender().sendMessage((ChatColor.translateAlternateColorCodes('&', "&f&l&9FraktalGuilds &r&o" + VERSION + "&r deactivated!")));
	}
	
	//when a player joins, add them to the hashmap of online players <-- hashmap not needed
	//write data to the config file?
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		//Player player = event.getPlayer();
		//players.put(player.getUniqueId().toString(), player);
		writeData();
	}
	
	//get the player
	//if the player is in a guild format their name and set it as prefix
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent c) {
		Player p = c.getPlayer();
		Guild g = getGuild(p);
		String prefix = "";
		if (g == null) {
			prefix = ChatColor.translateAlternateColorCodes('&', "&l[" + p.getName() + "]&r");
		} else {
			prefix = g.getChatFormattedPlayerName(p);
		}
		c.setFormat(prefix + ": %2$s");
	}
	
	//when a player leaves save the config data
	@EventHandler
	public void onPlayerExit(PlayerQuitEvent event) {
		writeData();
	}
	
	//modular functions
	
	//instantiate a new guild, passing in this as Main and the name
	//add the player that ran the command as permission 3 (leader)
	public void createGuild(String _name, Player _player) {		
		Guild guild = (new Guild(this, _name));
		guild.addPlayer(_player, 3);
		guilds.add(guild);
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l" + _player.getName() + "&r&7&o created &r" + guild.getFormattedName() + "&r&7&o."));
	}
	
	//empty the current list of guild names
	//update it with every guild currently in the guild list
	public void generateGuildNames() {
		guildNames.clear();
		for (Guild guild : guilds) {
			guildNames.add(guild.guildName);
		}
	}
	
	//guild locators
	
	//gets the guild of a certain player
	//if they are in no guild return null;
	public Guild getGuild(Player _player) {
		String uuid = _player.getUniqueId().toString();
		for(Guild guild : guilds) {
			if (guild.playerList.containsKey(uuid)) {
				return guild;
			}
		}
		return null;
	}
	
	//gets the guild by the guild name
	//if none exists return null;
	public Guild getGuildFromName(String s) {
		for(Guild guild : guilds) {
			if (guild.guildName.equalsIgnoreCase(s)) {
				return guild;
			}
		}
		return null;
	}
	
	//converts the string UUID to a player
	public Player UUIDtoPlayer(String s) {
		return Bukkit.getPlayer(UUID.fromString(s));
	}
	
	//gets every online player as a list
	public List<Player> getAllPlayers(){
		List<Player> value = new ArrayList<Player>();
		for (Object o : Bukkit.getOnlinePlayers().toArray()) {
			value.add((Player) o);
		}
		return value;
	}
	
	//gets every online player as a list
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

	
	//determines whether a given UUID is online or not
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
	
	//data handlers
	
	//write the data
	//set the data as empty first
	//for every guild in existence
	//set the key as the name and value as the hashmap<String, Integer> player list;
	//save the file
	public void writeData() {
		emptyConfig();
		for (Guild guild : guilds) {
			this.getConfig().set("data." + guild.guildName, guild.playerList);
			this.getConfig().set("cl." + guild.guildName, CCtoInt(guild.guildColour));
		}
		this.saveConfig();
	}
	
	//get the config section "data"
	//for every key (guild name)
	//get the memory section each key occupies
	//turn the keys within the section to an array - remember this has structure <K1, <K2, V>>
	//get the integer value that this second key is linked to
	//add this to the hashmap (K2, V)
	//create a new guild with the name (K1)
	//set the player list of the guild as the hashmap (K2, V)
	//add this new guild to the guild list
	//
	public void readData() {
		
		this.getConfig().getConfigurationSection("data").getKeys(false).forEach(key ->{
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			MemorySection mem = (MemorySection) this.getConfig().get("data." + key);
			Object[] keys = mem.getKeys(false).toArray();
			for(Object k : keys) {
				int i = this.getConfig().getInt("data." + key + "." + k);
				map.put((String)k, i);
			}
			Guild guild = new Guild(this, key);
			guild.playerList = map;
			guilds.add(guild);		
		});
		generateGuildNames();
		
		this.getConfig().getConfigurationSection("cl").getKeys(false).forEach(key ->{
			ChatColor c = chatColours[(int) this.getConfig().get("cl." + key)];
			getGuildFromName(key).setGuildColor(c);
		});
	}
	
	public void emptyConfig(){
		this.getConfig().set("data", null);
		this.getConfig().set("cl", null);
		this.saveConfig();
	}
	
	
}

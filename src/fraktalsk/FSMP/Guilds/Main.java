package fraktalsk.FSMP.Guilds;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	
	
	public static String VERSION = "V2.0";
	public static boolean inDebugMode = false;

	public Map<String, Long> cooldowns = new HashMap<String, Long>();
	public List<Guild> guilds = new ArrayList<>();
	public List<String> guildNames = new ArrayList<>();
	
	public Utils utils = new Utils(this);
	
	public ItemStack[] cl = new ItemStack[2];
	
	
	@Override
	public void onEnable() {
		this.getCommand("guild").setExecutor(new GuildCommand(this));
		this.getCommand("guild").setTabCompleter(new AutoTabComplete());
		Bukkit.getServer().getPluginManager().registerEvents(this, this);		
		if (this.getConfig().contains("data") && this.getConfig().contains("cl")) {
			this.readData();
		}		
		Bukkit.getLogger().info("FSMP-Guilds " + VERSION + " activated!");
	}
	
	
	@Override
	public void onDisable() {
		writeData();
		Bukkit.getLogger().info("FSMP-Guilds " + VERSION + " deactivated!");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClickEvent(InventoryClickEvent c) {
		if(c.getClickedInventory() == null) return;
		if(c.getClickedInventory().getType() != InventoryType.PLAYER) return;
			final ItemStack cursor = c.getCursor();	//will be banner
			ItemStack target = c.getCurrentItem();	//will be head
			if(c.getRawSlot() != 5) return;
			if(cursor == null) return;
			if(!cursor.getType().toString().endsWith("BANNER")) return;
			if(c.getClick().isLeftClick()) {
				if(cursor.getAmount() != 1) return;
			}
			if(c.getClick().isRightClick()) {
				if(target.getType() != Material.AIR) return;
				target = cursor.clone();
				target.setAmount(cursor.getAmount() - 1);
				cursor.setAmount(1);
			}
			
			final ItemStack newTarget = target;
			
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					c.setCursor(newTarget);
			        c.setCurrentItem(cursor);            		 
				}
			}, 1L);
	}		
	
	
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent c) {
		writeData();
	}
	
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent c) {
		Player p = c.getPlayer();
		Guild g = utils.getGuild(p);
		String prefix = "";
		if (g == null) {
			prefix = ChatColor.translateAlternateColorCodes('&', "&l[" + c.getPlayer().getName() + "]&r");
		} else {
			prefix = g.getChatFormattedPlayerName(p);
		}
		c.setFormat(prefix + ": %2$s");
	}
	
	
	@EventHandler
	public void onPlayerExit(PlayerQuitEvent c) {
		writeData();
	}
	
	//data handlers
	
	
	public void writeData() {
		emptyConfig();
		for (Guild guild : guilds) {
			this.getConfig().set("data." + guild.guildName, guild.playerList);
			this.getConfig().set("cl." + guild.guildName, utils.CCtoInt(guild.guildColour));
		}
		this.saveConfig();
	}
	
	
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
		utils.generateGuildNames();
		
		this.getConfig().getConfigurationSection("cl").getKeys(false).forEach(key ->{
			ChatColor c = utils.chatColours[(int) this.getConfig().get("cl." + key)];
			utils.getGuildFromName(key).setGuildColor(c);
		});
	}
	
	
	public void emptyConfig(){
		this.getConfig().set("data", null);
		this.getConfig().set("cl", null);
		this.saveConfig();
	}
	
}

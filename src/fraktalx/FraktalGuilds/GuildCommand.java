package fraktalx.FraktalGuilds;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class GuildCommand implements CommandExecutor {

	public Main main;
	
	private Player player = null;
	private Guild guild = null;
	private boolean inGuild = false;	
	private int permission = 0;
	private int inviteCooldown = 60;
	private int rallyCooldown = 60;
	
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
	//len 16
	
	private String[] errorText = new String[] {
			"&c&oYou must be a player to execute that command.",	
			"&c&oYou are not in a guild!",
			"&c&oYou do not have permission to do that.",
			"&c&oNo online player exists with that name.",			
			"&c&oIncorrect arguments for /guild. Please use /guild help."
	};
	
	public GuildCommand(Main _main) {
		main = _main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String tag, String[] args) {
		if (tag.equalsIgnoreCase("guild")) {
			if (args.length == 0) 
			{
				cmsg(sender, 4); //incorrect arguments
				return true;
			}
			
			boolean isPlayer = false;
			if(sender instanceof Player) {
				isPlayer = true;
				player = (Player) sender;
				guild = main.getGuild(player);
				if(guild != null) {
					inGuild = true;
					permission = guild.getPermission(player);
				}				
			}
			
							
			switch(args[0]) {
				case "color":
				case "colour":
					//this command can only be executed by players in a guild with a permission level of 2 or 3.
					//it takes one integer argument between 0 and 15 inclusive.
					if (!isPlayer) {
						cmsg(sender, 0); //not player
					} else if (!inGuild) {
						cmsg(sender, 1);
					} else if (args.length < 2) {
						sender.sendMessage(chatFormat("&c&ousage: /guild colour <0-15>") );
					} else if (permission < 2) {
						cmsg(sender, 2); //no permission
					} else {
						int colour = isInt(args[1]);
						if (colour < 0 || colour > 15) {
							sender.sendMessage(chatFormat("&c&ousage: /guild colour <0-15>") );													
						} else {
							guild.setGuildColor(main.chatColours[Integer.parseInt(args[1].trim())]);
							guild.broadcastToAllMembers(chatFormat(guild.colours[guild.getPermission(player) - 1] + player.getName() + " &r&7&orecoloured the guild to " + guild.getFormattedName()));
							main.writeData();
						}
					}
					break;
					
					
				case "create":
					//creates a new guild and adds current player as leader
					if (args.length == 1) {
						sender.sendMessage(chatFormat("&c&oUsage: /guild create <name>."));
					} else if (!isPlayer) {
						cmsg(sender, 0); //not player
					} else if (guild != null) {
						player.sendMessage(chatFormat("&c&oYou are already in a guild!"));
					} else if (main.guildNames.contains(String.join(" ", Arrays.copyOfRange(args, 1, args.length)))) {
						player.sendMessage(chatFormat("&c&oA guild already exists with that name."));
					} else if (main.getAllNames().contains(args[1].trim())) {
						player.sendMessage(chatFormat("&c&oYou cannot name a guild the same name as a player."));
					} else {
						main.createGuild(String.join(" ", Arrays.copyOfRange(args, 1, args.length)).trim(), player);
						main.generateGuildNames();
						main.writeData();
					}				
					break;
					
					
				case "delete":
					//deletes target guild
					if(!sender.hasPermission("guild.delete.use")) {
						cmsg(sender, 2); //no permission
					} else if (args.length < 2) {
						sender.sendMessage(chatFormat("&c&oUsage: /guild delete <guild>"));
					} else {
						try {
							Guild g = main.getGuildFromName(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
							g.selfDestruct();
							main.guilds.remove(g);
							main.generateGuildNames();
							main.writeData();
							sender.sendMessage(chatFormat("&c&oThe selected guild has been deleted."));
						}
						catch(NullPointerException c) {
							player.sendMessage(chatFormat("&c&oNo guild exists with that name."));
						}
																	
					}
					break;
					
					
				case "disband":
					//allows leader to disband guild
					if (!isPlayer) {
						cmsg(sender, 0);
					} else if (!inGuild) {
						cmsg(sender, 1);
					} else if (permission != 3) {
						cmsg(sender, 2);
					} else {
						guild.selfDestruct();
						main.guilds.remove(guild);
						main.generateGuildNames();
						main.writeData();
					}
					break;
					
					
				case "demote":
					//converts admin to member
					if (!isPlayer) {
						cmsg(sender, 0); //not player
					} else if (!inGuild) {
						cmsg(sender, 1); //no guild
					} else if (args.length < 2) {
						sender.sendMessage(chatFormat("&c&ousage: /guild demote <player>"));
					} else if (permission != 3) {
						cmsg(sender, 2); //no permission
					} else {
						try {
							Player target = Bukkit.getPlayer(args[1].trim());
							if (!(guild.playerList.containsKey(target.getUniqueId().toString()))) {
								sender.sendMessage(chatFormat("&c&oThat player is not in your guild"));
							} else if(player == target) {
								sender.sendMessage(chatFormat("&c&oYou cannot demote yourself."));
							} else if (guild.getPermission(target) == 1) {
								sender.sendMessage(chatFormat("&c&oYou cannot demote &r" + guild.colours[0] + guild.levels[0] + "s&r&c&o."));
							} else {
								int newPermission = (guild.getPermission(target) - 1);
								String pName = player.getName();
								String tName = target.getName();
								guild.setPermission(target, newPermission); 
								guild.broadcastToAllMembers(chatFormat("&l" + pName + "&r&7&o demoted &r&l"+ tName+ "&r&7&o to &r" + guild.colours[newPermission - 1] + guild.levels[newPermission - 1]) + "&r&7&o.");							
								main.writeData();
							}
						} 
						catch(NullPointerException c) {
							cmsg(sender, 3); //no guild
						}						
					}
					break;
					
			
				case "help":
					//this command can be executed by anyone.
					//it has 1 optional argument, which defaults to 1 if it is empty.
					//any invalid argument should also default to 1
					int n = 0;
					if (args.length == 1) n = 1;
					else n = isInt(args[1]);	
					if (n <= 0) n = 1;
					else if (n > 3) n = 3;
					
					sender.sendMessage(chatFormat("&7----&f&l&9 FraktalGuilds &r&o" + main.VERSION + "&r&7 " + Integer.toString(n) + "/3 ----"));
					sender.sendMessage(chatFormat("&8Use /guild help <n> to go to page n\n"));
					sender.sendMessage(helpSection(n));
					break;
					
					
				case "info":
					//displays info about target player/guild
					if(args.length < 2) {
						sender.sendMessage(chatFormat("&c&oUsage: /guild info <guild/player>"));
					} else {
						Guild g = main.getGuildFromName(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
						if (g != null) {
							sender.sendMessage(getInfo(null, g));
						} else {
							try {
								Player p = Bukkit.getPlayer(args[1].trim());
								g = main.getGuild(p);
								sender.sendMessage(getInfo(p, g));
							}
							catch(NullPointerException c) {
								sender.sendMessage(chatFormat("&c&oThat player/guild does not exist, or is not online."));							
							}
						}												
					}
					break;
					
					
				case "invite":
					//sends a clickable message to join the guild.
					//60 second time limit
					if (!isPlayer) {
						cmsg(sender, 0); //not player
					} else if (!inGuild) {
						cmsg(sender, 1); //no guild
					} else if (args.length < 2) {
						sender.sendMessage(chatFormat("&c&ousage: /guild invite <player>"));
					} else if (permission < 2) {
						cmsg(sender, 2); //no permission
					} else {
						try {
							Player target = Bukkit.getPlayer(args[1].trim());
							if (main.getGuild(target) != null) {
								sender.sendMessage(chatFormat("&c&oThat player is already in a guild!"));
							} else {
								String pName = player.getName();
								String tName = target.getName();
								guild.broadcastToAllMembers(chatFormat("&l" + pName + "&r&7&o invited &r&l"+ tName + "&r&7&o to the guild."));
								TextComponent message = new TextComponent(chatFormat("&l" + player.getName() + "&r&7&o invited you to &r" + guild.getFormattedName() + "&r&l&f.\nClick here to join."));
								message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild accept-invite-hash " + guild.guildName));
								target.spigot().sendMessage(message);
								main.cooldowns.put(target.getName(), (inviteCooldown * 1000) + System.currentTimeMillis());
							}							
						}
						catch(NullPointerException c) {
							cmsg(sender, 3); //no player
						}						
					}					
					break;
					
					
				case "kick":
					//can only be executed by a player in a guild with a higher permission rank than the person they are kicking
					//removes target from guild
					if (!isPlayer) {
						cmsg(sender, 0); //not player	
					} else if (!inGuild) {
						cmsg(sender, 1); //no guild
					} else if (args.length < 2) {
						sender.sendMessage(chatFormat("&c&ousage: /guild kick <player>"));
					} else {
						try {
							Player target = Bukkit.getPlayer(args[1].trim());
							if (!(guild.playerList.containsKey(target.getUniqueId().toString().toString()))) {
								sender.sendMessage(chatFormat("&c&oThat player is not in your guild"));
							} else if(player == target) {
								sender.sendMessage(chatFormat("&c&oYou cannot kick yourself."));
							} else if (guild.getPermission(target) >= permission) {
								cmsg(sender, 2); //no guild
							} else {
								String pName = player.getName();
								String tName = target.getName();
								guild.broadcastToAllMembers(chatFormat("&l" + pName + "&r&7&o kicked &r&l"+ tName + "&r&7&o from the guild."));
								guild.removePlayer(target);
								main.writeData();
							}
						}
						catch(NullPointerException c) {
							cmsg(sender, 3); //no player
						}
					}
					break;
				
				
				case "list":
					//lists every guild that exists, unless there are none.
					if(main.guilds.size() == 0) {
						sender.sendMessage(chatFormat("&c&oNo guilds exist yet!"));
					} else {
						for (Guild g : main.guilds) {
							String output = "";
							String q = (main.isOnline(g.findLeaderID())) ? main.UUIDtoPlayer(g.findLeaderID()).getName() : main.getOffline(g.findLeaderID()).getName();
							output += g.getFormattedName() + ChatColor.ITALIC + " (" + q + "'s)\n";						
							sender.sendMessage(output);
						}
					}
					break;
				
				
				case "leave":
					//remove the player from the guild
					if (!isPlayer) {
						cmsg(sender, 0); //not player
					} else if (!inGuild) {
						sender.sendMessage(chatFormat("&c&oYou are not in a guild!"));
					} else if (permission == 3){
						sender.sendMessage(chatFormat(guild.colours[2] + guild.levels[2] + "s&r&c&o must resign first."));
					} else {
						guild.removePlayer(player);
						main.generateGuildNames();
						main.writeData();
					}
					break;
					
					
				case "promote":
					//increase permission level of member to admin
					if (!isPlayer) {
						cmsg(sender, 0); //no player
					} else if (!inGuild) {
						cmsg(sender, 1); //no guild
					} else if (args.length < 2) {
						sender.sendMessage(chatFormat("&c&ousage: /guild promote <player>"));
					} else if (permission < 2) {
						cmsg(sender, 2); // no permission
					} else {
						try {
							Player target = Bukkit.getPlayer(args[1].trim());
							if (!(guild.playerList.containsKey(target.getUniqueId().toString()))) {
								sender.sendMessage(chatFormat("&c&oThat player is not in your guild."));
							} else if(player == target) {
								sender.sendMessage(chatFormat("&c&oYou cannot promote yourself."));
							} else if (guild.getPermission(target) == 2) {
								sender.sendMessage(chatFormat("&c&oYou cannot promote &r" + guild.colours[1] + guild.levels[1] + "s&c&o to &r" + guild.colours[2] + guild.levels[2] + "s&c&o."));
							} else {
								int newPermission = (guild.getPermission(target) + 1);
								String pName = player.getName();
								String tName = target.getName();
								guild.setPermission(target, newPermission); 
								guild.broadcastToAllMembers(chatFormat("&l" + pName + "&r&7&o promoted &r&l"+ tName+ "&r&7&o to &r" + guild.colours[newPermission - 1] + guild.levels[newPermission - 1]) + "&r&7&o.");
								main.writeData();
							}
						}
						catch(NullPointerException c) {
							cmsg(sender, 3); //no player
						}					
					}
					break;
					
				case "rally":
					if(!isPlayer) {
						cmsg(sender, 0);
					} else if (!inGuild) {
						cmsg(sender, 1);
					} else if (permission < 2) {
						cmsg(sender, 2);
					} else if (main.cooldowns.containsKey(player.getName()) && main.cooldowns.get(player.getName()) > System.currentTimeMillis()) {
						sender.sendMessage(chatFormat("&c&oYou must wait " + Long.toString(Math.round(Math.ceil((main.cooldowns.get(player.getName()) - System.currentTimeMillis()) / 1000f))) + " seconds before creating another rally point." ));
					} else {						
						int x = player.getLocation().getBlockX();
						int y = player.getLocation().getBlockY();
						int z = player.getLocation().getBlockZ();
						player.sendMessage(chatFormat("&7&oYou created a rally point here. It will expire in 60 seconds."));
						TextComponent message = new TextComponent(chatFormat("&l" + guild.colours[permission - 1] + player.getName() + "&r&7&o created a rally point.&r&l&f.\nClick here to teleport."));
						List<Player> players = guild.getAllOnlineMembersExcept(player);
						for (Player target : players) {
							message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild teleport-hash " + player.getUniqueId().toString() + " " +target.getName() + " " + player.getName() + " " + Integer.toString(x) + " " + Integer.toString(y) + " " + Integer.toString(z)));
							target.spigot().sendMessage(message);
						}
						main.cooldowns.put(player.getName(), (rallyCooldown * 1000) + System.currentTimeMillis());
					}
					break;
					
				case "teleport-hash":
					if(!isPlayer) {
						cmsg(sender, 0);
					} else if (!inGuild) {
						cmsg(sender, 1);
					} else {
						try {
							if(main.isOnline(args[1]) && guild.playerList.containsKey(args[1])) {
								Player target = Bukkit.getPlayer(args[3]);
								if (main.cooldowns.containsKey(target.getName())) {
									if(main.cooldowns.get(target.getName()) < System.currentTimeMillis()) {
										sender.sendMessage(chatFormat("&c&oThis rally point has expired."));
									} else {
										Bukkit.broadcastMessage(target.getWorld().getName());
										player.teleport(target.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);									
										sender.sendMessage(chatFormat("&7&oYou have teleported to &r&l" + guild.colours[guild.getPermission(target) - 1] + guild.levels[guild.getPermission(target) - 1] +"&r&l " + target.getName() + "'s&r&7&o rally point."));
										target.sendMessage(chatFormat("&l" + guild.colours[permission - 1] + guild.levels[permission - 1] + "&r&l " + player.getName() + "&r&7&o has teleported to your rally point."));					
									}
								} else {
									sender.sendMessage(chatFormat("&c&oA fatal error has occurred [02]. Contact fraktalx."));
								}
							} else {
								sender.sendMessage(chatFormat("&c&oThe owner of the rally point is no longer online or in the faction."));
							}
						}
						catch(NullPointerException c) {
							cmsg(sender, 3);
						}
					}
					break;
					
				
				case "rename":
					if (!isPlayer) {
						cmsg(sender, 0); //no player
					} else if (!inGuild) {
						cmsg(sender, 1); //no guild
					} else if (args.length < 2) {
						sender.sendMessage(chatFormat("&c&ousage: /guild rename <newname>"));
					} else if (permission != 3) {
						cmsg(sender, 2); // no permission
					} else if (main.guildNames.contains(String.join(" ", Arrays.copyOfRange(args, 1, args.length)))) {
						sender.sendMessage(chatFormat("&c&oA guild with that name already exists."));
					} else {
						String pName = player.getName();
						guild.guildName = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).trim();
						main.generateGuildNames();
						guild.broadcastToAllMembers(chatFormat("&l" + pName + "&r&7&o renamed the guild to &r" + guild.getFormattedName() + "&r&7&o."));
						main.writeData();
					}
					break;
				
				
				case "resign":
					//if guild is empty, delete it
					//else turn target player into admin
					if(!isPlayer) {
						cmsg(sender, 0); //not player
					} else if (!inGuild) {
						cmsg(sender, 1); //no guild
					} else if (permission != 3) {
						cmsg(sender, 2); //no permission
					} else if (guild.playerList.size() == 1) {
						//player can leave
						guild.removePlayer(player);						
					} else if (args.length < 2) {
						sender.sendMessage(chatFormat("&c&ousage: /guild resign <new leader>"));
					} else {
						try {
							Player target = Bukkit.getPlayer(args[1].trim());
							if(!(guild.playerList.containsKey(target.getUniqueId().toString()))) {
								sender.sendMessage(chatFormat("&c&oThat player is not in your guild."));
							} else if (player == target) {
								sender.sendMessage(chatFormat("&c&oYou cannot resign to yourself."));
							} else {
								guild.setPermission(player, 2);
								guild.setPermission(target, 3);
								String pName = player.getName();
								String tName = target.getName();
								guild.broadcastToAllMembers(chatFormat("&l" + pName + "&r&7&o resigned as &r" + guild.colours[2] + guild.levels[2] + "&r&7&o"));
								Bukkit.broadcastMessage(chatFormat("&l" + tName + "&r&7&o became the " + guild.colours[2] + guild.levels[2] + "&r&7&o of &r" + guild.getFormattedName() + "&r&7&o."));
								main.writeData();
							}
						}
						catch(NullPointerException c) {
							cmsg(sender, 3);
						}
					}
					break;
					
					
				case "tell":
					//can only be executed by a player in a guild
					//sends message to every member of the guild
					if (!isPlayer) {
						cmsg(sender, 0); //not player
					} else if (!inGuild) {
						cmsg(sender, 1); //no guild
					} else if (args.length < 2) {
						sender.sendMessage(chatFormat("&c&ousage: /guild tell \"message\""));
					} else {
						guild.broadcastToAllMembers(guild.getTeamFormattedPlayerName(player) +": " + String.join(" ", Arrays.copyOfRange(args, 1, args.length)));						
					}
					break;
					
					
				case "accept-invite-hash":
					//unusable for players
					//can only be issued by a player who has been invited in the last x seconds by clicking on the invite message.
					if (!isPlayer) {
						return true;
					} else if (args.length < 2) {
						return true;
					} else if (!main.cooldowns.containsKey(player.getName())) {
						sender.sendMessage(chatFormat("&c&oYou have not been invited to join a guild."));
					} else if (main.cooldowns.get(player.getName()) < System.currentTimeMillis()){
						sender.sendMessage(chatFormat("&c&oThis invite has expired."));
						main.cooldowns.remove(player.getName());
					} else {						
						try {
							main.cooldowns.remove(player.getName());
							Guild target = main.getGuildFromName(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));						
							target.addPlayer(player, (byte) 1);
							main.writeData();
						}
						catch(NullPointerException c) {
							sender.sendMessage(chatFormat("&c&oThe guild you are trying to join no longer exists."));
						}
						catch(ClassCastException c) {
							sender.sendMessage(chatFormat("&c&oA fatal error has occurred [01]. Please contact fraktalx."));
						}			
					}
					break;
					
				
				//development args
				/*
				case "debug":
					//list every online player and their guild and permissions
					List<Player> players = main.getAllPlayers();
					for(Player p : players) {
						String output = p.getName() + " ";
						Guild g = main.getGuild(p);
						if(g != null) {
							output += g.getFormattedName() + " " + Integer.toString(g.playerList.get(p.getUniqueId().toString()));
						}
						sender.sendMessage(output);
					}
					break;
					
				*/
				case "wipe":
					//erase all data
					if(!sender.hasPermission("guild.wipe.use")) {
						cmsg(sender, 2); //no permission
					} else {
						for(Guild g : main.guilds) {
							g.selfDestruct();
						}
						
						main.guilds.clear();
						main.generateGuildNames();
						main.emptyConfig();
						sender.sendMessage(chatFormat("&c&All guilds have been deleted."));
					}
					break;
				
					
					
				//invalid args
				default:
					cmsg(sender, 4);
			}
			return true;
		}
		return false;
	}
	
	//return numeric value of a string
	//if not number return -1
	public int isInt(String s) {
		try {
			int a = Integer.parseInt(s.trim());
			return a;
		}
		catch (NumberFormatException e) {
			return -1;
		}		
	}
	
	//switch case the help section
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
	
	public String getInfo(Player p, Guild g) {
		//if about guild and not player p == null, g != null
		//if about player with no guild p != null, g == null
		//if about player with guild p, g != null
		if (g == null) {
			return p.getName() + " - no guild";
		} else if (p == null) {
			boolean b = main.isOnline(g.findLeaderID());
			String q = (b) ? "online" : "offline";
			String name = (b) ? Bukkit.getPlayer(UUID.fromString(g.findLeaderID())).getName() : main.getOffline(g.findLeaderID()).getName();
			return g.getFormattedName() + 
					ChatColor.translateAlternateColorCodes('&', " - &r&l" + Integer.toString(g.playerList.size()) + " member(s), " + Integer.toString(g.getAllOnlineMembers().size())) + " online, " + "Leader: " + name + " (" + q +			
					")\n" + String.join(", ", g.getAllOnlineMemberNames()); 
		} else {

			return p.getName() + " " + g.getFormattedPlayerName(p);
		}
	}
	public String chatFormat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public void cmsg(CommandSender sender, int i) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', errorText[i]));
	}
}

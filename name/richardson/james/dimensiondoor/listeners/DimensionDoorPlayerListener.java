/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * DimensionDoorPlayerListener.java is part of DimensionDoor.
 * 
 * DimensionDoor is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 * 
 * DimensionDoor is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License 
 * along with DimensionDoor.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package name.richardson.james.dimensiondoor.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;

import name.richardson.james.dimensiondoor.DimensionDoor;
import name.richardson.james.dimensiondoor.exceptions.WorldIsNotManagedException;
import name.richardson.james.dimensiondoor.persistent.WorldRecord;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class DimensionDoorPlayerListener extends PlayerListener {

  private final Logger log = Logger.getLogger("Minecraft");
  private final DimensionDoor plugin;

  public DimensionDoorPlayerListener(final DimensionDoor plugin) {
    this.plugin = plugin;
  }

  @Override
  public void onPlayerChat(final PlayerChatEvent event) {
    if (event.isCancelled())
      return;
    if (!plugin.isolatedChatAttributes.containsValue(true))
      return;

    final World originWorld = event.getPlayer().getWorld();
    String message = event.getFormat();
    message = message.replace("%1$s", event.getPlayer().getDisplayName());
    message = message.replace("%2$s", event.getMessage());

    if (plugin.isolatedChatAttributes.get(originWorld.getName()))
      sendIsolatedMessage(message, originWorld);
    else sendNormalMessage(message);

    event.setCancelled(true);
    log.info(message);
  }

  @Override
  public void onPlayerRespawn(final PlayerRespawnEvent event) {
    final Player player = event.getPlayer();
    final String currentWorld = player.getWorld().getName();
    final String destinationWorld = event.getRespawnLocation().getWorld().getName();
    // if the respawn location is not in the current world, set a new one
    if (!currentWorld.equals(destinationWorld))
      event.setRespawnLocation(plugin.getServer().getWorld(currentWorld).getSpawnLocation());
  }

  public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
    final Player player = event.getPlayer();
    final World world = event.getPlayer().getWorld();
    final String worldFrom = event.getFrom().getName();
    
    try {
      final WorldRecord worldRecord = WorldRecord.findFirst(world.getName());
      player.setGameMode(worldRecord.getGamemode());
    } catch (WorldIsNotManagedException e) {
      DimensionDoor.log(Level.SEVERE, String.format("A world has been loaded but has not been automatically registered: %s", world.getName()));
    }
    
    // fixes a way players can bring items back from CREATIVE mode.
    if (plugin.worldGameModes.get(worldFrom).equals(GameMode.CREATIVE)) {
      // clear the item in the player's hand
      if (plugin.inventoryProtectionConfiguration.getBoolean("inventory-settings.deleteItemInHandOnReturn", true)) {
        clearItemInHand(player);
      }
      // clear all items from a player's action bar
      if (plugin.inventoryProtectionConfiguration.getBoolean("inventory-settings.deleteActionBarOnReturn", true)) {
        clearActionBar(player);
      }
    }
  }
  
  private void sendIsolatedMessage(final String message, final World world) {
    for (final Player player : plugin.getServer().getOnlinePlayers()) {
      if (player.getWorld().getName().equalsIgnoreCase(world.getName()) && player != null) {
        player.sendMessage(message);
      }
    }
  }

  private void sendNormalMessage(final String message) {
    for (final Player player : plugin.getServer().getOnlinePlayers()) {
      if (!plugin.isolatedChatAttributes.get(player.getWorld().getName()) && player != null) {
        player.sendMessage(message);
      }
    }
  }
  
  private void clearItemInHand(Player player) {
    ItemStack itemInHand = player.getInventory().getItemInHand();
    // only clear the item if they are actually hold the item
    // otherwise we get an exception
    if (!itemInHand.getType().equals(Material.AIR)) {
      itemInHand.setAmount(-1);
      player.getInventory().setItemInHand(itemInHand);
    }
  }
  
  private void clearActionBar(Player player) {
    for (int i=0; i<=8; i++) {
      player.getInventory().clear(i);
    }
  }
  

}

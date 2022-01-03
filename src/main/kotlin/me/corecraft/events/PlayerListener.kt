@file:Suppress("DEPRECATION")

package me.corecraft.events

import me.corecraft.commands.getSavedPlayer
import me.corecraft.func.persist.Config
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*

object PlayerListener : Listener {

    // Log out commands
    @EventHandler
    fun onPlayerLogout(e: PlayerQuitEvent): Unit = with(e) {
        if (player.getSavedPlayer()?.getVanished() == true) e.quitMessage = ""

        if (player.getSavedPlayer()?.getFrozen() == false) return

        if (Config.staffQuitCommands.isEmpty() || !Config.staffQuitCommandsEnabled) return

        Config.staffQuitCommands.forEach {
            player.server.dispatchCommand(e.player.server.consoleSender, it.replace("%player%", player.name))
        }
    }

    // Misc
    @EventHandler
    fun onPlayerDrop(e: PlayerDropItemEvent): Unit = with(e) {
        if (player.getSavedPlayer()?.getStaff() == true || player.getSavedPlayer()?.getFrozen() == true) isCancelled = true
    }

    @EventHandler
    fun onPlayerPick(e: PlayerAttemptPickupItemEvent): Unit = with(e) {
        if (player.getSavedPlayer()?.getStaff() == true || player.getSavedPlayer()?.getFrozen() == true) isCancelled = true
    }

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent): Unit = with(e) {
        if (player.getSavedPlayer()?.getStaff() == true || player.getSavedPlayer()?.getFrozen() == true) isCancelled = true
    }

    @EventHandler
    fun onPlayerDropClick(e: InventoryClickEvent): Unit = with(e) {
        if (whoClicked !is Player) return
        val player = whoClicked as Player
        if (player.getSavedPlayer()?.getStaff() == true || player.getSavedPlayer()?.getFrozen() == true) {
            isCancelled = true
            return
        }
    }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent): Unit = with(e) {
        if (player.getSavedPlayer()?.getFrozen() == true) isCancelled = true else return
    }
}
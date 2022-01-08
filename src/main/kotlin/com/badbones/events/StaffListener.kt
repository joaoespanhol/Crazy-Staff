package com.badbones.events

import com.badbones.commands.getSavedPlayer
import com.badbones.commands.parseMessage
import com.badbones.events.module.MiscManager
import com.badbones.events.module.RandomManager
import com.badbones.func.persist.Config
import com.badbones.func.persist.Data
import com.badbones.hooks.enums.Permissions
import com.badbones.hooks.enums.hasPermission
import io.papermc.lib.PaperLib
import net.kyori.adventure.title.Title
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.plugin.java.JavaPlugin

class StaffListener(private val plugin: JavaPlugin) : Listener {

    @EventHandler
    fun onHungerChange(e: FoodLevelChangeEvent): Unit = with(e) {
        if (entity !is Player) return
        val player = entity as Player

        if (player.getSavedPlayer()?.getStaff() == true || player.getSavedPlayer()?.getFrozen() == true && foodLevel < 20) foodLevel = 20
    }

    @EventHandler
    fun onWorldChange(e: PlayerChangedWorldEvent): Unit = with(e) {
        if (player.getSavedPlayer()?.getStaff() == false) return

        player.gameMode = GameMode.CREATIVE
    }

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent): Unit = with(e) {
        if (player.getSavedPlayer()?.getStaff() == false) return

        if (action == Action.PHYSICAL) return

        if (hasPermission(player, Permissions.STAFF_ITEMS_USE)) {
            when (player.inventory.itemInMainHand.type) {
                Config.staffItems.randomItem.material -> {
                    RandomManager.createGui(player)
                    isCancelled = true
                }
                Config.staffItems.vanishOffItem.material -> {
                    player.getSavedPlayer()?.setVanished(plugin)
                    if (Config.staffVanishEffects) MiscManager.effectLightning(player.location)
                    isCancelled = true
                }
                Config.staffItems.vanishOnItem.material -> {
                    player.getSavedPlayer()?.setVanished(plugin)
                    if (Config.staffVanishEffects) MiscManager.effectLightning(player.location)
                    isCancelled = true
                }
                else -> {
                    Config.staffCustomItems.forEach {
                        if (player.inventory.itemInMainHand.type == it.material) player.performCommand(it.command)
                    }
                }
            }
        }

        //if (hasPermission(player, Permissions.STAFF_SILENT_CHEST) && Config.staffSilentOpen) {

        //}
    }

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractAtEntityEvent): Unit = with(e) {
        if (player.getSavedPlayer()?.getStaff() == false) return
        if (player.inventory.itemInMainHand.type != Config.staffItems.freezeItem.material || !hasPermission(player, Permissions.STAFF_ITEMS_USE) || hand != EquipmentSlot.HAND) return

        val person = rightClicked as Player

        if (hasPermission(person, Permissions.FREEZE_BYPASS)) {
            val title = Title.title(parseMessage("&cCannot freeze."), parseMessage("&fNice Try!"))
            player.showTitle(title)
            return
        }

        runFreeze(person, player)
    }

    private fun runFreeze(rightClicked: Player, player: Player) {
        when (rightClicked.getSavedPlayer()?.getFrozen()) {
            true -> {
                if (PaperLib.isPaper() && Config.useTitles) MiscManager.sendTitle(
                    rightClicked,
                    Config.unFreezeTitle.title,
                    Config.unFreezeTitle.subtitle
                ) else rightClicked.sendMessage(parseMessage(Config.freezeLegacy))

                rightClicked.getSavedPlayer()?.setFrozen()

                rightClicked.gameMode = GameMode.SURVIVAL
                rightClicked.isInvulnerable = false

                player.playSound(player.location, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 0.5F, 0.5F)
            }
            false -> {
                if (PaperLib.isPaper() && Config.useTitles) MiscManager.sendTitle(
                    rightClicked,
                    Config.freezeTitle.title,
                    Config.freezeTitle.subtitle
                ) else rightClicked.sendMessage(parseMessage(Config.unFreezeLegacy))

                rightClicked.getSavedPlayer()?.setFrozen()

                rightClicked.gameMode = GameMode.ADVENTURE
                rightClicked.isInvulnerable = true
                Data.playerSpawn.forEach {
                    when (PaperLib.isPaper()) {
                        true -> {
                            if (Data.playerSpawn.isNotEmpty()) rightClicked.teleportAsync(it.getLocation()) else {
                                rightClicked.server.logger.info("No spawn location found, Teleporting to the nearest spawn location.")
                                rightClicked.server.logger.info("Make sure they don't die.")
                                rightClicked.teleportAsync(player.world.spawnLocation)
                            }
                        }
                        false -> {
                            if (Data.playerSpawn.isNotEmpty()) rightClicked.teleport(it.getLocation()) else {
                                rightClicked.server.logger.info("No spawn location found, Teleporting to the nearest spawn location.")
                                rightClicked.server.logger.info("Make sure they don't die.")
                                rightClicked.teleport(player.world.spawnLocation)
                            }
                        }
                    }
                }
                player.playSound(player.location, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 0.5F, 0.5F)
                return
            }
            else -> {}
        }
    }
}
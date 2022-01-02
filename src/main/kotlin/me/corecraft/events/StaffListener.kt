package me.corecraft.events

import me.corecraft.commands.getSavedPlayer
import me.corecraft.commands.parseMessage
import net.kyori.adventure.title.Title
import me.corecraft.events.module.MiscManager
import me.corecraft.events.module.RandomManager
import me.corecraft.func.persist.Config
import me.corecraft.hooks.enums.Permissions
import me.corecraft.hooks.enums.hasPermission
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryClickEvent
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

    @EventHandler(ignoreCancelled = true)
    fun onWorldChange(e: PlayerChangedWorldEvent): Unit = with(e) {
        if (player.getSavedPlayer()?.getStaff() == false) return

        player.gameMode = GameMode.CREATIVE
    }

    @EventHandler
    fun onPlayerLogout(e: PlayerQuitEvent): Unit = with(e) {

        if (player.getSavedPlayer()?.getFrozen() == false) return

        if (Config.staffQuitCommands.isEmpty()) return

        Config.staffQuitCommands.forEach {
            player.server.dispatchCommand(e.player.server.consoleSender, it.replace("%player%", player.name))
        }
    }

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent): Unit = with(e) {
        if (player.getSavedPlayer()?.getStaff() == false) return

        if (!hasPermission(player, Permissions.STAFF_ITEMS_USE)) return

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
                Config.staffCustomItem.forEach {
                    if (player.inventory.itemInMainHand.type == it.material) player.performCommand(it.command)
                }
            }
        }
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

        runFreeze(rightClicked as Player, player)
    }

    private fun runFreeze(rightClicked: Player, player: Player) {
        when (rightClicked.getSavedPlayer()?.getFrozen()) {
            true -> {
                MiscManager.sendTitle(rightClicked, Config.unFreezeTitle.title, Config.unFreezeTitle.subtitle)

                rightClicked.getSavedPlayer()?.setFrozen()

                rightClicked.gameMode = GameMode.SURVIVAL
                rightClicked.isInvulnerable = false

                player.playSound(player.location, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 0.5F, 0.5F)
            }
            false -> {
                MiscManager.sendTitle(rightClicked, Config.freezeTitle.title, Config.freezeTitle.subtitle)

                rightClicked.getSavedPlayer()?.setFrozen()

                rightClicked.gameMode = GameMode.ADVENTURE
                rightClicked.isInvulnerable = true
                rightClicked.teleport(
                    Location(
                        rightClicked.server.getWorld("world"),
                        0.5,
                        61.0,
                        0.5,
                        0.022159427404403687F,
                        -0.1511383205652237F
                    )
                )
                player.playSound(player.location, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 0.5F, 0.5F)
            }
            else -> {}
        }
    }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent): Unit = with(e) {
        when (player.getSavedPlayer()?.getFrozen()) {
            true -> {
                isCancelled = true
            }
            false -> {
                return
            }
            else -> {}
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerDrop(e: PlayerDropItemEvent): Unit = with(e) {
        if (player.getSavedPlayer()?.getStaff() == true || player.getSavedPlayer()?.getFrozen()!!) isCancelled = true
    }

    @EventHandler
    fun onPlayerPick(e: PlayerAttemptPickupItemEvent): Unit = with(e) {
        if (player.getSavedPlayer()?.getStaff() == true || player.getSavedPlayer()?.getFrozen()!!) isCancelled = true
    }

    @EventHandler
    fun onPlayerDamage(e: PlayerInteractEvent): Unit = with(e) {
        if (player.getSavedPlayer()?.getStaff() == true || player.getSavedPlayer()?.getFrozen()!!) isCancelled = true
    }

    @EventHandler
    fun onPlayerDamageTo(e: EntityDamageEvent): Unit = with(e) {
        if (e.entity !is Player) return
        val player = e.entity as Player
        if (player.getSavedPlayer()?.getStaff() == true || player.getSavedPlayer()?.getFrozen()!!) isCancelled = true
    }

    @EventHandler
    fun onPlayerDamage(e: EntityDamageByEntityEvent): Unit = with(e) {
        if (e.damager !is Player) return
        val player = e.damager as Player
        if (player.getSavedPlayer()?.getStaff() == true || player.getSavedPlayer()?.getFrozen()!!) isCancelled = true
    }

    @EventHandler
    fun onPlayerDropClick(e: InventoryClickEvent): Unit = with(e) {
        if (whoClicked !is Player) return
        val player = whoClicked as Player
        if (player.getSavedPlayer()?.getStaff() == true || player.getSavedPlayer()?.getFrozen()!!) {
            isCancelled = true
            return
        }
    }
}
package me.corecraft.events.module

import dev.triumphteam.gui.builder.item.ItemBuilder
import me.corecraft.commands.getSavedPlayer
import me.corecraft.commands.hideStaff
import me.corecraft.commands.parseName
import me.corecraft.commands.showStaff
import me.corecraft.func.persist.Config
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class VanishManager(private val plugin: JavaPlugin) {
    fun run(player: Player?) {
        val vanishOffItem = ItemBuilder.from(Config.staffItems.vanishOffItem.material).name(parseName(Config.staffItems.vanishOffItem.name)).build()
        val vanishOnItem = ItemBuilder.from(Config.staffItems.vanishOnItem.material).name(parseName(Config.staffItems.vanishOnItem.name)).build()
        when (player?.getSavedPlayer()?.getVanished()) {
            true -> {
                player.hideStaff(plugin)
                player.inventory.setItem(Config.staffItems.vanishOnItem.slot, vanishOnItem)
            }
            false -> {
                player.showStaff(plugin)
                player.inventory.setItem(Config.staffItems.vanishOffItem.slot, vanishOffItem)
            }
            else -> {}
        }
    }
}
package com.badbones.events.module

import com.badbones.commands.getSavedPlayer
import com.badbones.commands.hideStaff
import com.badbones.commands.parseName
import com.badbones.commands.showStaff
import com.badbones.func.persist.Config
import dev.triumphteam.gui.builder.item.ItemBuilder
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class VanishManager(private val plugin: JavaPlugin) {
    fun run(player: Player?) {
        if (Config.staffItems.vanishOnItem.slot == 100 || Config.staffItems.vanishOffItem.slot == 100) {
            plugin.logger.warning("One of your vanish items in config.json has 100 as a value.")
            return
        }
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
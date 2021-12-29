package me.corecraft.events.module

import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.Gui
import me.corecraft.commands.getSavedPlayer
import me.corecraft.commands.parseName
import org.bukkit.Material
import org.bukkit.entity.Player

object RandomManager {

    private val gui = Gui.paginated().title(parseName("&cRandom Teleport")).rows(4).pageSize(27).disableAllInteractions().create()

    fun createGui(player: Player) {
        gui.setItem(4, 3, ItemBuilder.from(Material.PAPER).name(parseName("&cPrevious")).asGuiItem { gui.previous() })
        gui.setItem(4, 7, ItemBuilder.from(Material.PAPER).name(parseName("&aNext")).asGuiItem { gui.next() })

        player.server.onlinePlayers.forEach {
            val skull = ItemBuilder.skull().owner(it.player!!).name(parseName("&e${it.player!!.name}")).lore(parseName("&7Right/Left click to &ateleport")).asGuiItem { players ->
                when {
                    players.isRightClick -> {
                        if (player != it) player.teleport(it.player!!.location)
                    }
                    players.isLeftClick -> {
                        if (player != it) player.teleport(it.player!!.location)
                    }
                }
            }
            if (!it.getSavedPlayer()?.isStaff()!!) {
                gui.clearPageItems()
                gui.addItem(skull)
            }
        }

        gui.open(player)
    }
}
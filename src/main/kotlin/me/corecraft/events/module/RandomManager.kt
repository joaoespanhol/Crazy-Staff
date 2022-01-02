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

        player.server.onlinePlayers.forEach { person ->
            val skull = person?.player?.let { owner ->
                ItemBuilder.skull().owner(owner).name(parseName("&e${person.player?.name}")).lore(parseName("&7Right/Left click to &ateleport")).asGuiItem { players ->
                    when {
                        players.isRightClick -> {
                            if (player != person) person.player?.location?.let { teleporter -> player.teleport(teleporter) }
                        }
                        players.isLeftClick -> {
                            if (player != person) person.player?.location?.let { teleporter -> player.teleport(teleporter) }
                        }
                    }
                }
            }
            if (person.getSavedPlayer()?.getStaff()  == true) {
                gui.clearPageItems()
                skull?.let { skulls -> gui.addItem(skulls) }
            }
        }

        gui.open(player)
    }
}
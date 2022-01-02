package me.corecraft.func.persist

import me.corecraft.commands.createInventory
import me.corecraft.commands.getSavedPlayer
import me.corecraft.commands.hideStaff
import me.corecraft.events.module.data.Player
import me.corecraft.func.types.json.Serializer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

object Data {

    var nextPlayerID: Int = 0
    var players = hashMapOf<UUID, Player>()

    fun load(plugin: JavaPlugin) = Serializer(plugin.dataFolder, true)
        .load(this, Data::class.java, "data.json")

    fun save(plugin: JavaPlugin) = Serializer(plugin.dataFolder, true)
        .save(this, "data.json")

}

class DataListener(private val plugin: JavaPlugin) : Listener {

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent): Unit = with(e) {
        when {
            player.getSavedPlayer() != null -> {
                if (player.getSavedPlayer()?.isStaff()!!) {
                    player.inventory.clear()
                    player.createInventory(plugin)
                    player.hideStaff(plugin)
                }
            }
            else -> {
                createPlayer(player)
            }
        }
    }

    private fun createPlayer(player: org.bukkit.entity.Player): Player {
        val newPlayer = Player(player.uniqueId)
        Data.players[player.uniqueId] = newPlayer
        Data.nextPlayerID++
        return newPlayer
    }
}
package me.corecraft.func.persist

import me.corecraft.commands.createInventory
import me.corecraft.commands.getSavedPlayer
import me.corecraft.commands.hideStaff
import me.corecraft.events.module.data.Player
import me.corecraft.func.types.json.Serializer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

object Data {

    var nextPlayerID: Int = 0
    var players = hashMapOf<UUID, Player>()

    var playerSpawn = hashSetOf<DataLocation>()

    fun load(plugin: JavaPlugin) = Serializer(plugin.dataFolder, true).load(this, Data::class.java, "data.json")

    fun save(plugin: JavaPlugin) = Serializer(plugin.dataFolder, true).save(this, "data.json")

}

class DataListener(private val plugin: JavaPlugin) : Listener {

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent): Unit = with(e) {
        when {
            player.getSavedPlayer() != null -> {
                if (player.getSavedPlayer()?.getStaff() == true) {
                    player.inventory.clear()
                    player.createInventory(plugin)
                    player.hideStaff(plugin)
                }
            }
            else -> {
                createPlayer(player)
            }
        }

        player.server.onlinePlayers.forEach {
            it.hideStaff(plugin)
        }
    }
}

fun createPlayer(player: org.bukkit.entity.Player): Player {
    val newPlayer = Player(player.uniqueId)
    Data.players[player.uniqueId] = newPlayer
    Data.nextPlayerID++
    return newPlayer
}

fun setSpawn(worldName: String, x: Double, y: Double, z: Double, yaw: Float = -1F, pitch: Float = -1F): Boolean {
    val loc = DataLocation(worldName, x, y, z, yaw, pitch)
    return Data.playerSpawn.add(loc)
}

data class DataLocation(val worldName: String?, val x: Double, val y: Double, val z: Double, val yaw: Float = -1F, val pitch: Float = -1F) {

    fun getLocation() = Location(Bukkit.getWorld(worldName.toString()), x, y, z).apply {
        val dataYaw = this@DataLocation.yaw
        if (dataYaw != -1F) yaw = dataYaw

        val dataPitch = this@DataLocation.pitch
        if (dataPitch != -1F) pitch = dataPitch
    }

    fun removeSpawn() {
        return Data.playerSpawn.clear()
    }
}
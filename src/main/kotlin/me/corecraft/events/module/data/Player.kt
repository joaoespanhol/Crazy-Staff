package me.corecraft.events.module.data

import dev.triumphteam.gui.builder.item.ItemBuilder
import me.corecraft.commands.hideStaff
import me.corecraft.commands.parseName
import me.corecraft.commands.showStaff
import me.corecraft.events.module.MiscManager
import me.corecraft.func.persist.Config
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

data class Player(val uuid: UUID) {

    private var frozen: Boolean = false

    private var staff: Boolean = false

    private var vanished: Boolean = false

    private var location = hashMapOf<String, DataLocation>()

    data class DataLocation(val worldName: String?, val x: Double, val y: Double, val z: Double, val yaw: Float = -1F, val pitch: Float = -1F) {
        //fun exists() = Bukkit.getWorld(worldName) != null

        fun getLocation() = Location(Bukkit.getWorld(worldName.toString()), x, y, z).apply {
            val dataYaw = this@DataLocation.yaw
            if (dataYaw != -1F) yaw = dataYaw

            val dataPitch = this@DataLocation.pitch
            if (dataPitch != -1F) pitch = dataPitch
        }
    }

    fun removeLocation() {
        return location.clear()
    }

    fun getCurrentLocation(name: String): Location? {
        return location[name]?.getLocation()
    }

    fun setLastLocation(name: String, worldName: String, x: Double, y: Double, z: Double, yaw: Float = -1F, pitch: Float = -1F) {
        location[name] = DataLocation(worldName, x, y, z, yaw, pitch)
    }

    fun isStaff() = staff

    fun isFrozen() = frozen

    fun setFrozen() {
        frozen = !isFrozen()
    }

    fun setStaff() {
        staff = !isStaff()
    }

    fun setVanished(player: Player?, plugin: JavaPlugin) {
        vanished = !vanished
        switchItem(player, plugin)
    }

    fun switchItem(player: Player?, plugin: JavaPlugin) {
        if (Config.staffVanishEffects) MiscManager.effectLightning(player?.location!!)
        val vanishOffItem = ItemBuilder.from(Config.staffItems.vanishOffItem.material).name(parseName(Config.staffItems.vanishOffItem.name)).build()
        val vanishOnItem = ItemBuilder.from(Config.staffItems.vanishOnItem.material).name(parseName(Config.staffItems.vanishOnItem.name)).build()
        when (vanished) {
            true -> {
                player?.hideStaff(plugin)
                player?.inventory?.setItem(Config.staffItems.vanishOnItem.slot, vanishOnItem)
            }
            false -> {
                player?.showStaff(plugin)
                player?.inventory?.setItem(Config.staffItems.vanishOffItem.slot, vanishOffItem)
            }
        }
    }
}
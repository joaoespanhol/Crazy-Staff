package me.corecraft.events.module.data

import me.corecraft.events.module.VanishManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

data class Player(val uuid: UUID) {

    private var frozen: Boolean = false

    private var staff: Boolean = false

    private var vanished: Boolean = false

    private var location = hashMapOf<String, DataLocation>()

    data class DataLocation(val worldName: String?, val x: Double, val y: Double, val z: Double, val yaw: Float = -1F, val pitch: Float = -1F) {

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

    fun isVanished() = vanished

    fun setFrozen() {
        frozen = !isFrozen()
    }

    fun setStaff() {
        staff = !isStaff()
    }

    fun setVanished(plugin: JavaPlugin) {
        vanished = !vanished
        VanishManager(plugin).run(Bukkit.getPlayer(uuid)!!)
    }
}
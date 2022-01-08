package com.badbones.events.module.data

import com.badbones.events.module.VanishManager
import com.badbones.func.persist.DataLocation
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

data class Player(val uuid: UUID) {

    private var frozen: Boolean = false

    private var staff: Boolean = false

    private var vanished: Boolean = false

    private var location = hashMapOf<String, DataLocation>()

    fun removeLocation() {
        return location.clear()
    }

    fun getCurrentLocation(name: String): Location? {
        return location[name]?.getLocation()
    }

    fun setLastLocation(name: String, worldName: String, x: Double, y: Double, z: Double, yaw: Float = -1F, pitch: Float = -1F) {
        location[name] = DataLocation(worldName, x, y, z, yaw, pitch)
    }

    fun getStaff() = staff

    fun getFrozen() = frozen

    fun getVanished() = vanished

    fun setFrozen() {
        frozen = !getFrozen()
    }

    fun setStaff() {
        staff = !getStaff()
    }

    fun setVanished(plugin: JavaPlugin, exit: Boolean = false, vanishOff: Boolean = false) {
        if (exit) vanished = vanishOff else {
            vanished = !vanished
            VanishManager(plugin).run(getPlayer())
        }
    }

    // Player Checks

    private fun getPlayer() = Bukkit.getPlayer(uuid)

    fun isOnline() = getPlayer()?.isOnline == true

    fun isOffline() = getPlayer()?.isOnline == false

}
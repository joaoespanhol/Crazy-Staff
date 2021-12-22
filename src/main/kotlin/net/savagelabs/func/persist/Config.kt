package net.savagelabs.func.persist

import net.savagelabs.func.types.json.JsonManager
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

object Config {

    data class StaffModeEnterTitle(val title: String, val subtitle: String)
    data class StaffModeQuitTitle(val title: String, val subtitle: String)

    var messagePrefix = "&r &cStaff &8>"

    var staffModeEnter = StaffModeEnterTitle("&cStaff Mode", "&aENABLED")
    var staffModeExit = StaffModeQuitTitle("&cStaff Mode", "&cDISABLED")

    var staffItems = StaffItems(
        RandomItem(
            Material.ENDER_EYE,
            "&cRandom Teleport",
            listOf(),
            6
        ),
        FreezeItem(
            Material.BLAZE_ROD,
            "&dFreeze Player",
            listOf(),
            2
        ),
        VanishOffItem(
            Material.RED_DYE,
            "&cVanish &4Off",
            listOf(),
            4
        ),
        VanishOnItem(
            Material.LIME_DYE,
            "&cVanish &aOn",
            listOf(),
            4
        )
    )

    fun load(plugin: JavaPlugin) = JsonManager(plugin, false).loadFromFile(this, Config::class.java, "config.json")

    fun save(plugin: JavaPlugin) = JsonManager(plugin, false).saveToFile(this, "config.json")

}

data class StaffItems(val randomItem: RandomItem, val freezeItem: FreezeItem, val vanishOffItem: VanishOffItem, val vanishOnItem: VanishOnItem)

data class RandomItem(val material: Material, val name: String, val lore: List<String>, val slot: Int)

data class FreezeItem(val material: Material, val name: String, val lore: List<String>, val slot: Int)

data class VanishOffItem(val material: Material, val name: String, val lore: List<String>, val slot: Int)

data class VanishOnItem(val material: Material, val name: String, val lore: List<String>, val slot: Int)
package me.corecraft.func.persist

import me.corecraft.func.types.json.Serializer
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

object Config {

    data class StaffModeEnterTitle(val title: String, val subtitle: String)
    data class StaffModeQuitTitle(val title: String, val subtitle: String)

    data class FreezeTitle(val title: String, val subtitle: String)
    data class UnFreezeTitle(val title: String, val subtitle: String)

    var staffModeEnter = StaffModeEnterTitle("&cStaff Mode", "&aENABLED")
    var staffModeExit = StaffModeQuitTitle("&cStaff Mode", "&cDISABLED")

    var freezeTitle = FreezeTitle("&cYou have been frozen.", "&fDo not log out.")
    var unFreezeTitle = UnFreezeTitle("&cYou have been unfrozen.", "")

    var staffSilentOpen = false
    var staffVanishEffects = true

    var staffQuitCommands = listOf(
        "tempban %player% hacking 1d"
    )

    var staffCustomItem = listOf(
        CustomItem(
            Material.CHEST,
            "&cPunishment Menu",
            listOf(),
            "punisher",
            0
        )
    )

    data class CustomItem(val material: Material, val name: String, val lore: List<String>, val command: String, val slot: Int)

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

    fun load(plugin: JavaPlugin) = Serializer(plugin.dataFolder, false).load(this, Config::class.java, "config.json")

    fun save(plugin: JavaPlugin) = Serializer(plugin.dataFolder, false).save(this, "config.json")

}

data class StaffItems(val randomItem: RandomItem, val freezeItem: FreezeItem, val vanishOffItem: VanishOffItem, val vanishOnItem: VanishOnItem)

data class RandomItem(val material: Material, val name: String, val lore: List<String>, val slot: Int)

data class FreezeItem(val material: Material, val name: String, val lore: List<String>, val slot: Int)

data class VanishOffItem(val material: Material, val name: String, val lore: List<String>, val slot: Int)

data class VanishOnItem(val material: Material, val name: String, val lore: List<String>, val slot: Int)
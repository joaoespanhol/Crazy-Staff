@file:Suppress("DEPRECATION")

package me.corecraft.commands

import dev.triumphteam.gui.builder.item.ItemBuilder
import io.papermc.lib.PaperLib
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.msg.adventure.AdventureMessage
import me.mattstudios.msg.base.MessageOptions
import me.mattstudios.msg.base.internal.Format
import net.kyori.adventure.text.Component
import me.corecraft.events.module.MiscManager
import me.corecraft.events.module.VanishManager
import me.corecraft.func.colorizeList
import me.corecraft.func.persist.Config
import me.corecraft.func.persist.Data
import me.corecraft.func.persist.setSpawn
import me.corecraft.hooks.Support
import me.corecraft.hooks.enums.Permissions
import me.corecraft.hooks.enums.hasPermission
import me.corecraft.hooks.types.EssentialsXAPI
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@Command("staff")
class StaffCommand(private val plugin: JavaPlugin) : CommandBase() {

    @Default
    @Permission("staff.use")
    fun defaultCommand(player: Player): Unit = with(player) {
        if (getSavedPlayer()?.getStaff() == true) exitStaff(plugin) else enterStaff(plugin)
    }

    @SubCommand("reload")
    @Permission("staff.use.reload")
    fun onReload(commandSender: CommandSender): Unit = with(commandSender) {
        Config.load(plugin)
        Config.save(plugin)

        Data.save(plugin)

        commandSender.server.onlinePlayers.forEach {
            if (it.getSavedPlayer()?.getStaff() == true) it.createInventory(plugin)
        }
    }

    @SubCommand("setspawn")
    @Permission("staff.use.setspawn")
    fun onSetSpawn(player: Player): Unit = with (player) {
        if (!Data.serverSetUp) Data.serverSetUp = true
        setSpawn(world.name, location.x, location.y, location.z, location.yaw, location.pitch)

        sendMessage(parseMessage(Config.spawnSet))
    }
}

fun Player.getSavedPlayer() = Data.players[uniqueId]

fun Player.hideStaff(plugin: JavaPlugin) {
    when {
        Support.ESSENTIALSX.isPluginLoaded -> {
            EssentialsXAPI.setVanish(player)
        }
        else -> {
            server.onlinePlayers.forEach {
                if (hasPermission(it, Permissions.VANISH_SEE)) it.showPlayer(plugin, player!!) else it.hidePlayer(plugin, player!!)
            }
        }
    }
}

fun Player.showStaff(plugin: JavaPlugin) {
    when {
        Support.ESSENTIALSX.isPluginLoaded -> {
            EssentialsXAPI.setVanish(player)
        }
        else -> {
            server.onlinePlayers.forEach {
                it.showPlayer(plugin, player!!)
            }
        }
    }
}

fun Player.enterStaff(plugin: JavaPlugin) {

    if (PaperLib.isPaper() && Config.useTitles) MiscManager.sendTitle(this, Config.staffModeEnter.title, Config.staffModeEnter.subtitle) else sendMessage(parseMessage(Config.staffModeEnterLegacy))

    getSavedPlayer()?.setStaff()
    VanishManager(plugin).run(this)

    if (getSavedPlayer()?.getCurrentLocation(name) != null) getSavedPlayer()?.removeLocation()

    getSavedPlayer()?.setLastLocation(name, world.name, location.x, location.y, location.z, location.yaw, location.pitch)

    gameMode = GameMode.CREATIVE

    createInventory(plugin)
}

fun Player.exitStaff(plugin: JavaPlugin) {

    if (PaperLib.isPaper() && Config.useTitles) MiscManager.sendTitle(this, Config.staffModeExit.title, Config.staffModeExit.subtitle) else sendMessage(parseMessage(Config.staffModeExitLegacy))

    getSavedPlayer()?.setStaff()
    VanishManager(plugin).run(this)

    inventory.clear()

    gameMode = GameMode.SURVIVAL
    foodLevel = 20

    val loc = getSavedPlayer()?.getCurrentLocation(name)
    teleport(Location(loc?.world, loc?.x!!, loc.y, loc.z, loc.yaw, loc.pitch))

    getSavedPlayer()?.removeLocation()
}

fun Player.createInventory(plugin: JavaPlugin) {

    inventory.clear()

    val randomTeleport = ItemBuilder.from(Config.staffItems.randomItem.material).name(parseName(Config.staffItems.randomItem.name)).build()
    val freezeItem = ItemBuilder.from(Config.staffItems.freezeItem.material).name(parseName(Config.staffItems.freezeItem.name)).build()

    VanishManager(plugin).run(player)

    if (Config.staffCustomItem.isNotEmpty()) {
        Config.staffCustomItem.forEach {
            val item = ItemBuilder.from(it.material).name(parseName(it.name)).setLore(parseLore(it.lore)).amount(1).build()
            inventory.setItem(it.slot, item)
        }
    }

    inventory.setItem(Config.staffItems.randomItem.slot, randomTeleport)
    inventory.setItem(Config.staffItems.freezeItem.slot, freezeItem)
}

fun getMessage(): AdventureMessage {
    val msgOptions = MessageOptions.builder().removeFormat(Format.ITALIC).addFormat(Format.COLOR).build()
    return AdventureMessage.create(msgOptions)
}

fun parseName(msg: String): Component {
    return getMessage().parse(msg)
}

fun parseLore(msg: List<String>): ArrayList<String> {
    return colorizeList(msg)
}

fun parseMessage(message: String): Component {
    return getMessage().parse(message)
}
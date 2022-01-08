@file:Suppress("DEPRECATION")

package com.badbones.commands

import com.badbones.events.module.MiscManager
import com.badbones.events.module.VanishManager
import com.badbones.func.colorizeList
import com.badbones.func.persist.Config
import com.badbones.func.persist.Data
import com.badbones.func.persist.setSpawn
import com.badbones.hooks.Support
import com.badbones.hooks.enums.Permissions
import com.badbones.hooks.enums.hasPermission
import com.badbones.hooks.types.EssentialsXAPI
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
                player?.let { pp ->
                    if (hasPermission(it, Permissions.VANISH_SEE)) it.showPlayer(plugin, pp) else it.hidePlayer(plugin, pp)
                }
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
                player?.let { pp -> it.showPlayer(plugin, pp) }
            }
        }
    }
}

fun Player.enterStaff(plugin: JavaPlugin) {

    if (PaperLib.isPaper() && Config.useTitles) MiscManager.sendTitle(this, Config.staffModeEnter.title, Config.staffModeEnter.subtitle) else sendMessage(parseMessage(Config.staffModeEnterLegacy))

    getSavedPlayer()?.setStaff()
    getSavedPlayer()?.setVanished(plugin)

    if (getSavedPlayer()?.getCurrentLocation(name) != null) getSavedPlayer()?.removeLocation()

    getSavedPlayer()?.setLastLocation(name, world.name, location.x, location.y, location.z, location.yaw, location.pitch)

    gameMode = GameMode.CREATIVE

    createInventory(plugin)
}

fun Player.exitStaff(plugin: JavaPlugin) {

    if (PaperLib.isPaper() && Config.useTitles) MiscManager.sendTitle(this, Config.staffModeExit.title, Config.staffModeExit.subtitle) else sendMessage(parseMessage(Config.staffModeExitLegacy))

    getSavedPlayer()?.setStaff()
    getSavedPlayer()?.setVanished(plugin, true)

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

    if (Config.staffCustomItems.isNotEmpty() && Config.staffCustomItemsEnabled) {
        Config.staffCustomItems.forEach {
            val item = ItemBuilder.from(it.material).name(parseName(it.name)).setLore(parseLore(it.lore)).amount(1).build()
            inventory.setItem(it.slot, item)
        }
    }

    if (Config.staffItems.randomItem.slot != 100) inventory.setItem(Config.staffItems.randomItem.slot, randomTeleport) else plugin.logger.info("Random Teleport slot is set to 100 and will not load.")
    if (Config.staffItems.randomItem.slot != 100) inventory.setItem(Config.staffItems.freezeItem.slot, freezeItem) else plugin.logger.info("Freeze Tool slot is set to 100 and will not load.")
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
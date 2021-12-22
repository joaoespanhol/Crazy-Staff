@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package net.savagelabs.commands

import dev.triumphteam.gui.builder.item.ItemBuilder
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.msg.adventure.AdventureMessage
import me.mattstudios.msg.base.MessageOptions
import me.mattstudios.msg.base.internal.Format
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.savagelabs.func.persist.Config
import net.savagelabs.func.persist.Data
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@Command("staff")
class StaffCommand(private val plugin: JavaPlugin) : CommandBase() {

    @Default
    @Permission("staffx.use")
    fun defaultCommand(player: Player): Unit = with(player) {
        when (getSavedPlayer()?.isStaff) {
            true -> {
                player.exitStaff(plugin)
            }
            else -> {
                player.enterStaff(plugin)
            }
        }
    }

    @SubCommand("reload")
    @Permission("staffx.use.reload")
    fun onReload(commandSender: CommandSender): Unit = with(commandSender) {
        Config.load(plugin)
        Config.save(plugin)

        Data.save(plugin)

        commandSender.server.onlinePlayers.forEach {
            when (it.getSavedPlayer()?.isStaff) {
                true -> {
                    it.createInventory(plugin)
                }
                else -> {
                    plugin.logger.info("No players are currently in /staff")
                }
            }
        }
    }
}

fun Player.getSavedPlayer() = Data.players[uniqueId]

fun Player.hideStaff(plugin: JavaPlugin) {
    server.onlinePlayers.forEach {
        if (it.player?.hasPermission("staffx.vanish.see")!!) {
            it.showPlayer(plugin, player!!)
            return
        } else {
            it.hidePlayer(plugin, player!!)
        }
    }
}

fun Player.showStaff(plugin: JavaPlugin) {
    server.onlinePlayers.forEach {
        it.showPlayer(plugin, player!!)
        return
    }
}

fun Player.enterStaff(plugin: JavaPlugin) {

    // Tell them they entered staff mode
    val title = Title.title(parseMessage(Config.staffModeEnter.title), parseMessage(Config.staffModeEnter.subtitle))

    showTitle(title)

    // Save armor contents
    val armorContents = inventory.armorContents
    val invContents = inventory.contents
    getSavedPlayer()?.armorContents = armorContents
    getSavedPlayer()?.inventoryContents = invContents
    // Set isStaff for true, So we can persist joins/restarts
    getSavedPlayer()?.isStaff = true
    getSavedPlayer()?.isVanished = true
    // Just in case it's not true
    //if (!getSavedPlayer()?.isVanished!!) getSavedPlayer()?.isVanished = true

    // Set gamemode to ADVENTURE with fly
    gameMode = GameMode.ADVENTURE
    allowFlight = true
    isFlying = true

    // Hide the player
    hideStaff(plugin)

    // Create items
    createInventory(plugin)

    Data.save(plugin)
}

fun Player.exitStaff(plugin: JavaPlugin) {

    // tell them they exited staff mode.
    val title = Title.title(parseMessage(Config.staffModeExit.title), parseMessage(Config.staffModeExit.subtitle))

    showTitle(title)

    // Set booleans to false
    getSavedPlayer()?.isStaff = false
    // Set vanished to true to keep vanish on creation
    getSavedPlayer()?.isVanished = true

    // Show the player.
    showStaff(plugin)

    // Clear inventory
    inventory.clear()

    // Set gamemodes and isFlying to false
    gameMode = GameMode.SURVIVAL
    allowFlight = false
    isFlying = false

    // Get armor contents and re-set them
    val armorContents = getSavedPlayer()?.armorContents
    val contents = getSavedPlayer()?.inventoryContents

    inventory.setArmorContents(armorContents)
    inventory.contents = contents

    getSavedPlayer()?.armorContents = emptyArray()
    getSavedPlayer()?.inventoryContents = emptyArray()

    // Teleport to spawn
    val world = server.getWorld("world")!!
    val location = Location(world, world.spawnLocation.x, world.spawnLocation.y, world.spawnLocation.z, world.spawnLocation.yaw, world.spawnLocation.pitch)

    teleportAsync(location)

    Data.save(plugin)
}

fun Player.createInventory(plugin: JavaPlugin) {

    inventory.clear()

    val randomTeleport = ItemBuilder.from(Config.staffItems.randomItem.material).name(parseName(Config.staffItems.randomItem.name)).build()
    val freezeItem = ItemBuilder.from(Config.staffItems.freezeItem.material).name(parseName(Config.staffItems.freezeItem.name)).build()
    val vanishOffItem = ItemBuilder.from(Config.staffItems.vanishOffItem.material).name(parseName(Config.staffItems.vanishOffItem.name)).build()
    val vanishOnItem = ItemBuilder.from(Config.staffItems.vanishOnItem.material).name(parseName(Config.staffItems.vanishOnItem.name)).build()

    when {
        Data.players[uniqueId]?.isVanished!! -> {
            inventory.setItem(Config.staffItems.vanishOnItem.slot, vanishOnItem)
            hideStaff(plugin)
        }
        else -> {
            inventory.setItem(Config.staffItems.vanishOffItem.slot, vanishOffItem)
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

fun parseMessage(message: String): Component {
    return getMessage().parse(message)
}
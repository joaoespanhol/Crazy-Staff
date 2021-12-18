package net.savagelabs

import dev.triumphteam.core.BukkitPlugin
import dev.triumphteam.core.feature.install
import net.savagelabs.func.persist.Config
import net.savagelabs.func.persist.Settings
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class StaffX : BukkitPlugin(), Listener {

    override fun onEnable() {
        if (!dataFolder.exists()) dataFolder.mkdirs()

        install(Config)

        this.server.pluginManager.registerEvents(this, this)
    }

    override fun onDisable() {

    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent): Unit = with(e) {
        val msg = config[Settings.STAFF_ENTER_MESSAGE.toString()]
        player.sendMessage(config.get(Settings.STAFF_ENTER_MESSAGE.toString()).toString())
        //if (Config.testString.contains(player.uniqueId)) return

        //val newPlayer = User(player.uniqueId.toString(), player.name)
        //Config.testString[player.uniqueId] = newPlayer
    }
}
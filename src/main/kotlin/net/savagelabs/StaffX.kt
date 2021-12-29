package net.savagelabs

import me.mattstudios.mf.base.CommandManager
import net.savagelabs.commands.StaffCommand
import net.savagelabs.events.StaffListener
import net.savagelabs.func.persist.Config
import net.savagelabs.func.persist.Data
import net.savagelabs.func.persist.DataListener
import net.savagelabs.func.registerListener
import org.bukkit.plugin.java.JavaPlugin
import kotlinx.coroutines.*

class StaffX: JavaPlugin() {

    override fun onEnable() {
        if (!dataFolder.exists()) dataFolder.mkdirs()

        val commandManager = CommandManager(this)

        commandManager.hideTabComplete(true)
        commandManager.register(StaffCommand(this))

        Config.load(this)
        Data.load(this)

        registerListener(
            DataListener(this),
            StaffListener(this)
        )

        server.scheduler.runTaskTimerAsynchronously(this, Runnable {
            Data.save(this)
        }, 0L, 20L * 300)
    }

    override fun onDisable() {
        Config.load(this)
        Config.save(this)

        Data.save(this)
    }
}
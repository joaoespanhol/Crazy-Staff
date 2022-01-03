package me.corecraft

import io.papermc.lib.PaperLib
import me.corecraft.commands.StaffCommand
import me.corecraft.events.PaperListeners
import me.corecraft.events.StaffListener
import me.corecraft.func.MetricsHandler
import me.corecraft.func.persist.Config
import me.corecraft.func.persist.Data
import me.corecraft.func.persist.DataListener
import me.corecraft.func.registerListener
import me.corecraft.hooks.enums.registerPermissions
import me.mattstudios.mf.base.CommandManager
import org.bukkit.plugin.java.JavaPlugin

class StaffX: JavaPlugin() {

    override fun onEnable() {

        PaperLib.suggestPaper(this)

        MetricsHandler(this).connect()

        if (!dataFolder.exists()) dataFolder.mkdirs()

        val commandManager = CommandManager(this)

        commandManager.hideTabComplete(true)
        commandManager.register(StaffCommand(this))

        registerPermissions(server.pluginManager)

        Config.load(this)
        Data.load(this)

        registerListener(
            DataListener(this),
            StaffListener(this)
        )

        if (PaperLib.isPaper()) {
            logger.info("Utilizing Paper functions...")
            registerListener(PaperListeners)
        }

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
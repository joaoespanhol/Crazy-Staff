package com.badbones

import com.badbones.commands.StaffCommand
import com.badbones.events.EntityListener
import com.badbones.events.PaperListeners
import com.badbones.events.PlayerListener
import com.badbones.events.StaffListener
import com.badbones.func.MetricsHandler
import com.badbones.func.persist.Config
import com.badbones.func.persist.Data
import com.badbones.func.persist.DataListener
import com.badbones.func.registerListener
import com.badbones.hooks.enums.registerPermissions
import io.papermc.lib.PaperLib
import me.mattstudios.mf.base.CommandManager
import org.bukkit.plugin.java.JavaPlugin

class CrazyStaff: JavaPlugin() {

    override fun onEnable() {

        PaperLib.suggestPaper(this)

        if (Config.metricTracking) MetricsHandler(this).connect()

        if (!dataFolder.exists()) dataFolder.mkdirs()

        val commandManager = CommandManager(this)

        commandManager.hideTabComplete(true)
        commandManager.register(StaffCommand(this))

        registerPermissions(server.pluginManager)

        Config.load(this)
        Data.load(this)

        registerListener(
            DataListener(this),
            StaffListener(this),
            EntityListener,
            PlayerListener
        )

        if (PaperLib.isPaper()) {
            logger.info("Utilizing Paper functions...")
            registerListener(PaperListeners)
        }

        if (!Data.serverSetUp) {
            logger.warning("---------------------------------------------")
            logger.warning("You are installing the plugin for the first time!")
            logger.warning("Please when in-game, Run the commands /staff setspawn")
            logger.warning("This is to insure that Freeze works as intended.")
            logger.warning("---------------------------------------------")
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
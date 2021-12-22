package net.savagelabs

import me.mattstudios.mf.base.CommandManager
import net.savagelabs.commands.StaffCommand
import net.savagelabs.func.persist.Config
import net.savagelabs.func.persist.Data
import net.savagelabs.func.persist.DataListener
import net.savagelabs.func.registerListener
import org.bukkit.plugin.java.JavaPlugin

class StaffX: JavaPlugin() {

    override fun onEnable() {
        if (!dataFolder.exists()) dataFolder.mkdirs()

        val commandManager = CommandManager(this)

        commandManager.hideTabComplete(true)
        commandManager.register(StaffCommand(this))

        //FileManager().setup(this)

        Config.load(this)
        Data.load(this)

        registerListener(
            DataListener(this)
        )
    }

    override fun onDisable() {
        Data.save(this)
    }
}
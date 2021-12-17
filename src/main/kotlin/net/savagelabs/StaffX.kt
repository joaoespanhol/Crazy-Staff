package net.savagelabs

import net.savagelabs.func.persist.Config
import org.bukkit.plugin.java.JavaPlugin

class StaffX : JavaPlugin() {

    override fun onEnable() {
        super.onEnable()
        if (dataFolder.exists()) dataFolder.mkdirs()

        Config.load(this)
    }

    override fun onDisable() {
        super.onDisable()
        Config.load(this)
        Config.save(this)
    }
}
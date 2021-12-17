package net.savagelabs

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.savagelabs.func.persist.DefaultFile
import net.savagelabs.func.persist.Project
import net.savagelabs.func.persist.write
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class StaffX : JavaPlugin() {

    override fun onEnable() {
        println("The plugin is launching.")
        if (this.dataFolder.exists()) this.dataFolder.mkdirs()

        val data = Project("test")

        val string = Json.encodeToString(data)

        write(this, DefaultFile(false, File("config.json"), string))
        //Config.load(this)
    }

    override fun onDisable() {
        //Config.load(this)
        //Config.save(this)
    }
}
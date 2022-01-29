package com.badbones

import org.bukkit.plugin.java.JavaPlugin

class CrazyStaff: JavaPlugin() {

    override fun onEnable() {
        logger.info("Plugin is turning on.")
    }

    override fun onDisable() {
        logger.info("Plugin is turning off.")
    }
}
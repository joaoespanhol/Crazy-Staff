package com.badbones69.crazystaff

import org.bukkit.plugin.java.JavaPlugin

class CrazyStaff: JavaPlugin() {

    override fun onEnable() {
        logger.info("Plugin is turning on.")
        logger.info("Testing")
    }

    override fun onDisable() {
        logger.info("Plugin is turning off.")
    }
}
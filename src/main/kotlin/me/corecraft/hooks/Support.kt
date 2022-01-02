package me.corecraft.hooks

import org.bukkit.Bukkit

enum class Support(val value: String) {

    PLACEHOLDERAPI("PlaceholderAPI"),

    // EssentialsX / CMI
    ESSENTIALSX("EssentialsX");

    val isPluginLoaded: Boolean = Bukkit.getServer().pluginManager.getPlugin(name) != null

}
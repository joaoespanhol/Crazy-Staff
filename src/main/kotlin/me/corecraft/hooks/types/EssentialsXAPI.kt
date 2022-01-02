package me.corecraft.hooks.types

import com.earth2me.essentials.IEssentials
import me.corecraft.hooks.Support
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object EssentialsXAPI {

    private val ess = Bukkit.getPluginManager().getPlugin(Support.ESSENTIALSX.name) as IEssentials?

    fun setVanish(player: Player?) {
        ess?.getUser(player?.uniqueId)?.isVanished
        ess?.getUser(player?.uniqueId)?.isHidden
    }
}
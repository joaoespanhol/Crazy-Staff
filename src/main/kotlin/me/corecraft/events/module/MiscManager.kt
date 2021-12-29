package me.corecraft.events.module

import net.kyori.adventure.title.Title
import me.corecraft.commands.parseMessage
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

object MiscManager {
    private val random = Random()

    fun effectLightning(location: Location) {
        val x = location.blockX
        val y = location.blockY.toDouble()
        val z = location.blockZ

        for (i in 0..30) {
            val firstStrike = if (random.nextBoolean()) x + random.nextInt(6).toDouble() else x - random.nextInt(6).toDouble()
            val secondStrike = if (random.nextBoolean()) z + random.nextInt(6).toDouble() else z - random.nextInt(6).toDouble()

            val locStrike = Location(location.world, firstStrike, y, secondStrike)
            location.world.strikeLightningEffect(locStrike)
        }
    }

    fun sendTitle(player: Player?, title: String, subtitle: String) {
        val newTitle = Title.title(parseMessage(title), parseMessage(subtitle))
        return player?.showTitle(newTitle)!!
    }
}
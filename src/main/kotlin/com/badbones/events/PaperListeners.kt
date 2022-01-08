package com.badbones.events

import com.badbones.commands.getSavedPlayer
import com.destroystokyo.paper.event.entity.PhantomPreSpawnEvent
import com.destroystokyo.paper.event.entity.PlayerNaturallySpawnCreaturesEvent
import com.destroystokyo.paper.event.entity.ProjectileCollideEvent
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.spigotmc.event.entity.EntityMountEvent

object PaperListeners : Listener {

    @EventHandler
    fun onMount(e: EntityMountEvent): Unit = with(e) {
        if (mount !is Player) return
        val player = mount as Player

        if (player.getSavedPlayer()?.getVanished() == true || player.getSavedPlayer()?.getFrozen() == true) isCancelled = true
    }

    @EventHandler
    fun onNaturalSpawn(e: PlayerNaturallySpawnCreaturesEvent): Unit = with(e) {
        if (player.getSavedPlayer()?.getVanished() == true || player.getSavedPlayer()?.getFrozen() == true) isCancelled = true
    }

    @EventHandler
    fun onPhantomSpawn(e: PhantomPreSpawnEvent): Unit = with(e) {
        if (spawningEntity !is Player) return
        val player = spawningEntity as Player
        if (player.getSavedPlayer()?.getVanished() == true || player.getSavedPlayer()?.getFrozen() == true) isCancelled = true
    }

    @EventHandler
    fun onPickupExperience(e: PlayerPickupExperienceEvent): Unit = with(e) {
        if (player.getSavedPlayer()?.getVanished() == true || player.getSavedPlayer()?.getFrozen() == true) isCancelled = true
    }

    @EventHandler
    fun onProjectileCollide(e: ProjectileCollideEvent): Unit = with(e) {
        if (collidedWith !is Player) return
        val player = collidedWith as Player
        if (player.getSavedPlayer()?.getVanished() == true || player.getSavedPlayer()?.getFrozen() == true) isCancelled = true
    }
}
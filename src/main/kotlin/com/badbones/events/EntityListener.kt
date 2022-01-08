package com.badbones.events

import com.badbones.commands.getSavedPlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.vehicle.VehicleDestroyEvent
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent

object EntityListener : Listener {

    @EventHandler
    fun onVehicleCollision(e: VehicleEntityCollisionEvent): Unit = with(e) {
        if (entity !is Player) return
        val player = entity as Player

        if (player.getSavedPlayer()?.getVanished() == true) isCancelled = true
    }

    @EventHandler
    fun onVehicleDestroy(e: VehicleDestroyEvent): Unit = with(e) {
        if (attacker !is Player) return
        val player = attacker as Player

        if (player.getSavedPlayer()?.getVanished() == true) isCancelled = true
    }

    @EventHandler
    fun onEntityTarget(e: EntityTargetEvent): Unit = with(e) {
        if (entity !is Player) return
        val target = e.entity as Player

        if (target.getSavedPlayer()?.getVanished() == true) isCancelled = true
    }

    @EventHandler
    fun onEntityDamage(e: EntityDamageEvent): Unit = with(e) {
        if (entity !is Player) return
        val smacked = e.entity as Player

        if (smacked.getSavedPlayer()?.getVanished() == true || smacked.getSavedPlayer()?.getFrozen() == true) isCancelled = true

        if (e is EntityDamageByEntityEvent) {
            if (e.damager !is Player) return
            val player = e.damager as Player
            if (player.getSavedPlayer()?.getStaff() == true || player.getSavedPlayer()?.getFrozen() == true) isCancelled = true
        }
    }
}
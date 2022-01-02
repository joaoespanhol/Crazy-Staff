package me.corecraft.hooks.enums

import org.bukkit.entity.HumanEntity
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.PluginManager

enum class Permissions(private val node: String, val description: String, val permissionDefault: PermissionDefault) {

    STAFF_ITEMS_USE("staff.items.use", "Ability to use all items for StaffX", PermissionDefault.FALSE),
    VANISH_SEE("staff.vanish.see", "Ability to see other staff in vanish", PermissionDefault.FALSE),
    FREEZE_BYPASS("staff.freeze.bypass", "Ability to bypass being frozen", PermissionDefault.FALSE);

    fun getFullNode() = this.node
}

fun registerPermissions(pluginManager: PluginManager) {
    Permissions.values().forEach {
        if (pluginManager.getPermission(it.getFullNode()) == null)
            pluginManager.addPermission(
                Permission(
                    it.getFullNode(),
                    it.description,
                    it.permissionDefault
                )
            )
    }
}

fun getPermission(humanEntity: HumanEntity, permissions: Permissions) = humanEntity.hasPermission(permissions.getFullNode())
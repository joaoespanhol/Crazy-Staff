@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package me.corecraft.func

import net.md_5.bungee.api.ChatColor
import me.corecraft.StaffX
import org.bukkit.event.Listener
import java.util.regex.Matcher
import java.util.regex.Pattern


fun color(message: String?): String {
    val pattern = Pattern.compile("#[a-fA-F0-9]{6}")
    val matcher: Matcher = pattern.matcher(message)
    val buffer = StringBuffer()
    while (matcher.find()) {
        matcher.appendReplacement(buffer, ChatColor.of(matcher.group()).toString())
    }
    return org.bukkit.ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString())
}

fun colorizeList(input: List<String?>): ArrayList<String> {
    val ret = ArrayList<String>()
    input.forEach { ret.add(color(it)) }
    return ret
}

fun StaffX.registerListener(vararg listeners: Listener) = listeners.toList().forEach { server.pluginManager.registerEvents(it, this) }
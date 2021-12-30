package me.corecraft.func

import org.bstats.bukkit.Metrics
import org.bstats.charts.SingleLineChart
import org.bukkit.plugin.java.JavaPlugin

class MetricsHandler(private val plugin: JavaPlugin) {

    private val pluginID = 13767
    private val metrics = Metrics(plugin, pluginID)

    fun connect() {
        addPlayerChart()
    }

    private fun addPlayerChart() {
        metrics.addCustomChart(SingleLineChart("players") {
            plugin.server.onlinePlayers.size
        })
    }
}
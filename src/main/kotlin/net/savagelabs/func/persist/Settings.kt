package net.savagelabs.func.persist

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object Settings : SettingsHolder {

    @Path("staff-enter-message")
    val STAFF_ENTER_MESSAGE = Property.create("Welcome")

}
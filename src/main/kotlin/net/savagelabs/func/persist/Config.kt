package net.savagelabs.func.persist

import dev.triumphteam.core.TriumphApplication
import dev.triumphteam.core.configuration.BaseConfig
import dev.triumphteam.core.configuration.ConfigFeature
import dev.triumphteam.core.feature.attribute.key
import me.mattstudios.config.SettingsHolder
import java.nio.file.Path

class Config(path: Path, holder: Class<out SettingsHolder>) : BaseConfig(path, holder) {

    companion object Feature : ConfigFeature<TriumphApplication, Config, Config> {
        override val key = key<Config>("Config")

        override fun install(application: TriumphApplication, configure: Config.() -> Unit): Config {
            return Config(Path.of(application.applicationFolder.path, "config.yml"), Settings::class.java)
        }
    }
}

//data class DefaultFile(val dataEnabled: Boolean, val file: File, val contents: JsonElement)

//@Serializable
//data class User(val uuid: String, val name: String) {
//    val amount = 0
//}
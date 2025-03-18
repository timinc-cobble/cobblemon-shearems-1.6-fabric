package us.timinc.mc.cobblemon.unchained.config

import com.google.gson.GsonBuilder
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import java.io.File
import java.io.FileReader
import java.io.PrintWriter

class ConfigBuilder<T> private constructor(private val clazz: Class<T>, private val path: String) {
    companion object {
        fun <T> load(clazz: Class<T>, path: String): T {
            return ConfigBuilder(clazz, path).loadConfig()
        }
    }

    fun loadConfig(): T {
        val gson = GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create()

        var config = gson.fromJson("{}" /*default value*/, clazz)
        val configFile = File("config/$path.json")
        configFile.parentFile.mkdirs()

        if (configFile.exists()) {
            try {
                val fileReader = FileReader(configFile)
                config = gson.fromJson(fileReader, clazz)
                fileReader.close()
            } catch (e: Exception) {
                println("Error reading config file")
            }
        }

        val pw = PrintWriter(configFile)
        gson.toJson(config, pw)
        pw.close()

        return config
    }
}
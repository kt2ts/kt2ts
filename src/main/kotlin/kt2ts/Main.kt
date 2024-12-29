package kt2ts

import kt2ts.domain.Configuration
import java.nio.file.Paths

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("missing configuration file in argument")
        return
    }
    println("Configuration file : " + args[0])
    val configurationFile = Paths.get(args[0]).toFile()
    if (!configurationFile.exists()) {
        println("$configurationFile does not exist")
        return
    }
    val configuration = Configuration.load(configurationFile)
}
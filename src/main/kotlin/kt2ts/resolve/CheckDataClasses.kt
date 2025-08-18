package kt2ts.resolve

import java.nio.file.Files
import kotlin.io.path.Path

fun main() {
    val exceptions = setOf(
        "commands.kt",
        "Commands.kt",
        "TestOnboardingCommands.kt",

        "WebInternalCommand.kt",
        "ApiInternalCommand.kt",
        "PricingInternalCommand.kt",
        "ProInternalCommand.kt",
        "PartnerInternalCommand.kt",

        "queries.kt",
        "Queries.kt",
        "OnboardingQueries.kt",

        "BrevoService.kt",
        "MailJetService.kt",
        "MailjetService.kt",
        "SendGridService.kt",

        "EnedisM023InfosTechniquesEtContractuellesImportService.kt",

        "FormerHttpService.kt",

        "IntRangeDeserializerTest.kt",
        "OpenEndRangeSerializerTest.kt",
        "OpenEndRangeDeserializerTest.kt",
        "PairSerializerTest.kt",
        "SerializerTest.kt",
    )
    KotlinSourceFilesResolver.sequenceKotlinFiles(Path("/Users/mlo/git/lite")).forEach { file ->
        val content = Files.readString(file.toPath())
        val c = content.replace("\n", "")
            .replace("data class ", "\ndata class ")
         Regex("""data class ([a-zA-Z0-9_.-]*)\(val ([^,]*)\)""").find(c)?.groupValues?.forEach {
             if(file.name in exceptions) {
                 return@forEach
             }
             println(file)
//             println(c)
//             println("----")
//             println(it)
         }
    }
}
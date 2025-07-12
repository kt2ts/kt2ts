package kt2ts

import io.github.treesitter.ktreesitter.Language
import io.github.treesitter.ktreesitter.Parser
import org.treesitter.TreeSitterKotlin

fun main() {
    val language = Language(TreeSitterKotlin.language())
    val parser = Parser(language)
    val tree = parser.parse("fun main() {}")
    val rootNode = tree.rootNode

    assert(rootNode.type == "source_file")
    println(rootNode.startPoint.column)
    println(rootNode.endPoint.column)
}
package de.tautenhahn.catch

import java.util.regex.Matcher
import java.util.regex.Pattern

	fun main(args: Array<String>) {
		val data = " irgendwas } catch ( DummyException e) { {}{{{}}} {}}"
		val instance = BlockFinder();
		println(instance.captureBlock(data, 0, '{', '}'))
	}

class BlockFinder {
	private val CATCH = "catch *\\( *\\w+( *\\| *\\w+)* (\\w+) *\\)"
	private val BLOCK = "\\{([^\\{\\}]*(\\{[^\\{\\}]*\\}[^\\{\\}]*)*)\\}"
	internal var catchBlock = Pattern.compile(CATCH)
	// static final Pattern LOGGED=
	// Pattern.compile("\\w*(?i:log)\\w*.(debug|trace|info|error|warn|fatal)\([^)]")
	

	/**
	 * Due to recursion, regular expressions are not usable to safely extract block contents.
	 */
	internal fun captureBlock(source: String, start: Int, opening: Char, closing: Char): String {
		var level = 0
		var pos = start
		while (pos < source.length && source.get(pos) != opening) {
			pos++
		}
		level++
		var endPos = pos + 1
		while (endPos < source.length && level > 0) {
			if (source.get(pos) == opening) {
				level++
			} else if (source.get(pos) == closing) {
				level--
			}
			endPos++
		}
		return source.substring(pos + 1, endPos - 1)
	}
}
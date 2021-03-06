package de.tautenhahn.catch

import java.util.regex.Matcher
import java.util.regex.Pattern
import java.nio.file.Path

/**
Finds catch blocks in source codes of supported languages.
@author TT
 */
class BlockFinder(language: Language) {
	val catchPattern: Pattern

	init {
		when (language) {
			Language.JAVA, Language.CSHARP, Language.CPP -> catchPattern = Pattern.compile("\\bcatch *\\( *\\w+( *\\| *\\w+)* (\\w+) *\\)")
			Language.KOTLIN -> catchPattern = Pattern.compile("\\bcatch *\\(( *)(\\w+) *: *\\w+ *\\)")
			// note that there is a compiler warning on the Java-like default (here else) case, missing
			// cases are detected by the compiler. What happens when somebody keeps this compiled class but extends Language? 
		}
	}

	private val callPattern = Pattern.compile("(^|\\s+)(throw\\s+)?(new\\s+)?(\\w+\\s*\\.)*\\s*\\w+\\s*\\(", Pattern.DOTALL)
	private val logPattern = Pattern.compile("\\s*LOG\\s*\\.\\s*(trace|debug|info|warn|error|fatal).*", Pattern.DOTALL);
	private val throwPattern = Pattern.compile("\\s*throw\\s+.*", Pattern.DOTALL);

	/**
	Returns a list of catch blocks found in the source code
	 */
	fun findCatchBlocks(source: String, path: Path): List<CatchBlock> {
		val result: MutableList<CatchBlock> = mutableListOf()
		val m: Matcher = catchPattern.matcher(source)
		var line = 1;
		var lastPos = 0;
		while (m.find()) {
			val varName = m.group(2)
			val content = captureBlock(source, m.end(), '{', '}')
			for (i in lastPos..m.start()) {
				if (source.get(i) == '\n') {
					line++
				}
			}
			lastPos = m.end();
			result.add(CatchBlock(Location(path, line), varName, content))
		}
		return result;
	}

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
			if (source.get(endPos) == opening) {
				level++
			} else if (source.get(endPos) == closing) {
				level--
			}
			endPos++
		}
		return source.substring(pos + 1, endPos - 1)
	}

	/*
 Returns the counts of usage of the exception parameter. Assumes that the exception is last parameter for calls.
 */
	fun analyze(block: CatchBlock): ExUsage {
		val paramPattern = Pattern.compile(".*[,\\s\\(]" + block.varName + "\\s*\\)", Pattern.DOTALL)
		val plainThrowPattern = Pattern.compile("(^|\\s+)throw\\s+" + block.varName + "($|\\s|;)", Pattern.DOTALL)
		val m = callPattern.matcher(block.content)
		var calls: MutableList<String> = mutableListOf();
		while (m.find()) {
			calls.add(m.group(0) + captureBlock(block.content, m.end() - 1, '(', ')') + ')');
		}
		var logged = 0;
		var thrown = 0;
		var forwarded = 0;

		for (line in calls) {
			if (paramPattern.matcher(line).matches()) {
				if (logPattern.matcher(line).matches()) {
					logged++
				} else if (throwPattern.matcher(line).matches()) {
					thrown++
				} else {
					forwarded++
				}
			}
		}
		val pm = plainThrowPattern.matcher(block.content)
		while (pm.find()) {
			thrown++;
		}

		return ExUsage(block, logged, thrown, forwarded)
	}

}
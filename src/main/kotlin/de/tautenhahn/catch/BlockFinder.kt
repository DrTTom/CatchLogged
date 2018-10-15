package de.tautenhahn.catch

import java.util.regex.Matcher
import java.util.regex.Pattern

	fun main(args: Array<String>) {
		val data = " irgendwas } catch ( DummyException e) { {}{{{}}} {}}"
		val instance = BlockFinder(Language.JAVA)
		println(instance.captureBlock(data, 0, '{', '}'))
}

class BlockFinder (language: Language) {
  val catchPattern: Pattern	
	init{
		when(language)
		{
			Language.JAVA, Language.C, Language.CPP -> catchPattern=Pattern.compile("\\bcatch *\\( *\\w+( *\\| *\\w+)* (\\w+) *\\)")
			Language.KOTLIN -> catchPattern=Pattern.compile("\\bcatch *\\(( *)(\\w+) *: *\\w+* \\)")
			// note that there is a compiler warning on the Java-like default (here else) case, missing
			// cases are detected by the compiler. What happens when somebody keeps this compiled class but extends Language? 
		}
	}
	
	private val javaCppCatch = Pattern.compile("\\bcatch *\\( *\\w+( *\\| *\\w+)* (\\w+) *\\)");

  fun findCatchBlocks(source: String): List<CatchBlock>
	{
		val result: MutableList<CatchBlock> = mutableListOf()
		val m: Matcher = catchPattern.matcher(source)
		var line=1;
		var lastPos=0;
		while (m.find())
			{
				val varName = m.group(2)
				val content = captureBlock(source, m.end(), '{', '}')
				for (i in lastPos..m.start())
					{
						if (source.get(i) == '\n')
							{
								line++
							}
					}
				lastPos=m.end();
				result.add(CatchBlock(line, varName, content))
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
}
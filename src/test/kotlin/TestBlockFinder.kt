package de.tautenhahn.catch

import org.junit.Test;
import org.junit.Assert.assertThat;
import org.hamcrest.Matchers.equalTo;
import org.hamcrest.Matchers.hasSize;

/**
Unit tests for finding catch blocks in different languages
 */
class TestBlockFinder() {

	/**
	Finds catch blocks inside a Java-like source code and checks result
	 */
	@Test
	fun findInJava() {
		val blocks = listOf("e.printStackTrace();", " throw t; ")
		val source = "...} \n catch ( IOException e) \n {" + blocks.get(0) +
				"}\n catch (RuntimeException|OutOfMemoryError t) {" + blocks.get(1) + "} ... "
		val systemUnderTest = BlockFinder(Language.JAVA)
		val findings: List<CatchBlock> = systemUnderTest.findCatchBlocks(source)

		assertThat("findings", findings, hasSize(2))
		assertThat("varName", findings.get(0).varName, equalTo("e"))
		assertThat("content", findings.get(0).content, equalTo(blocks.get(0)))
		assertThat("line", findings.get(0).line, equalTo(2))

		assertThat("varName", findings.get(1).varName, equalTo("t"))
		assertThat("content", findings.get(1).content, equalTo(blocks.get(1)))
		assertThat("line", findings.get(1).line, equalTo(4))
	}

	/**
	Makes sure variable name is found in Kotlin as well (all other supperted languages tested like Java)
	 */
	fun findInKotlin() {
		val source = "... catch ( exc: Exception) { // nothing }"
		val systemUnderTest = BlockFinder(Language.KOTLIN)
		val found: CatchBlock = systemUnderTest.findCatchBlocks(source).get(0);

		assertThat("varName", found.varName, equalTo("exc"))
		assertThat("content", found.content, equalTo(" // nothing "))
		assertThat("line", found.line, equalTo(1))

	}

	/**
	simple Test for capturing blocks using nested blocks.
	 */
	@Test
	fun findBlocks() {
		val javaData = "... catch (IOException | IllegalArgumentException e) { if (a==b) { something();} LOG.debug(\"ex\", e);}";
		val systemUnderTest = BlockFinder(Language.JAVA);
		assertThat("block", systemUnderTest.captureBlock(javaData, 0, '{', '}'), equalTo(" if (a==b) { something();} LOG.debug(\"ex\", e);"));
	}

	/**
	Counts the usages of the exception
	 */
	@Test
	fun countCalls() {
		val block = CatchBlock(0, "e", " LOG.debug\n(\"caugth\", e); LOG.info(\"huhu\"); \nthrow new RuntimeException(e); \n  Utils.handle(e);")
		val systemUnderTest = BlockFinder(Language.KOTLIN)
		val counts = systemUnderTest.analyze(block);
		assertThat("logged count", counts.logged, equalTo(1));
		assertThat("throw count", counts.reThrown, equalTo(1));
		assertThat("call count", counts.forwarded, equalTo(1));
	}
}
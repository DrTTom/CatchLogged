package de.tautenhahn.catch

import org.junit.Test;
import org.junit.Assert.assertThat;
import org.hamcrest.Matchers.equalTo;

/**
Unit tests for finding catch blocks in different languages
 */
class TestBlockFinder() {
	@Test
	fun findBlocks() {
		// Covers C++ case close enough for our purpose:
		val javaData = "... catch (IOException | IllegalArgumentException e) { if (a==b) { something();} LOG.debug(\"ex\", e);}";
		// there seems to be no multi-catch here?
		val kotlinData = "... catch (e : SomeException) { something();} LOG.debug(\"ex\", e);}";

		val systemUnderTest = BlockFinder();
		assertThat("block", systemUnderTest.captureBlock(javaData, 0, '{', '}'), equalTo(" if (a==b) { something();} LOG.debug(\"ex\", e);"));

	}
}
package de.tautenhahn.catch

import org.junit.Test;
import org.junit.Assert.assertThat;
import org.hamcrest.Matchers.equalTo;
import org.hamcrest.Matchers.hasSize;
import java.nio.file.Paths;

/**
Unit tests for creaating a report.
 */
class TestCatchAndLogReporter() {

	/**
	Greates report for own GuineaPig class.
	 */
	@Test
	fun checkOwnProject() {
		println(CatchAndLogReporter(Paths.get(".")).analyzeProject())
		assertThat("just failing", "x", equalTo("y"))
	}

}
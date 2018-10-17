package de.tautenhahn.catch
import java.nio.file.Path;

/**
 * wraps a warning message together with 
*/
data class Location(val source: Path, val startLine: Int)
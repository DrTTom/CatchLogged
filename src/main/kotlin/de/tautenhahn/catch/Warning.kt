package de.tautenhahn.catch

/**
 * wraps a warning message together with the source location
*/
data class Warning(val source:Location, val message: String)
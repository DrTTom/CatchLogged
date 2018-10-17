package de.tautenhahn.catch

/**
Represents a found catch block. Just a data class. Everything is implicit.
 */
data class CatchBlock(val source: Location, val varName: String, val content: String)
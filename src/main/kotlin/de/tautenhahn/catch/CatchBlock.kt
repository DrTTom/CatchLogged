package de.tautenhahn.catch

/**
Represents a found catch block. Just a data class. Everything is implicit.
 */
data class CatchBlock(val line: Int, val varName: String, val content: String) 
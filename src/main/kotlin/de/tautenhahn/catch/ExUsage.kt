package de.tautenhahn.catch

/**
Counts what an excetion is used for.
 */
data class ExUsage(val logged: Int, val reThrown: Int, val forwarded: Int) 
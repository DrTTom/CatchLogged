package de.tautenhahn.catch

import java.util.regex.Pattern
import java.nio.file.Paths
import java.nio.file.Path
import java.nio.file.Files
import java.util.stream.Stream
import java.util.stream.Collectors

/**
Prints a report about project specified by root path to standard output.
 */
fun main(args: Array<String>) {
	var root = ".";
	if (args.any()) {
		root = args.get(0);
	}
	val path = Paths.get(root)
	CatchAndLogReporter(path).analyzeProject().forEach({ w -> println(path.relativize(w.source.source).toString()+", line "+ w.source.startLine+": "+w.message) })
}

/**
Analyzes a project for unusual catch and log behaviour
 */
class CatchAndLogReporter(projectRoot: Path) {

	val root = projectRoot

	private val IGNORE_PATTERN = Pattern.compile("// (\\w+ )?(not logged|handled twice) because \\w+")

	/**
	 Returns a list of warnings found for that project
	 */
	fun analyzeProject(): List<Warning> {
		return Files.walk(root).filter({ p -> Files.isRegularFile(p) })
				.filter({ p -> !p.toString().contains("/build/") }).parallel()
				.flatMap({ p -> checkFile(p) }).collect(Collectors.toList());
	}

	internal fun checkFile(p: Path): Stream<Warning> {
		val name = p.toString()
		var lan: Language?
		if (name.endsWith(".java")) {
			lan = Language.JAVA;
		} else {
			val findings: List<Warning> = listOf()
			return findings.stream();
		}
		return checkFile(BlockFinder(lan), p.toFile().readText(), p)
	}

	internal fun checkFile(finder: BlockFinder, content: String, p: Path): Stream<Warning> {
		return finder.findCatchBlocks(content, p).stream().map({ b -> assign(finder.analyze(b)) }).filter({ m -> m != null }).map({ x -> x as Warning })
	}

	internal fun assign(findings: ExUsage): Warning? {
		val totalUses = findings.logged + findings.reThrown + findings.forwarded
		if (totalUses == 1 || IGNORE_PATTERN.matcher(findings.block.content).find()) {
			return null
		}
		var msg: String
		if (totalUses == 0) {
			msg = "Exceptional information lost"
		} else {
			msg = "Exceptional info used several times (" + findings.logged + " x logged, " +
					findings.reThrown + " x thrown, " +
					findings.forwarded + " x forwarded)"
		}
		return Warning(findings.block.source, msg);
	}
}
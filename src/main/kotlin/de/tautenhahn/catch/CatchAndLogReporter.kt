package de.tautenhahn.catch

import java.util.regex.Pattern
import java.util.regex.Matcher
import java.nio.file.Paths
import java.nio.file.Path
import java.nio.file.Files
import java.util.stream.Stream
import java.util.stream.StreamSupport
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
	private val DECLARE_PATTERN = Pattern.compile("\\s(\\w+) = LoggerFactory.getLogger\\(([^)]+)\\)")

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
		val blockMessages = finder.findCatchBlocks(content, p).stream().map({ b -> assign(finder.analyze(b)) })
		val categoryMessages = Stream.of(checkLogCategory(content, p)) 
		return Stream.concat(blockMessages, categoryMessages).filter({ m -> m != null }).map({ x -> x as Warning })
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
	
	internal fun checkLogCategory(content: String, p : Path) : Warning?
	{
		// TODO: make log framework configurable
		val m : Matcher = DECLARE_PATTERN.matcher(content);
		if (m.find())
			{
				// TODO: could use logger name to make further analysis more flexible
				val arg = m.group(2);
				if (arg.endsWith(".class") && !arg.endsWith(p.getFileName().toString().replace(".java", ".class")))
					{
						return Warning(Location(p, -1), "potentially wrong category "+arg+" expected "+p.getFileName().toString()+".class");
					}
			}
		return null;
	}
}
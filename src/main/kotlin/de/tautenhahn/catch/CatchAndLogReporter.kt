package de.tautenhahn.catch

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.util.stream.Collectors;

fun main(args: Array<String>) {
	println(CatchAndLogReporter(Paths.get(args.get(0))).analyzeProject())
}

class CatchAndLogReporter(projectRoot: Path) {
	val root = projectRoot
 fun analyzeProject(): List<String>
 {
 	return Files.walk(root).filter( {p-> Files.isRegularFile(p)})
 	.flatMap({p -> checkFile(p)}).collect(Collectors.toList());
 }
 
 fun checkFile(p: Path): Stream<String>
 {
 	val  name = p.toString()
 	var lan : Language? 
	if (name.endsWith(".java"))
	{
		lan=Language.JAVA;
	}
	else
	{
	val findings : List<String> = listOf();
 	return findings.stream();
	}
	
	
	return checkFile(BlockFinder(lan), p.toFile().readText(), p);
 }
 fun checkFile(finder: BlockFinder, content: String, p: Path): Stream<String> {
 	return finder.findCatchBlocks(content).stream().map({b-> assign(finder.analyze(b), b.line, p)}).filter({m -> m!=null}).map({x-> x as String})
 }
 
 fun assign(findings: ExUsage, line: Int, p:Path) : String?
 {
 	return "TODO for "+findings+line+ p;
 }
}

package webcrafttools.js.closure.task


import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

import webcrafttools.js.closure.CompilerLevel

import com.google.javascript.jscomp.CompilationLevel
import com.google.javascript.jscomp.Compiler
import com.google.javascript.jscomp.CompilerOptions
import com.google.javascript.jscomp.JSError
import com.google.javascript.jscomp.Result
import com.google.javascript.jscomp.SourceFile

class ClosureCompileTask extends SourceTask {


	@Input
	def compileLevel = CompilerLevel.ADVANCED_OPTIMIZATIONS

	@Optional
	@Input
	def entryPoint
	@OutputFile
	def dest
	@Optional
	@OutputFile
	def sourceMap

	File getDest() {
		project.file(dest)
	}

	@TaskAction
	def run() {


		Compiler compiler = new Compiler()
		CompilerOptions options = new CompilerOptions()
		// Advanced mode is used here, but additional options could be set, too.
		CompilationLevel.ADVANCED_OPTIMIZATIONS.setOptionsForCompilationLevel(
				options)

		if(entryPoint) {

			println "setting $entryPoint"
			options.dependencyOptions.entryPoints << entryPoint
			options.dependencyOptions.dependencyPruning = true
			options.dependencyOptions.dependencySorting = true
			options.dependencyOptions.moocherDropping = true

		}
		def jsSources = source.collect({SourceFile.fromFile(it)})


		Result result = compiler.compile([], jsSources, options)

		result.warnings.each {JSError warn -> println warn }
		result.errors.each {JSError warn -> println warn }
		// The compiler is responsible for generating the compiled code; it is not
		// accessible via the Result.
		compiler.toSource()


		getDest().text = compiler.toSource()
	}
}

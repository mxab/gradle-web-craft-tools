
package webcrafttools.js.closure.task


import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*

import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

import webcrafttools.js.closure.CompilerLevel
import webcrafttools.js.closure.DepsParser

class RemoteClosureCompileTask extends SourceTask {


	@Input
	def compileLevel = CompilerLevel.ADVANCED_OPTIMIZATIONS

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

		def fileInfos = new DepsParser().parse(source)

		def http = new HTTPBuilder('http://closure-compiler.appspot.com')
		//
		//// perform a GET request, expecting JSON response data
		def postBody = [
			js_code : fileInfos.collect({it.file.text}),
			compilation_level : compileLevel,
			output_format : "json",
			output_info : [
				"compiled_code",
				"statistics",
				"errors",
				"warnings"]
		] // will be url-encoded

		http.post(path: '/compile', body: postBody, contentType: JSON,
		requestContentType: URLENC) {response, json->


			logger.info "Closure Compiler Service Status Line {}", response.statusLine
			println json
			if(json.errors) {

				logger.error("There where {} errors in your code!", json.errors.size())
				json.errors.each { error ->
					println error
					def fileNr = error.file-"Input_" as int
					logger.error "Error: {} ({})", error.error, error.type
					logger.error "{}:{}:{}", fileInfos[fileNr].file,error.lineno,error.charno
					logger.error "Line: {}", error.line
					logger.error error.line.trim()
					logger.error ("-"*(error.charno) +"^")
				}

			}
			if(json.warnings) {

				logger.warn("There where {} warnings in your code!", json.warnings.size())
				json.warnings.each { warning ->

					def fileNr = warning.file-"Input_" as int
					logger.warn "Warning: {} ({})", warning.warning, warning.type
					logger.warn "{}:{}:{}", fileInfos[fileNr].file,warning.lineno,warning.charno
					logger.warn warning.line.trim()
					logger.warn ("-"*(warning.charno) +"^")

				}
			}
			json.statistics.each {k,v ->
				logger.info "$k: $v"
			}
			if(json.compiledCode) {
				getDest().text = json.compiledCode
			}else {
				throw new GradleException("No compiled code available")
			}
		}

	}
}

package webcrafttools.development.server.closure

import org.gradle.api.Project
import org.gradle.api.artifacts.result.ResolvedComponentResult

import webcrafttools.js.closure.FileInfo

class ClosureJSPathUtil {


	final static def PROJECT = ~/\/JS\/PROJECT_COMPONENT\/([^\/\s]*)\/(\w+)\/(.*)/
	def static matchProjectJS(path) {
		path =~ PROJECT
	}

	def static projectJSDepsPath(Project project, String sourceSetName, File sourceDir, FileInfo fileInfo) {
		def path = fileInfo.file.path - sourceDir.path - "/"
		return "JS/PROJECT_COMPONENT/$project.path/$sourceSetName/$path"
	}



	final static def JS_MODULE_COMPONENT = ~/\/JS\/MODULE_COMPONENT\/([\w\d\.\-]+)\/([\w\d\.\-]+)\/([\w\d\.\-]+)\/(.*)/
	def static matchModuleJS(path) {
		path =~ JS_MODULE_COMPONENT
	}
	def static moduleJSDepsPath(ResolvedComponentResult it,String pathInRepo, FileInfo fileInfo) {
		def path = fileInfo.file.path - pathInRepo - "/"
		"JS/MODULE_COMPONENT/$it.moduleVersion.group/$it.moduleVersion.name/$it.moduleVersion.version/$path"
	}
}

package webcrafttools.development.server.closure

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.component.ComponentIdentifier
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.artifacts.component.ProjectComponentIdentifier
import org.gradle.api.artifacts.result.ResolvedComponentResult
import org.gradle.api.artifacts.result.ResolvedDependencyResult
import org.gradle.api.tasks.SourceSet
import org.slf4j.LoggerFactory

import webcrafttools.CacheUtil
import webcrafttools.content.WebPackContent
import webcrafttools.content.WebPackContentResolveService
import webcrafttools.development.server.files.FilePathBuilder
import webcrafttools.source.WebSourceSet
class DevelopmentHandler extends AbstractHandler {


	final def JS = { location -> """
document.write('<script src="$location"></script>');
""" }
	final def CSS = { location -> """
document.write('<link href="$location" rel="stylesheet">');
""" }
	final def ENTRY_POINT = { entryPoint -> """
document.write('<script>goog.require("$entryPoint")</script>');
""" }
	final def CLOSURE_SETTINGS = { """CLOSURE_BASE_PATH="/";
""" }
	//final def CSS = """<link href="$location" rel="stylesheet">"""

	def logger = LoggerFactory.getLogger(DevelopmentHandler)


	Configuration configuration

	Project project
	WebPackContentResolveService webPackContentResolveService = new WebPackContentResolveService([project:project])


	FilePathBuilder builder = new FilePathBuilder([project:project])

	String sourceSetName = SourceSet.MAIN_SOURCE_SET_NAME
	@Override
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {

		println "Checking $target"

		if("/DEVELOPMENT" == target) {


			response.setContentType('application/javascript')
			response.writer << CLOSURE_SETTINGS.call()
			ResolvedComponentResult rootDependency = configuration.incoming.resolutionResult.root


			def recursiveAdd
			def list = [] as Set
			recursiveAdd = { ResolvedComponentResult resolved ->

				println "checking $resolved"
				resolved.dependencies.grep({it instanceof ResolvedDependencyResult})
				.collect({ResolvedDependencyResult it -> it.selected})
				.each(recursiveAdd)
				list << resolved
			}
			recursiveAdd.call(rootDependency)

			def toId = { ResolvedComponentResult it ->  it.id }


			list.collect(toId).each { ComponentIdentifier cId ->

				println "writing paths for $cId"
				WebPackContent content = webPackContentResolveService.from(cId)
				if(cId instanceof ProjectComponentIdentifier) {
					//builder.staticsPathFor(it,
					Project p =project.findProject(cId.projectPath)

					WebSourceSet wss = p.extensions.webcrafttools.source[sourceSetName]

					def allStatics = wss.statics.files

					content.staticsDirectory.unique().sort().eachWithIndex {dir, index->


						println "[$index] $dir"
						project.fileTree(dir).each { file ->
							if(allStatics.contains(file)) {
								def path  = builder.staticsPathFor(cId,wss,dir, index, file)
								writePath(response, path)
							}

						}

					}
				}else if(cId instanceof ModuleComponentIdentifier){

					def dir =project.file( CacheUtil.staticsPathInCache(cid))
					project.fileTree(dir).each {

						def path = builder.staticsPathFor(cid, it)

						writePath(response, path)
					}

				}


			}

			if(baseRequest.parameterMap["entryPoint"]) {
				response.writer << ENTRY_POINT.call(baseRequest.parameterMap["entryPoint"].first())
			}

			baseRequest.handled = true
		}


	}
	def writePath(HttpServletResponse response, String path) {
		if(path.endsWith(".js")) {

			response.writer << JS.call(path)
		}else if(path.endsWith(".css")){

			response.writer <<CSS.call(path)
		}
	}
}

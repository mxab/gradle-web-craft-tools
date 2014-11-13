package webcrafttools.development.server

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.HandlerList
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import webcrafttools.content.WebPackContentResolveService
import webcrafttools.development.server.closure.DepsJSHandler
import webcrafttools.development.server.closure.DevelopmentHandler
import webcrafttools.development.server.files.FilesHandler
import webcrafttools.source.WebSourceSet


/**
 * Created by bruchmann on 23/10/14.
 */
class DevelopmentServer {

	WebSourceSet sourceSet
	Project project
	int port = 10101

	def js = "app.min.js"

	Configuration configuration



	private static final Logger logger = LoggerFactory.getLogger(DevelopmentServer)

	public DevelopmentServer(){
	}
	void start() {

		WebPackContentResolveService webPackContentResolveService = new WebPackContentResolveService([project:project])

		Server server = new Server(port)

		HandlerList handlerList = new HandlerList()

		def handlers = []


		handlers << new FilesHandler([project:project,webPackContentResolveService:webPackContentResolveService])


		def developmentHandler = new DevelopmentHandler([project:project,webPackContentResolveService:webPackContentResolveService, configuration:configuration])
		handlers << developmentHandler

		def depsJSHandler = new DepsJSHandler([project:project, configuration:configuration])
		handlers << depsJSHandler



		handlerList.handlers = handlers
		server.handler = handlerList
		server.start()

		logger.info('Started Development Server at {}', server.URI)
		server.join()
	}
}

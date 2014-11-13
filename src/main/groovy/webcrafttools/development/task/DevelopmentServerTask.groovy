package webcrafttools.development.task

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.TaskAction

import webcrafttools.development.server.DevelopmentServer
import webcrafttools.source.WebSourceSet

/**
 * Created by bruchmann on 23/10/14.
 */
class DevelopmentServerTask extends DefaultTask{


	WebSourceSet sourceSet
	Configuration configuration
	@TaskAction
	def start(){
		def server = new DevelopmentServer([project:project, sourceSet: sourceSet, configuration:configuration])



		server.start()
	}
}

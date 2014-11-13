
package webcrafttools.plugin

import javax.inject.Inject

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.internal.artifacts.publish.ArchivePublishArtifact
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.plugins.DefaultArtifactPublicationSet
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.internal.reflect.Instantiator

import webcrafttools.EnsureExtractedArtifactsTask
import webcrafttools.WebCraftLibrary
import webcrafttools.bundling.task.WebPack
import webcrafttools.css.sass.plugin.SassPlugin
import webcrafttools.development.task.DevelopmentServerTask
import webcrafttools.js.closure.ClosureCompilerPlugin
import webcrafttools.source.WebSourceSet
import webcrafttools.source.WebSourceSetContainer

class WebCraftPlugin implements Plugin<Project> {

	public static final String CONFIGURATION_NAME = "web"
	private final Instantiator instantiator
	private final FileResolver fileResolver



	private final static String CLOSURE_BASE_JS_DEPENDENCY = "closure-base"



	@Inject
	public WebCraftPlugin(Instantiator instantiator, FileResolver fileResolver) {
		this.instantiator = instantiator
		this.fileResolver = fileResolver
	}

	void apply(final Project project) {
		project.apply plugin:WebCraftBasePlugin
		project.apply plugin:ClosureCompilerPlugin
		project.apply plugin:SassPlugin


		configureSourceSets(project)

		configureArchivesAndComponent(project)
		applyTasks(project)
	}

	void configureSourceSets(project) {
		createSourceSet(project, SourceSet.MAIN_SOURCE_SET_NAME)
		createSourceSet(project, SourceSet.TEST_SOURCE_SET_NAME)
	}
	WebSourceSet createSourceSet(Project project, name) {
		WebSourceSetContainer container = project.extensions.webcrafttools.source
		WebSourceSet main = container.create(name)

		main
	}
	void applyTasks(final Project project) {

		def compiledJSName = "${project.name}.js"

		def config = project.configurations.getByName(CONFIGURATION_NAME)
		project.task("ensureExtractedArtifacts", type: EnsureExtractedArtifactsTask ){ configuration  = config }

		project.task('server', type: DevelopmentServerTask, group: 'Development', description: 'Servers all required dependencies') {
			sourceSet = getMainSourceSet(project)
			configuration = config
			dependsOn "ensureExtractedArtifacts"
		}
	}


	private void configureArchivesAndComponent(final Project project) {



		WebPack pack = project.tasks.create("pack", WebPack)
		pack.setGroup(BasePlugin.BUILD_GROUP)

		pack.setDescription("Assembles a web craft archive containing the main sources.")

		WebSourceSet mainSourceSet = getMainSourceSet(project)

		pack.js{ from mainSourceSet.js }
		pack.statics { from mainSourceSet.statics }
		pack.scss  { from mainSourceSet.scss }

		ArchivePublishArtifact artifact = new ArchivePublishArtifact(pack)
		Configuration configuration = project.configurations.getByName(CONFIGURATION_NAME)

		configuration.artifacts.add(artifact)
		project.extensions.getByType(DefaultArtifactPublicationSet).addCandidate(artifact)
		project.components.add(new WebCraftLibrary(artifact, configuration.allDependencies))
	}
	def getMainSourceSet(Project project) {
		project.extensions.webcrafttools.source.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
	}
}

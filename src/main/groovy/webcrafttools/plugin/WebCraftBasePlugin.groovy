
package webcrafttools.plugin

import javax.inject.Inject

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.plugins.BasePlugin
import org.gradle.internal.reflect.Instantiator

import webcrafttools.Constants
import webcrafttools.WebCraftToolsExtension
import webcrafttools.WebDependencyFileCollection
import webcrafttools.content.WebPackContentResolveService
import webcrafttools.source.WebSourceSet

class WebCraftBasePlugin implements Plugin<Project> {

	private final Instantiator instantiator
	private final FileResolver fileResolver

	final static JS_FOLDER_NAME = Constants.SRC_TYPE_JS
	final static STATICS_FOLDER_NAME = Constants.SRC_TYPE_STATICS
	final static SCSS_FOLDER_NAME = Constants.SRC_TYPE_SCSS

	WebPackContentResolveService webPackContentResolveService
	@Inject
	public WebCraftBasePlugin(Instantiator instantiator, FileResolver fileResolver) {
		this.instantiator = instantiator
		this.fileResolver = fileResolver
	}

	void apply(final Project project) {


		project.plugins.apply(BasePlugin)
		webPackContentResolveService = new WebPackContentResolveService([project:project])
		WebCraftToolsExtension ext = project.extensions.create(WebCraftToolsExtension.NAME, WebCraftToolsExtension, project, instantiator, fileResolver)

		ext.source.all { WebSourceSet sourceSet ->

			def sourceSetConfigurationName = sourceSet.configurationName
			def taskBaseName = sourceSet.taskBaseName
			def config = project.configurations.findByName(sourceSetConfigurationName)

			if(!config) {
				config = project.configurations.create(sourceSetConfigurationName)
			}
			sourceSet.js.srcDir "src/$sourceSet.name/$JS_FOLDER_NAME"
			sourceSet.statics.srcDir "src/$sourceSet.name/$STATICS_FOLDER_NAME"
			sourceSet.scss.srcDir "src/$sourceSet.name/$SCSS_FOLDER_NAME"
			sourceSet.JSPath = new WebDependencyFileCollection(config, project)
		}
	}
}

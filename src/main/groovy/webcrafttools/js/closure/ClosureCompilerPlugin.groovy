package webcrafttools.js.closure

import javax.inject.Inject

import org.apache.commons.lang.StringUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.reflect.Instantiator

import webcrafttools.WebCraftToolsExtension
import webcrafttools.js.closure.task.ClosureCompileTask
import webcrafttools.source.WebSourceSet

class ClosureCompilerPlugin implements Plugin<Project>{
	private Instantiator instantiator
	private FileResolver fileResolver
	@Inject
	public ClosureCompilerPlugin(Instantiator instantiator, FileResolver fileResolver) {
		this.instantiator = instantiator
		this.fileResolver = fileResolver
	}
	@Override
	public void apply(Project project) {

		WebCraftToolsExtension ext = project.extensions.findByType(WebCraftToolsExtension)
		ext.source.all { WebSourceSet sourceSet ->

			def sourceSetConfigurationName = sourceSet.configurationName

			def config = project.configurations.findByName(sourceSetConfigurationName)


			project.task(StringUtils.uncapitalize("${taskBaseName}ClosureCompileJS"),type: ClosureCompileTask){
				source  sourceSet.JSPath
				sourceSet.js.srcDirs.each { source it }

				dest = "$project.buildDir/compiled/$sourceSetConfigurationName/app.js"
			}
		}
	}
}

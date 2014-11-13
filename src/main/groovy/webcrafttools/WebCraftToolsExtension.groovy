package webcrafttools


import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.reflect.Instantiator
import org.gradle.util.ConfigureUtil

import webcrafttools.source.WebSourceSetContainer
import webcrafttools.source.internal.DefaultWebSourceSetContainer

class WebCraftToolsExtension {

	public static final NAME = "webcrafttools"

	final WebSourceSetContainer source

	WebCraftToolsExtension(Project project, Instantiator instantiator, FileResolver fileResolver) {
		source = instantiator.newInstance(DefaultWebSourceSetContainer, project, instantiator, fileResolver)
	}

	void source(Closure closure) {
		ConfigureUtil.configure(closure, source)
	}

	void manifest(Closure closure) {
		ConfigureUtil.configure(closure, source)
	}
}

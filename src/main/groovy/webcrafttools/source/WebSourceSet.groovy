package webcrafttools.source

import org.gradle.api.Action
import org.gradle.api.Named
import org.gradle.api.file.FileCollection
import org.gradle.api.file.SourceDirectorySet
import org.gradle.util.Configurable

interface WebSourceSet extends Named, Configurable<WebSourceSet> {

	SourceDirectorySet getScss()

	SourceDirectorySet scss(Action<SourceDirectorySet> action)

	SourceDirectorySet getJs()

	SourceDirectorySet js(Action<SourceDirectorySet> action)

	SourceDirectorySet getStatics()

	SourceDirectorySet statics(Action<SourceDirectorySet> action)


	String getConfigurationName()
	String getTaskBaseName()
	FileCollection getJSPath()

	void setJSPath(FileCollection jsPath)
}

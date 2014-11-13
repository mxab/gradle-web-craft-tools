package webcrafttools.content

import org.gradle.api.Project
import org.gradle.api.artifacts.ModuleVersionIdentifier

import webcrafttools.CacheUtil


class ModuleWebpackContent implements WebPackContent {

	ModuleVersionIdentifier id
	Project project


	@Override
	public Set<File> getJSDirectory() {
		return project.file( CacheUtil.jsPathInCache(id))
	}

	@Override
	public Set<File> getStaticsDirectory() {
		return project.file( CacheUtil.staticsPathInCache(id))
	}

	@Override
	public Set<File> getScssDirectory() {
		return project.file( CacheUtil.scssPathInCache(id))
	}
}

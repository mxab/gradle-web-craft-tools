package webcrafttools

import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.component.ModuleComponentIdentifier

import webcrafttools.bundling.task.WebPack

class CacheUtil {

	public static final BASE_LOCATION = System.getProperty('user.home')+"/.webcrafttools/repo"
	static def pathInCache(ResolvedArtifact resolvedArtifact) {

		pathInCache(resolvedArtifact.moduleVersion.id.group, resolvedArtifact.moduleVersion.id.name, resolvedArtifact.moduleVersion.id.version)
	}
	static def pathInCache(ModuleComponentIdentifier id) {
		pathInCache(id.group,id.module,id.version)
	}
	static def pathInCache( group, name, version) {
		"$BASE_LOCATION/$group/$name/$version"
	}

	static def jsPathInCache(ModuleComponentIdentifier id) {
		"${pathInCache(id)}/$WebPack.JS_DIR"
	}
	static def staticsPathInCache(ModuleComponentIdentifier id) {
		"${pathInCache(id)}/$WebPack.STATICS_DIR"
	}
	static def scssPathInCache(ModuleComponentIdentifier id) {
		"${pathInCache(id)}/$WebPack.SCSS_DIR"
	}
}

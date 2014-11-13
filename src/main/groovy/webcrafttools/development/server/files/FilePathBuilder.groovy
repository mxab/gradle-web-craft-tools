package webcrafttools.development.server.files

import org.gradle.api.Project
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.artifacts.component.ProjectComponentIdentifier

import webcrafttools.CacheUtil
import webcrafttools.bundling.task.WebPack
import webcrafttools.source.WebSourceSet


class FilePathBuilder {

	Project project

	def jsPathFor(ProjectComponentIdentifier pcid,  WebSourceSet wss,  File srcDir, int srcDirIndex,File file) {
		pathFor(pcid, wss, WebPack.JS_DIR, srcDir,srcDirIndex, file)
	}
	def jsPathFor(ModuleComponentIdentifier mcid,  File file) {
		pathFor(mcid,WebPack.JS_DIR, file)
	}
	def staticsPathFor(ProjectComponentIdentifier pcid,  WebSourceSet wss,  File srcDir, int srcDirIndex,File file) {
		pathFor(pcid, wss, WebPack.STATICS_DIR, srcDir,srcDirIndex, file)
	}
	def staticsPathFor(ModuleComponentIdentifier mcid,  File file) {
		pathFor(mcid,WebPack.STATICS_DIR, file)
	}
	def pathFor(ProjectComponentIdentifier pcid,  WebSourceSet wss, String type, File srcDir, int srcDirIndex,File file) {
		def path = file.path - srcDir.path - "/"
		"/FILES/project/$pcid.projectPath/$wss.name/$type/$srcDirIndex/$path"
	}
	def pathFor(ModuleComponentIdentifier mcid, String type, File file) {
		def path = file.path - CacheUtil.pathInCache(mcid) - "/"
		"/FILES/module/$mcid.group/$mcid.module/$mcid.version/$type/$path"
	}
}

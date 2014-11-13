package webcrafttools.bundling.task

import org.gradle.api.internal.file.collections.FileTreeAdapter
import org.gradle.api.internal.file.collections.MapFileTree
import org.gradle.api.internal.file.copy.CopySpecInternal
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.tasks.bundling.Zip
import org.gradle.util.ConfigureUtil

import webcrafttools.Constants
import webcrafttools.bundling.StaticsUsageDescriptor
import webcrafttools.bundling.WebManifest

class WebPack extends Zip {

	public static final String SCSS_DIR = Constants.SRC_TYPE_SCSS
	public static final String STATICS_DIR = Constants.SRC_TYPE_STATICS
	public static final String JS_DIR =  Constants.SRC_TYPE_JS
	public static final String DEFAULT_EXTENSION = 'webpack'


	private WebManifest manifest

	WebPack() {
		extension = DEFAULT_EXTENSION
		manifest = new WebManifest(project.container(StaticsUsageDescriptor))


		rootSpec.from({
			MapFileTree manifestSource = new MapFileTree(temporaryDirFactory, fileSystem)
			manifestSource.add "web.json", {OutputStream output ->

				OutputStreamWriter wtr= new OutputStreamWriter(output)
				def manifestToJson = manifest.toJson()
				println "manifst: $manifestToJson"
				wtr<< manifestToJson
				wtr.flush()
			}
			return new FileTreeAdapter(manifestSource)
		})
	}


	public WebManifest getManifest() {
		return manifest
	}


	public void setManifest(WebManifest manifest) {
		this.manifest = manifest
	}
	public void js(Closure c) {
		into(JS_DIR,c)
	}
	public void statics(Closure c) {
		into(STATICS_DIR,c)
	}
	public void scss(Closure c) {
		into(SCSS_DIR,c)
	}
	public WebPack manifest(Closure<?> configureClosure) {
		if (getManifest() == null) {
			manifest = new WebManifest(project.container(StaticsUsageDescriptor))
		}

		ConfigureUtil.configure(configureClosure, getManifest())
		return this
	}
}

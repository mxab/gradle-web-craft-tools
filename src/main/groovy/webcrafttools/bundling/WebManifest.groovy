package webcrafttools.bundling

import groovy.json.JsonBuilder

import org.gradle.api.NamedDomainObjectContainer


class WebManifest {

	def version = 1
	final NamedDomainObjectContainer<StaticsUsageDescriptor> staticsUsages

	WebManifest(NamedDomainObjectContainer<StaticsUsageDescriptor> staticsUsages) {
		this.staticsUsages = staticsUsages
	}

	def toJson() {
		JsonBuilder json = new JsonBuilder()
		json {
			version version
			usage staticsUsages.asMap
		}
		return json.toString()
	}
}

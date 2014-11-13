package webcrafttools.development.server.closure

import static org.junit.Assert.*

import org.junit.Test

class ClosureJSPathUtilTest {

	@Test
	void matcherMatchesProjectJSPath() {
		//JS/PROJECT_COMPONENT/$sourceSetName/$otherProject.path/$path
		def request = "/JS/PROJECT_COMPONENT/:my-project:sub-project/main/this/is/the/file.js"
		def matcher = ClosureJSPathUtil.matchProjectJS(request)

		assertTrue(matcher.matches())
		def projectPath = matcher.group(1)
		def sourceSet = matcher.group(2)
		def file = matcher.group(3)

		assertEquals(projectPath, ":my-project:sub-project")
		assertEquals(sourceSet, "main")
		assertEquals(file, "this/is/the/file.js")
	}

	@Test
	void matcherMatchesModulePath() {
		//JS/PROJECT_COMPONENT/$sourceSetName/$otherProject.path/$path
		def request = "/JS/MODULE_COMPONENT/org.example/my-component/1.3.6SR1/this/is/the/file.js"
		def matcher = ClosureJSPathUtil.matchModuleJS(request)

		assertTrue(matcher.matches())

		def group = matcher.group(1)
		def name = matcher.group(2)
		def version = matcher.group(3)
		def file = matcher.group(4)


		assertEquals(group, "org.example")
		assertEquals(name, "my-component")
		assertEquals(version, "1.3.6SR1")
		assertEquals(file, "this/is/the/file.js")
	}

	void createsModuleJSPath() {
		//		ResolvedComponentResult result = MockFor(ResolvedComponentResult)
		//		result.demand.id {
		//
		//		}
		//		ClosureJSPathUtil.moduleJSDepsPath(
	}
}

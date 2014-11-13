package webcrafttools.js.closure

import java.util.regex.Pattern

class FileInfo {

	def file
	def provides = []
	def requires = []
	
	@Override
	public String toString() {

		"$file: provides: $provides, requires: $requires"
	}
	
}


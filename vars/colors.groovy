def call(r, g, b, outfile) {
	def steps = 20
	def factorHeadline = 0.75
	def factorCoat = 0.5
	def colors =
		'\\usepackage{color}\n' +
		'\\definecolor{c-white}{rgb}{1,1,1}\n'
	def stepR = (1 - r) / 20
	def stepG = (1 - g) / 20
	def stepB = (1 - b) / 20
	for (n = 1; n <=19; n++) {
		def thisR = 1 - (stepR * n)
		def thisG = 1 - (stepG * n)
		def thisB = 1 - (stepB * n)
		colors += "\\definecolor{c-${n}}{rgb}{${thisR},${thisG},${thisB}}\n"
	}
	colors +=
		"\\definecolor{c-cover}{rgb}{${r},${g},${b}}\n" +
		"\\definecolor{c-headline}{rgb}{${r*factorHeadline},${g*factorHeadline},${b*factorHeadline}}\n" +
		"\\definecolor{c-coverdark}{rgb}{${r*factorCoat},${g*factorCoat},${b*factorCoat}}\n" +
		"\\newcommand{\\coverbgfile}{cover-${r},${g},${b}.jpg}"
	writeFile(file: outfile, text: colors)
}


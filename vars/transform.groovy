def call(text, format) {

	// Ersetzungen
	def replacements = [

		// ***** Texterfassung mit LibreOffice ********************
		
		// Soft hyphen ­ zu LaTeX-Trenner konvertieren
		'(?m)­$' : [
			latex : '\\\\-%',
			raw   : '='
		],
		'­' : [
			latex : '\\\\-',
			raw   : ''
		],


		// ***** Zeichenersetzungen *******************************

		// Ligatur unterdrücken
		'·' : [
			latex : '"|',
			raw   : ''
		],

		// Mehrere . zu …
		' *\\.[\\. ]*\\. *' : [ latex : '~… ' ],

		// … ? zu …?
		'… \\?' : [ latex : '…?' ],

		// – ohne Leerzeichen mit Leerzeichen versehen.
		'([^ \r\n])–' : [ latex : '$1 –' ],

		// Mehrere – zu einem
		'[ –]+–[ –]*([^,“«!?.:])' : [ latex : ' – $1' ],
		'[ –]+–[ –]*([,“«!?.:])' : [ latex : ' –$1' ],

		// = zu -
		'=' : [ latex : '-' ],

		// »« zu ›‹ 
		'»' : [ latex : '›' ],
		'«' : [ latex : '‹' ],
		'“' : [ latex : '«' ],
		'„' : [ latex : '»' ],

		// Nach «‹ Leerzeichen einfügen
		'([«‹])…' : [ latex : '$1~…' ],

		// ,« zu «,
		',«' : [ latex : '«,' ],

		// ,‹ zu ‹,
		',‹' : [ latex : '‹,' ],

		// «. zu .«
		'«\\.' : [ latex : '.«' ],

		// ‹. zu .‹
		'‹\\.' : [ latex : '.‹' ],

		// Six-per-em space zu gesperrt
		' ' : [ latex : '\\\\kern 0.1em{}' ],

		// ... «
		'… ([«‹])' : [ latex : '…~$1' ],


		// ***** Eingriffe, Korrekturen, **************************
		// ***** Verweise zu Anmerkungsapparat ********************

		// Korrektureingriffe
		'\\[([^\\|\\]]*)\\|([^\\]]*)\\]' : [
			latex : '$2',
			raw   : '$1'
		],

		// Kommentare
		'(?m)^\\[- [^\\]\\|]*\\]\n' : '',
		'\\[- [^\\]\\|]*\\]'        : '' ,

		// Seitenangaben
		'(?m)^\\[s ([^\\]\\|]+)\\]\n' : [
			latex : '% Seite $1\n',
			raw   : '[$1]\n'
		],

		// Kapitel
		'<k ([^>]+)>\n' : [
			latex : '\\\\cleartooddpage\\\\addchap{$1}\n',
			raw   : '$1\n'
		],

		// Looseness / Stretch
		'<s>' : [
			latex : '\\\\looseness=1\n',
			raw   : ''
		],
		'<s 0>' : [
			latex : '\\\\resetstretch{}',
			raw   : ''
		],
		'<s ([^>]+)>' : [
			latex : '\\\\setstretch{$1}',
			raw   : ''
		],

		// Zentriert
		'<z ([^>]+)>\n' : [
			latex : '\n\\\\$1{center}\n',
			raw   : ''
		],

		// No break
		'<n ([^>]+)>' : [
			latex : '\\\\rlap{$1}',
			raw   : '$1'
		],

		// No hyphen
		'<h ([^>]+)>' : [
			latex : '\\\\mbox{$1}',
			raw   : '$1'
		],

		// Separator
		'<>'          : [
			latex : '{}',
			raw   : ''
		],

		// Label
		'<l ([^>]+)>' : [
			latex : '\\\\label{$1}\\\\linelabel{$1}',
			raw   : ''
		],

		// Abschnitt
		'<a>\n' : [
			latex : '\\\\bigbreak\\\\bigbreak\n\\\\noindent ',
			raw   : '\n'
		],
		'<aa>\n' : [
			latex : '\\\\bigbreak\\\\bigbreak\\\\bigbreak\n\\\\noindent ',
			raw   : '\n'
		],
		'<aaa>\n' : [
			latex : '\\\\bigbreak\\\\bigbreak\\\\bigbreak\\\\bigbreak\n\\\\noindent ',
			raw   : '\n'
		],

		// Verse
		'<v begin ([^>]+)>\n' : [
			latex : '\\\\begin{verse}[$1]\n',
			raw   : ''
		],
		'<v begin>\n' : [
			latex : '\\\\begin{verse}[6cm]\n',
			raw   : ''
		],
		'<v end>\n' : [
			latex : '\\\\end{verse}\n',
			raw   : ''
		],

		// Verszeilenenden
		'\\\\\\\\([\\*]*)' : [
			latex : '\\\\\\\\$1',
			raw   : ''
		],

		// Protected space
		'\\\\ ' : [
			latex : '\\\\ ',
			raw   : ' '
		],

		// x, –‹ etc. Leerzeichen schützen
		'([^ \n]+)([,\\.]) –([‹«])'   : [ latex : '\\\\mbox{$1$2 –$3}' ],
		'([^ \n]+) –([‹«])'           : [ latex : '\\\\mbox{$1 –$2}' ],
		'([›»])~… ([^\\\\ \n]+)'      : [ latex : '\\\\mbox{$1 … $2}' ], // ???
		'([^ \n-]+) – ([^\n])'                : [ latex : '\\\\mbox{$1 – }$2' ],
		'([^ \n-]+) – \n'                : [ latex : '\\\\mbox{$1 – }%\n' ],


		// ***** Zeichenabstände in LaTeX *************************

		// Abstand vor .,
		'([a-zäöüßéèA-ZÄÖÜ])([\\.,])' : [ latex : '$1\\\\kern 0.05em$2' ],

		// French spacing verhindern 
		'([\\.!?:;‹«]) –'             : [ latex : '$1\\\\ –' ],
		'([‹«]) ([a-zäöü…])'          : [ latex : '$1\\\\ $2' ],

		// Mehr um Gedankenstrich 
		'–'                           : [ latex : '\\\\kern 0.1em{}–\\\\kern 0.1em{}' ],

		// Ersetzen … durch ...
		'…'                           : [ latex : '.\\\\kern 0.1em.\\\\kern 0.1em.' ],

		// Abstand vor !
		'!'                           : [ latex : '\\\\kern 0.05em!' ],

		// Abstand vor :
		':'                           : [ latex : '\\\\kern 0.05em:' ],

		// Abstand vor ;
		';'                           : [ latex : '\\\\kern 0.05em;' ],

		// Abstand vor ?
		'\\?'                         : [ latex : '\\\\kern 0.05em\\?' ]

	]

	def transformedText = text

	replacements.each { key, value ->
		if (value instanceof String) {
			transformedText = transformedText.replaceAll(key, value)
		} else {
			if (value[format] != null) {
				transformedText = transformedText.replaceAll(key, value[format])
			}
		}
	}

	transformedText

}


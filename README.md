# Max Mohr: Frau Marie's Gast


## Quellenlage

Es stehen keine weiteren Quellen zur Verfügung als die gedruckte
Erstausgabe.


## Struktur

### Quellcode

| Verzeichnis / Dateien          | Inhalt                                                  | Format           |
| ---                            | ---                                                     | ---              |
| README.md                      | Dieser Text                                             | Markdown         |
| Jenkinsfile                    | Pipeline zum Erzeugen und Verifizieren der PDFs         | Jenkins Pipeline |
| Jenkinsfile-OCR                | Pipeline zum Konvertieren der Scans                     | Jenkins Pipeline |
| jobs.seed                      | Beschreibung der Jenkins-Jobs                           | Jenkins Job DSL  |
| ocr/text.txt                   | Korrigiertes OCR der Scans                              | UTF-8            |
| scans/                         | Scans                                                   | TIFF             |
| src/main/latex/                | Quellcode des Buchtextes                                | verschieden      |
| src/main/latex/text-*.tex      | Quellcode des Buchtextes, Hauptdatei                    | LaTeX            |
| src/main/latex/*_teil.tex      | Quellcode des Buchtextes, Kapitel                       | proprietär       |
| src/main/latex/zum_band.tex    | Quellcode des Buchtextes, Anhang                        | LaTeX            |
| src/main/latex/titel.tex       | Quellcode des Buchtextes, Titelteil                     | LaTeX            |
| vars/transform.groovy          | Transformator proprietäres Format --> LaTeX             | Groovy           |
| vars/colors.groovy             | Erzeugung von Farbdefinitionen auf Basis der Coverfarbe | Groovy           |
| src/main/latex/coat-*.tex      | Details siehe nächster Abschnitt                        | LaTeX            | 
| src/main/latex/cover-*.tex     | Details siehe nächster Abschnitt                        | LaTeX            |
| src/main/latex/sleeve-*.tex    | Details siehe nächster Abschnitt                        | LaTeX            |
| src/main/latex/definitions.tex | Diverse Definitionen                                    | LaTeX            |
| src/main/latex/measures-*.tex  | Definition von Abmessungen der einzelnen Ausgaben       | LaTeX            |

### Komposition der verschiedenen Dokumente

**fett:** Ein eigener LaTeX-Build

#### Buch Hardcover

* **cover-hardcover.tex** --> ***cover-hardcover.pdf***
  * *include* cover picture
  * *include* sleeve-back-hardcover.pdf
  * *include* cover-back-hardcover.pdf
  * *include* cover-spine-hardcover.pdf
  * *include* cover-front-hardcover.pdf
  * *include* sleeve-front-hardcover.pdf
    
* **sleeve-back.tex**
  * measures-hardcover.tex
  * sleeve-back.tex
    * commons.tex

* **cover-back-hardcover.tex**
  * measures-hardcover.tex
  * cover-back.tex
    * commons.tex
    * definitions.tex

* **spine-hardcover.tex**
  * measures-hardcover.tex
  * spine.tex
    * commons.tex
    * definitions.tex

* **cover-front-hardcover.tex**
  * measures-hardcover.tex
  * cover-front.tex
    * commons.tex
    * definitions.tex

* **sleeve-front.tex**
  * measures-hardcover.tex
  * sleeve-front.tex
    * commons.tex

* **coat-hardcover.tex** --> ***coat-hardcover.pdf***
  * *include* coat-back-hardcover.pdf
  * *include* coat-spine-hardcover.pdf
  * *include* coat-front-hardcover.pdf

* **coat-back-hardcover.tex**
  * measures-coat.tex
  * commons.tex

* **coat-spine-hardcover.tex**
  * measures-coat.tex
  * commons.tex

* **coat-front-hardcover.tex**
  * measures-coat.tex
  * commons.tex

**text-hardcover.tex** --> ***text-hardcover.tex***
  * measures-hardcover.tex
  * text.tex
    * Text of book


#### Buch Paperback

* **cover-paperback.tex** --> ***cover-paperback.pdf***
  * *include* cover picture
  * *include* cover-back-hardcover.pdf
  * *include* cover-spine-hardcover.pdf
  * *include* cover-front-hardcover.pdf
    
* **cover-back-paperback.tex**
  * measures-paperback.tex
  * cover-back.tex
    * commons.tex
    * definitions.tex

* **spine-paperback.tex**
  * measures-paperback.tex
  * spine.tex
    * commons.tex
    * definitions.tex

* **cover-front-paperback.tex**
  * measures-paperback.tex
  * cover-front.tex
    * commons.tex
    * definitions.tex

**text-paperback.tex** --> ***text-paperback.pdf***
  * measures-paperback.tex
  * text.tex
    * Text of book


#### Ebbok PDF

* **text-ebook.tex** --> ***text-ebook.pdf***
  * measures-ebook.tex
  * text.tex
    * *include* cover-front-ebook.pdf with cover picture
    * Text of book
    * *include* cover-back-ebook.pdf

* **cover-front-ebook.tex**
  * measures-ebook.tex
  * cover-front.tex
    * commons.tex
    * definitions.tex

* **cover-back-ebook.tex**
  * measures-ebook.tex
  * cover-back.tex
    * commons.tex
    * definitions.tex


## Proprietäres Format für den Buchtext

Obwohl LaTeX (genauer: XeTeX) als Satzformat verwendet wird,
wird ein proprietäres Format für den Quelltext des Buches verwendet.
Die Gründe dafür sind vielfältig:

### Einfach zu wartendes und zu verarbeitendes Format

Auf technischer Seite werden Standardtechnologien
eingesetzt, die für dieses Projekt eine größere Flexibilität
und Transparenz als LaTeX besitzen:

* Groovy
* Regex
* UTF

### Trennung von Text und Formatierungsanweisungen

#### Automatismen

Um das gewünschte Satzbild des Textabschnittes

	„Nein, er schläft nimmer!“, rief Hans Jung,
	„guten Morgen!“ – „Guten Morgen!“, rief der
	Bruder herauf. – „Guten Morgen!“, rief es aus dem
	Zimmer neben ihm. Sie lachte wieder kurz auf, wie
	gestern. Vielleicht strich sie sich wieder die Strähne
	in den glatten Scheitel zurück.

zu erreichen, ist in LaTeX folgender effektiver Code notwendig:

	»Nein\kern 0.05em, er schläft nimmer\kern 0.05em!«\kern 0.05em,\ rief Hans Jung\kern 0.05em,
	»guten \mbox{Morgen\kern 0.05em!«\kern 0.05em,\ \kern 0.1em{}–\kern 0.1em{} }»Guten Morgen\kern 0.05em!«\ rief der
	Bruder \mbox{herauf\kern 0.05em.\ \kern 0.1em{}–\kern 0.1em{} }»Guten Morgen\kern 0.05em!«\kern 0.05em,\ rief es aus dem
	Zimmer neben ihm\kern 0.05em. Sie lachte wieder kurz auf\kern 0.05em, wie
	gestern\kern 0.05em. Vielleicht strich sie sich wieder die Strähne
	in den glatten Scheitel zurück\kern 0.05em.

Die meisten Formatanweisungen für LaTeX sollen nicht explizit
gesetzt werden, wie der Abstand vor Satzzeichen. Diese werden
mit Hilfe regulärer Ausdrücke in eine Kopie des Quelltextes
eingefügt, z. B. wird jedes Vorkommen von `!` zu `\kern 0.05em!`.

Diese Vorgehensweise bietet den Vorteil, dass sich bei einer
Anpassung des Satzformates der Text-Quelltext des Buches
nicht verändert.

Für alle Automatismen ist der Quellcode der Datei
[transform.groovy](vars/transform.groovy) zu konsultieren.

Explizite Formatierungsanweisungen werden durch spezielle
Tags im Quelltext realisiert, z. B. Zentrierungen:

	<z begin>
	Geschrieben Kairo 1911.
	<z end>

Für alle expliziten Formatierungsanweisungen ist der Quellcode der
Datei [transform.groovy](vars/transform.groovy) zu konsultieren.

#### Manuelle Formatierungen

* Absatzlänge dehnen oder kürzen mit dem Tag `<s>` (dehnen) und den Tagpaar
`<s #>` am Absatzanfang und `<s 0>` am Absatzende, wobei `#` für einen
numerischen Wert steht.

Für weitere Formatierungstags und deren Verwendung ist der Quellcode
der Datei [transform.groovy](vars/transform.groovy) zu konsultieren.


### Metainformationen im Quelltext

Formatierungsanweisungen sollen weitest möglich aus dem
Quelltext getilgt werden. Bestimmte Metainformationen
hingegen sollen im Quelltext gepflegt werden, z. B.
Korrekturen des Originaltextes:

	Und eines [Morgen|Morgens] fiel auch der schöne Vogel
	tot aus den Blüten. Der Baum stand leer.

### Identität mit der Vorlage

Die Quelltexte sind identisch mit der primären Erfassungsquelle.
Von der Vorlage abweichende Formatierungsanweisungen und
Korrekturen werden über Tags realisiert.

Um aus dem Quelltext den reinen Vorlagentext zu erhalten,
existieren einige wenige Regeln, um die Tags zu entfernen.

Vergleiche mit zusätzlichen Quellen sind möglich, z. B.
durch OCR gewonnene Dokumente.


## Übersicht über die Tags im Text-Quelltext

### Betroffene Dateien

* [Erster Teil](src/main/latex/erster_teil.tex)
* [Zweiter Teil](src/main/latex/zweiter_teil.tex)
* [Dritter Teil](src/main/latex/dritter_teil.tex)

### Korrekturen

Korrekturen werden durch `[Original|Korrektur]` gekennzeichnet.

### Kommentare

Kommentare besitzen das Format `[- Kommentartext]`. Sie werden im
Ausgabeformat nicht berücksichtigt.

### Seitenangaben

Die Seiten des Textes der EA werden durch `[s Seitenzahl]`
gekennzeichnet. Sie werden im Ausgabeformat nicht berücksichtigt.

### Kapitel

Kapitelüberschriften werden durch das Format `[k Kapitelüberschrift]`
gekennzeichnet. Sie erscheinen im Ausgabeformat als Kapitelüberschrift.

### Abweichende Absatzformatierungen

Zentrierter Text wird durch die Blöcke `<z begin>` und
`<z end>` eingefasst.

Verszeilen werden durch die Blöcke `<v begin>`und `<v end>`
eingefasst. Es kann auch die Breite der Verse angegeben werden
durch einen zusätzlichen Parameter: `<v begin 7cm>`. Unterbleibt
dies, wird eine angenommene Breite von 6 cm gesetzt.

Für die Zeilenumbrüche bei Versen werden die Zeilentrenner
von LaTex `\\` und `\\*` verwendet.

Textabschnitte, die nicht umgebrochen werden dürfen, werden
durch `<n Textabschnitt>` markiert.

Ein Abschnitt zwischen zwei Absätzen wird durch `<a>`, `<aa>`
oder `<aaa>` (je mehr `a` desto größer der Abstand) gesetzt.
An einigen Stellen werden auch Zeilenubrüche (`\\`) verwendet,
um den Abstand genauer justieren zu können.

### Geschützte Leerzeichen

Geschützte Leerzeichen werden in der LaTeX-Schreibweise
durch einen vorausgehenden `\` gesetzt.


## Regeln für den Satz dieser Ausgabe

### Interpunktion bei Anführungszeichen

Hier werden die aktuellen Regeln der deutschen Rechtschreibung
angewandt.

Im Text der EA stehen das Komma fast immer vor den abschließenden
Anführungszeichen. Beim Bauen wird es automatisch danach platziert.
Das Setzen eines Komma nach Frage- und Ausrufesätzen in der wörtlichen
Rede wird in entsprechenden Fällen manuell korrigiert.


### Gedankenstriche

Mehrere direkt aufeinander folgende Gedankenstriche werden beim Bauen
automatisch zu einem einzigen zusammen gefasst.

An einigen Stellen stehen (mehrere) Gedankenstriche für einen Sprung
im zeitlichen Ablauf. Dort werden sie manuell durch einen
grafischen Abschnitt im Buchsatz ergänzt.

Beispiel aus der Vorlage:

	Aber als er später sprach, war ein Klang in
	seiner Stimme, als wolle er die alte, dünne Hand
	küssen. –
	   Frische Blumen waren in dem Krug auf seinem
	Tisch. Er trat ans Fenster und öffnete es weit.

(Seite 60)

### Auslassungspunkte

Auslassungspunkte werden einheitlich als … wiedergegeben.

Diese werden im Text oft als Pausenzeichen verwendet.

An einigen Stellen stehen Auslassungspunkte für einen Sprung
im zeitlichen Ablauf. Dort werden sie durch einen
grafischen Abschnitt im Buchsatz ergänzt.

Beispiele aus der Vorlage:

	bis Hans Jung und der Baron sich gemeinsam ver-
	abschiedeten und zusammen durch die Wiesen gingen,
	in denen schon die Grillen angefangen hatten mit
	ihrem Sommerlärm.
	   . . . An manchen Abenden saß er in seinem
	Zimmer bei Marie vor dem Feldblumenstrauß,

(Seite 45)

	„Wissen Sie noch, wie Sie das letztemal bei
	uns gespielt haben,“ sagte sie, „wie lang es her ist“ . . .
	   Ja, es war lange her. Es war an dem Abend
	gewesen, bevor er sich verabschiedet hatte.

(Seite 31)

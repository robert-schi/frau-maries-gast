@Library('global-lib@komma') _


properties([buildDiscarder(logRotator(numToKeepStr: '10'))])


def textFiles = ['erster_teil', 'zweiter_teil', 'dritter_teil']
def latexCmd = 'xelatex'
def srcDir = 'src/main/latex'
def buildSrcDir = "build/${srcDir}"

node {

  def outputDirBook = "${WORKSPACE}/build/book"

  stage('Get Source') {
    checkout scm
  }

  stage('Prepare') {
    parallel([
      'LaTeX': {
        sh "mkdir -p ${buildSrcDir}"
        sh "cp -p ${srcDir}/* ${buildSrcDir}/"
        textFiles.each {
          def textPart = readFile(file: "${srcDir}/${it}.tex", encoding: 'UTF-8')
          writeFile(file: "${buildSrcDir}/${it}.tex", text: transform(textPart, 'latex'), encoding: 'UTF-8')
        }
        colors(0.8980, 0.2302, 0.2757, "${buildSrcDir}/commons.tex")
      },
      'Compare with OCR': {
        def textAll = ''
        textFiles.each {
          def textPart = readFile(file: "${srcDir}/${it}.tex", encoding: 'UTF-8')
          textAll += transform(textPart, 'raw')
        }
        writeFile(file: 'build/text.txt', text: textAll, encoding: 'UTF-8')
        archiveArtifacts 'build/text.txt'
        sh 'icdiff --line-numbers --unified=2 --cols=160 ocr/text.txt build/text.txt | head -n 750'
        sh '#!/bin/bash\nset +x\necho $(diff ocr/text.txt build/text.txt | grep "^>" | wc -l) differences, for details see step before.\n' +
          '( cmp=$(diff ocr/text.txt build/text.txt | grep "^>" | wc -l); ((cmp == 0)) )'
      }
    ])
  }

  dir(buildSrcDir) {

    stage('Covers') {
      parallel([
        'Paperback': {
          sh "${latexCmd} cover-back-paperback.tex"
          sh "${latexCmd} cover-spine-paperback.tex"
          sh "${latexCmd} cover-front-paperback.tex"
          sh "${latexCmd} cover-paperback.tex"
          fileOperations([fileCopyOperation(includes: 'cover-paperback.pdf', targetLocation: outputDirBook)])
        },
        'Hardcover': {
          sh "${latexCmd} sleeve-back-hardcover.tex"
          sh "${latexCmd} cover-back-hardcover.tex"
          sh "${latexCmd} cover-spine-hardcover.tex"
          sh "${latexCmd} cover-front-hardcover.tex"
          sh "${latexCmd} sleeve-front-hardcover.tex"
          sh "${latexCmd} cover-hardcover.tex"
          fileOperations([fileCopyOperation(includes: 'cover-hardcover.pdf', targetLocation: outputDirBook)])
        },
        'Coat Hardcover': {
          sh "${latexCmd} coat-back-hardcover.tex"
          sh "${latexCmd} coat-spine-hardcover.tex"
          sh "${latexCmd} coat-front-hardcover.tex"
          sh "${latexCmd} coat-hardcover.tex"
          fileOperations([fileCopyOperation(includes: 'coat-hardcover.pdf', targetLocation: outputDirBook)])
        },
        'Ebook': {
          sh "${latexCmd} cover-front-ebook.tex"
          sh "${latexCmd} cover-back-ebook.tex"
        }
      ])
    }

    stage('Texts') {
      parallel([
        'Paperback': {
          sh "${latexCmd} text-paperback.tex"
          sh "${latexCmd} text-paperback.tex"
          sh "${latexCmd} text-paperback.tex"
          sh "${latexCmd} text-paperback.tex"
          fileOperations([fileCopyOperation(includes: 'text-paperback.pdf', targetLocation: outputDirBook)])
        },
        'Hardcover': {
          sh "${latexCmd} text-hardcover.tex"
          sh "${latexCmd} text-hardcover.tex"
          sh "${latexCmd} text-hardcover.tex"
          sh "${latexCmd} text-hardcover.tex"
          fileOperations([fileCopyOperation(includes: 'text-hardcover.pdf', targetLocation: outputDirBook)])
        },
        'Ebook': {
          sh "${latexCmd} text-ebook.tex"
          sh "${latexCmd} text-ebook.tex"
          sh "${latexCmd} text-ebook.tex"
          sh "${latexCmd} text-ebook.tex"
          fileOperations([fileCopyOperation(includes: 'text-ebook.pdf', targetLocation: outputDirBook)])
        }
      ])
    }

  }

  archiveArtifacts "build/book/*.pdf"
  archiveArtifacts "src/main/resources/*"

//  stage('ComparePDF with Raw Text') {
//    sh "pdftotext build/ebook/text.pdf build/text-pdf.txt"
//    sh 'icdiff --line-numbers --unified=2 --cols=160 build/text-pdf.txt build/text.txt'
//  }

}


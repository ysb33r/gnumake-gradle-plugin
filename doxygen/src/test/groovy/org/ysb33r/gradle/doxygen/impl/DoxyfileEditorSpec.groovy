package org.ysb33r.gradle.doxygen.impl

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification


/**
 * Created by schalkc on 22/05/2014.
 */
class DoxyfileEditorSpec extends Specification {
    static final File SRC_DOXYFILE = new File( System.getProperty('TESTFSREADROOT') ?: 'src/test/resources','DoxyfileEditor.dox' )
    static final File WRITEABLE_DOXYFILE = new File( System.getProperty('TESTFSWRITEROOT') ?: 'build/tmp/test','editor/DoxyfileEditor.dox' )

    Project project = ProjectBuilder.builder().build()
    DoxyfileEditor editor = new DoxyfileEditor( logger : project.logger )
    DoxygenProperties replacements = new DoxygenProperties()

    void setup() {
        if(WRITEABLE_DOXYFILE.parentFile.exists()) {
            WRITEABLE_DOXYFILE.parentFile.deleteDir()
        }

        WRITEABLE_DOXYFILE.parentFile.mkdirs()

        WRITEABLE_DOXYFILE.text = SRC_DOXYFILE.text
    }

    def "Default action will removes comments and collapse +="() {
        given:
            replacements.setProperty 'CREATE_SUBDIRS', true
            replacements.setProperty 'PROJECT_NUMBER', '1.11'
            editor.update(replacements.properties,WRITEABLE_DOXYFILE)
            def lines = []
            WRITEABLE_DOXYFILE.eachLine { lines.add(it) }

        expect:
            lines.find { it == 'CREATE_SUBDIRS = YES'}
            !lines.find { it.startsWith('#') }
            lines.find { it == 'FILE_PATTERNS = *.c *.cpp' }
            lines.find { it == 'PROJECT_NUMBER = 1.11' }
    }
}
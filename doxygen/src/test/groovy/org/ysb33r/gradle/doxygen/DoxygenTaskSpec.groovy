// ============================================================================
// (C) Copyright Schalk W. Cronje 2013
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and limitations under the License.
//
// ============================================================================

package org.ysb33r.gradle.doxygen

import org.gradle.api.logging.LogLevel
import spock.lang.*
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class DoxygenTaskSpec extends spock.lang.Specification {

    static final Boolean DO_NOT_RUN_DOXYGEN_EXE_TESTS = System.getProperty('DO_NOT_RUN_DOXYGEN_EXE_TESTS')
    static final File TESTFSREADROOT = new File( System.getProperty('TESTFSREADROOT') ?: 'src/test/resources' )
    static final File TESTFSWRITEROOT = new File( System.getProperty('TESTFSWRITEROOT') ?: 'build/tmp/test' )
    static final File DOXY_TEMPLATE = new File ( System.getProperty('DOXY_TEMPLATE') ?: 'src/main/resources/doxyfile-template.dox')

    Project project = ProjectBuilder.builder().withProjectDir(TESTFSREADROOT).build()
    def dox = project.task('doxygen', type: Doxygen )

    void setup() {
        if(TESTFSWRITEROOT.exists()) {
            TESTFSWRITEROOT.deleteDir()
        }

        TESTFSWRITEROOT.mkdirs()

        project.buildDir = TESTFSWRITEROOT

        if(System.getProperty('DOXYGEN_PATH')) {
            dox.configure {
                executables {
                    doxygen System.getProperty('DOXYGEN_PATH')
                }
            }
        }
    }

    def "Setting specific Doxygen properties that take boolean values"() {
        given:

            dox.configure {
                quiet true
                warnings false
                recursive true
                subgrouping false
            }

        expect:
            dox.doxygenProperties[doxName] == doxValue

        where:
            doxName      | doxValue
            'QUIET'      | 'YES'
            'WARNINGS'   | 'NO'
            'RECURSIVE'  | 'YES'
            'SUBGROUPING'| 'NO'
    }

    def "Using 'input' should throw an exception"() {
        when:
            dox.configure {
                input '/this/path'
            }

        then:
            thrown(DoxygenException)
    }

    def "Using 'mscgen_path' should throw an exception"() {
        when:
            dox.configure {
                mscgen_path '/this/path'
            }

        then:
            thrown(DoxygenException)
    }

    def "Must be able to set executable paths via executables closure"() {
        given:
            dox.configure {
                executables {
                    doxygen '/path/to/doxygen'
                    mscgen  '/path/to/mscgen'
                }
            }

        expect:
            dox.executables.doxygen == '/path/to/doxygen'
            dox.executables.mscgen  == '/path/to/mscgen'

    }

    def "Only supported executables must be configurable"() {
        when:
            dox.configure {
                executables {
                    foobar '/path/to/foo'
                }
            }

        then:
            thrown(DoxygenException)
    }

    def "Only lower case equivalents of Doxygen properties are allowed"() {
        when:
            dox.configure {
                'OUTPUT_LANGUAGE' 'English'
            }

        then:
            thrown(DoxygenException)
    }

    def "Lower case equivalents of Doxygen properties should update final properties"() {
        given:
            dox.configure {

                output_language      'English'
                tab_size              2
                inherit_docs          true
                separate_member_pages false
                project_logo          new File('src/resources/logo.png')
                file_patterns         '*.c', '*.cpp'
                project_brief         'This is a description with spaces'
            }

        expect:
            dox.doxygenProperties[doxName] == doxValue

        where:
            doxName                | doxValue
            'OUTPUT_LANGUAGE'      | 'English'
            'TAB_SIZE'             | '2'
            'INHERIT_DOCS'         | 'YES'
            'SEPARATE_MEMBER_PAGES'| 'NO'
            'PROJECT_LOGO'         | new File('src/resources/logo.png').absolutePath
            'FILE_PATTERNS'        | '*.c *.cpp'
            'PROJECT_BRIEF'        | '"This is a description with spaces"'
    }

    def "Default Doxygen properties should be set for specific paths and gradle properties"() {
        given:
            Project proj = ProjectBuilder.builder().withName('DoxygenTaskSpec').build()
            proj.version  = '1.1'
            proj.buildDir = 'build/foo'
            proj.logging.level = LogLevel.INFO
            def defdox = proj.task('doxygen', type: Doxygen )


        expect:
            defdox.doxygenProperties[doxName] == doxValue

        where:
            doxName                | doxValue
            'PROJECT_NUMBER'       | '1.1'
            'PROJECT_NAME'         | 'DoxygenTaskSpec'
//            'QUIET'                | 'NO'
//            'WARNINGS'             | 'YES'
    }

    @IgnoreIf( {DO_NOT_RUN_DOXYGEN_EXE_TESTS} )
    def "Run Doxygen to generate simple documentation with a default template"() {
        given:
            dox.configure {
                source new File(TESTFSREADROOT,'sample-cpp')
                outputDir new File(TESTFSWRITEROOT,'docs')

                generate_xml   false
                generate_latex false
                generate_html  true
                have_dot       false

                executables {
                    dot 'path/to/dot'
                }
            }

            dox.exec()

        expect:
            new File(TESTFSWRITEROOT,'docs/html').exists()
            new File(TESTFSWRITEROOT,'docs/html/index.html').exists()
            dox.doxygenProperties['DOT_PATH'] == new File('path/to/dot').absolutePath
    }

    def "Setting image_path should also update the input files (not source files)"() {
        given:
            dox.configure {
                source new File(TESTFSREADROOT,'sample-cpp')
                outputDir new File(TESTFSWRITEROOT,'docs')
                image_path new File(TESTFSREADROOT,'non-existing1')
                image_path new File(TESTFSREADROOT,'non-existing2')
            }

            dox.setDefaults()

        expect:
            dox.inputs.files.contains(new File(TESTFSREADROOT,'non-existing1'))
            dox.inputs.files.contains(new File(TESTFSREADROOT,'non-existing2'))
            dox.doxygenProperties['IMAGE_PATH'] == new File(TESTFSREADROOT,'non-existing1').absolutePath + ' ' +
                    new File(TESTFSREADROOT,'non-existing2').absolutePath

    }

    @IgnoreIf( {DO_NOT_RUN_DOXYGEN_EXE_TESTS} )
    def "When custom template is supplied, expect template to be copied and then modified"() {
        given:
            Project proj = ProjectBuilder.builder().withName('DoxygenTaskSpec').build()
            proj.buildDir = TESTFSWRITEROOT
            def doxCustom = proj.task('doxygen', type: Doxygen )

            doxCustom.configure {
                source new File(TESTFSREADROOT,'sample-cpp')
                outputDir new File(TESTFSWRITEROOT,'docs')

                generate_xml   false
                generate_latex false
                generate_html  true
                have_dot       false

                template DOXY_TEMPLATE

                if(System.getProperty('DOXYGEN_PATH')) {
                    executables {
                        doxygen System.getProperty('DOXYGEN_PATH')
                    }
                }
            }

            doxCustom.exec()

            def lines = new File(proj.buildDir,'tmp/DoxygenTaskSpec.doxyfile').text.readLines()

        expect:
            new File(TESTFSWRITEROOT,'docs/html').exists()
            new File(TESTFSWRITEROOT,'docs/html/index.html').exists()
            lines.find { 'FILE_PATTERNS ='}
            doxCustom.inputs.files.contains(DOXY_TEMPLATE)
    }

    @IgnoreIf( {DO_NOT_RUN_DOXYGEN_EXE_TESTS} )
    def "When 'template' is supplied as a string, configuration should still work"() {
        given:
            Project proj = ProjectBuilder.builder().withName('DoxygenTaskSpec').build()
            proj.buildDir = TESTFSWRITEROOT
            def doxCustom = proj.task('doxygen', type: Doxygen )

            doxCustom.configure {
                source new File(TESTFSREADROOT,'sample-cpp')
                outputDir new File(TESTFSWRITEROOT,'docs')

                generate_xml   false
                generate_latex false
                generate_html  true
                have_dot       false

                template DOXY_TEMPLATE.absolutePath

                if(System.getProperty('DOXYGEN_PATH')) {
                    executables {
                        doxygen System.getProperty('DOXYGEN_PATH')
                    }
                }
            }

            doxCustom.exec()

            def lines = new File(proj.buildDir,'tmp/DoxygenTaskSpec.doxyfile').text.readLines()

        expect:
            new File(TESTFSWRITEROOT,'docs/html').exists()
            new File(TESTFSWRITEROOT,'docs/html/index.html').exists()
            lines.find { 'FILE_PATTERNS ='}
            doxCustom.inputs.files.contains(DOXY_TEMPLATE)
    }
}


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

package org.ysb33r.gradle.a2x

import spock.lang.*
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class AsciidocPluginSpec extends spock.lang.Specification {
//    File testFsReadOnlyRoot = new File("${System.getProperty('TESTFSREADROOT')}/src/test/resources/test-files")
//    String testFsURI = new URI(testFsReadOnlyRoot).toString()
//    File testFsWriteRoot= new File( "${System.getProperty('TESTFSWRITEROOT') ?: 'build/tmp/test-files'}/file")
//    String testFsWriteURI= new URI(testFsWriteRoot).toString()
    Project project = ProjectBuilder.builder().build()
    def a2x

    void setup() {
        project.apply plugin:'a2x'
        a2x = project.task('a2xtest', type: A2X )
        File of = new File('build/tmp/a2x')
        if(of.exists()) {
            assert of.deleteDir()
        }
        of.mkdir()
        a2x.outputDir = of
    }
    
    def "Can apply Asciidoc plugin to project"() {

        expect:
            project.tasks.a2x != null
    }

    def "Generate HTML"() {

        when:
            a2x.renderFile( 'html', new File('src/test/resources/unittest.adoc') )

        then:
            new File(a2x.outputDir,'unittest.html').exists()
    }
//    def "Calling VFS closures should execute immediately"() {
//
//        Integer count= 0
//        given:
//            testFsWriteRoot.deleteDir()
//            project.apply plugin:'vfs'
//            project.vfs {
//                cp testFsURI,"${testFsWriteURI}/one/two/three", recursive:true
//            }
//            testFsWriteRoot.eachFileRecurse {
//                if (it.name =~ /file\d\.txt/ ) {
//                    ++count
//                }
//            }
//
//        expect:
//          count == 4
//
//    }
//
//    def "Must be able to set vfs style properties via a configuration-style block"() {
//        FtpFileSystemConfigBuilder fscb
//
//        given:
//        project.apply plugin:'vfs'
//        fscb = project.__vfs.fsMgr.getFileSystemConfigBuilder('ftp') as FtpFileSystemConfigBuilder
//        fscb.setPassiveMode( project.__vfs.defaultFSOptions, false)
//        project.vfs {
//            options {
//                ftp {
//                    passiveMode true
//                }
//            }
//        }
//
//        expect:
//        fscb.getPassiveMode( project.__vfs.defaultFSOptions )
//    }
}


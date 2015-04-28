//
// ============================================================================
// (C) Copyright Schalk W. Cronje 2013-2015
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and limitations under the License.
//
// ============================================================================
//

package org.ysb33r.gradle.gnumake

import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification


/**
 * @author Schalk W. Cronjé
 */
class RunMakeSpec extends Specification {

    static final File MAKESCRIPT = new File( "src/test/resources/fake-make-scripts/${OperatingSystem.current().isWindows() ? 'fake-make.bat' : 'fake-make.sh'}" )
    Project project = ProjectBuilder.builder().build()

    void captureStdOut() {
    }


    def "Check that Make is invoked with the correct arguments"() {
        given:
        def systemOut = new ByteArrayOutputStream()
        System.out = new PrintStream(systemOut)

        project.allprojects {
            apply plugin : 'org.ysb33r.gnumake'

            gnumake {
                executable MAKESCRIPT.absolutePath
            }

            make {
                flags DESTDIR : 'foo/bar'
                targets 'build','install'
            }
        }

        project.evaluate()
        project.tasks.make.execute()
        String output = systemOut.toString()

        expect:
            output.contains('fake-make build install DESTDIR=foo/bar') || output.contains('fake-make.bat build install DESTDIR=foo/bar')
    }
}
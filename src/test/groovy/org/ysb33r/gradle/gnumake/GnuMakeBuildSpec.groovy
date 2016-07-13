//
// ============================================================================
// (C) Copyright Schalk W. Cronje 2013-2016
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

import org.gradle.api.logging.LogLevel
import org.ysb33r.gradle.gnumake.internal.FakeExecutor
import spock.lang.*
import org.ysb33r.gradle.gnumake.GnuMakeBuild
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.internal.os.OperatingSystem

class GnuMakeBuildSpec extends spock.lang.Specification {

    Project project
    def gnumake
    boolean isWindows = OperatingSystem.current().isWindows()

    void setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin:'org.ysb33r.gnumake'
        gnumake = project.task('foomake', type: GnuMakeBuild )
    }

    def "Newly created Task will set executable to OS-specific value"() {
        expect:
        gnumake.executable == 'make'
    }

    def "Newly created Task will have empty command line"() {
        expect:
        gnumake.cmdArgs.size() == 0
    }

    def "alwaysMake adds -B" () {

        given:
        gnumake.alwaysMake = true
        gnumake.buildCmdArgs()

        expect:
        gnumake.cmdArgs.size() == 1
        gnumake.cmdArgs.contains('-B')
    }

    def "keepGoing adds -k" () {

        given:
        gnumake.keepGoing = true
        gnumake.buildCmdArgs()

        expect:
        gnumake.cmdArgs.size() == 1
        gnumake.cmdArgs.contains('-k')
    }

    def "environmentOverrides adds -e" () {

        given:
        gnumake.environmentOverrides = true
        gnumake.buildCmdArgs()

        expect:
        gnumake.cmdArgs.size() == 1
        gnumake.cmdArgs.contains('-e')
    }

    def "ignoreErrors adds -i" () {

        given:
        gnumake.ignoreErrors = true
        gnumake.buildCmdArgs()

        expect:
        gnumake.cmdArgs.size() == 1
        gnumake.cmdArgs.contains('-i')
    }

    def "jobs affect -j" () {

        when: "jobs == 1"
        gnumake.jobs = 1
        gnumake.buildCmdArgs()

        then: "-j will not be added"
        gnumake.cmdArgs.size() == 0

        when: "jobs < 1"
        gnumake.jobs = -3
        gnumake.buildCmdArgs()

        then: "-j will not be added"
        gnumake.cmdArgs.size() == 0

        when: "jobs > 1"
        gnumake.jobs = 4
        gnumake.buildCmdArgs()

        then: "-j will be added"
        gnumake.cmdArgs.size() == 2
        gnumake.cmdArgs[0] == '-j'
        gnumake.cmdArgs[1] == '4'
    }

    def "if makefile is empty -f is not added"() {
        given:
        gnumake.makefile = ''
        gnumake.buildCmdArgs()

        expect:
        gnumake.cmdArgs.size() == 0
    }

    def "if makefile is String add asis"() {
        given:
        gnumake.makefile = 'Xakefile'
        gnumake.buildCmdArgs()

        expect:
        gnumake.cmdArgs.size() == 2
        gnumake.cmdArgs[0] == '-f'
        gnumake.cmdArgs[1] == 'Xakefile'

    }

    def "if makefile is File then absolute path should not be added"() {
        given:
            gnumake.makefile = new File ('../../Makefile')
            gnumake.buildCmdArgs()

        expect:
            gnumake.cmdArgs.size() == 2
            gnumake.cmdArgs[0] == '-f'
            gnumake.cmdArgs[1] == (isWindows ? '..\\..\\Makefile' : '../../Makefile')
    }

    def "chDir affects -C"() {
        given:
            gnumake.chDir = './change/here'
            gnumake.buildCmdArgs()

        expect:
            gnumake.cmdArgs.size() == 2
            gnumake.cmdArgs[0] == '-C'
            gnumake.cmdArgs[1] == project.file('./change/here').absolutePath

    }

    def "includeDirs adds -I plus path"() {
        given:
          gnumake.includeDirs 'localDir', new File ('../FileObjectDir'), '/absolutePath'
          gnumake.buildCmdArgs()

        expect:
          gnumake.cmdArgs.size() == 6
          gnumake.cmdArgs[0] == '-I'
          gnumake.cmdArgs[1] == project.file('localDir').toString()
          gnumake.cmdArgs[2] == '-I'
          gnumake.cmdArgs[3] == project.file('../FileObjectDir').toString()
          gnumake.cmdArgs[4] == '-I'
          gnumake.cmdArgs[5] == project.file('/absolutePath').toString()

    }

    def "targets are added in order specified"() {
        given:
          gnumake.targets 'clean','install'
          gnumake.buildCmdArgs()

        expect:
          gnumake.cmdArgs.size() == 2
          gnumake.cmdArgs[0] == 'clean'
          gnumake.cmdArgs[1] == 'install'
    }

    def "flags are added in the format X=Y"() {
        given:
            gnumake.flags DESTDIR : '/path/somewhere', BUILD_NUMBER : 12345
            gnumake.buildCmdArgs()

        expect:
            gnumake.cmdArgs.size() == 2
            gnumake.cmdArgs.contains( "BUILD_NUMBER=12345${''}" )
            gnumake.cmdArgs.contains( "DESTDIR=/path/somewhere${''}" )
    }

    def "arbitrary switches are added at the end"() {
        given:
            gnumake.ignoreErrors = true
            gnumake.targets 'clean'
            gnumake.switches  '-X','-Y','-Z'
            gnumake.buildCmdArgs()

        expect:
            gnumake.cmdArgs.size() == 5
            gnumake.cmdArgs[2] == '-X'
            gnumake.cmdArgs[3] == '-Y'
            gnumake.cmdArgs[4] == '-Z'
    }

    def "Ordering is InternalSwitches + targets + flags + extraSwitches"() {
        given:
            gnumake.ignoreErrors = true
            gnumake.targets 'clean'
            gnumake.flags DESTDIR:'/path/somewhere'
            gnumake.switches  '-q', '-n'
            gnumake.buildCmdArgs()

        expect:
            gnumake.cmdArgs.join(' ') == "-i clean DESTDIR=/path/somewhere -q -n"
    }

    def "'tasks' is an alias for 'targets', therefore writing tasks, should update targets"() {
        given:
        gnumake.tasks = ['build','install']

        expect:
        gnumake.targets == [ 'build','install' ]
    }

    def "'tasks' is an alias for 'targets', therefore writing targets, tasks should reflect"() {
        given:
        gnumake.targets  'build','install'

        expect:
        gnumake.tasks == [ 'build','install' ]
    }

    def "BuildFile is an alias for Makefile, therefore writing buildFile, should update makefile"() {
        given:
        gnumake.buildFile = 'GNUMakefile'

        expect:
        gnumake.makefile == 'GNUMakefile'
    }

    def "BuildFile are an alias for Makefile, therefore writing makefile, buildFile should reflect"() {
        given:
        gnumake.makefile = 'GNUMakefile'

        expect:
        gnumake.buildFile == 'GNUMakefile'
    }

    def "Dir is an alias for chDir, therefore writing dir, should update chDir"() {
        given:
        gnumake.dir  '/path/to/somewhere'

        expect:
        gnumake.chDir == project.file('/path/to/somewhere')
    }

    def "Dir is an alias for chDir, therefore writing chDir, dir should reflect"() {
        given:
        gnumake.chDir '/path/to/somewhere'

        expect:
        gnumake.dir == project.file('/path/to/somewhere')
    }

    def "execArgs must follow executable directly before anything else"() {
        given:
          project.extensions.gnumake.executable 'fooMake'
          project.extensions.gnumake.execArgs   '-k', 'gmake'
          gnumake.jobs = 2
          gnumake.targets 'clean','install'
          gnumake.buildCmdArgs()

        expect:
          gnumake.cmdArgs.join(' ') == '-k gmake -j 2 clean install'
    }

    def "It is not necessary to set makefile"() {
        given:
        project.allprojects {
            make {
                executable 'foo'
                targets 'clean'
            }
        }

        project.tasks.make.executor = new FakeExecutor()
        project.evaluate()
        project.tasks.make.execute()

        expect:
        project.tasks.make.didWork
    }

    def "Monitor input sources and output folders"() {
        given:
        File srcDir = new File('src').absoluteFile
        File propsFile = new File('src/main/resources/META-INF/gradle-plugins/org.ysb33r.gnumake.properties').absoluteFile
        File outDir = new File(project.buildDir,'foo')

        project.allprojects {

            make {
                makeInputs {
                    dir srcDir
                }
                makeOutputs {
                    dir outDir
                }
            }
        }

        project.evaluate()

        expect: 'Files to be added to inputs and outputs'
        !project.tasks.make.inputs.files.isEmpty()
        project.tasks.make.inputs.files.files.contains(propsFile)
        !project.tasks.make.outputs.files.isEmpty()

    }
}


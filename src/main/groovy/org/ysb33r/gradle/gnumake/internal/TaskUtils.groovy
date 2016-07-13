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

package org.ysb33r.gradle.gnumake.internal

import org.gradle.api.Project
import org.gradle.process.ExecResult
import org.ysb33r.gradle.gnumake.tasks.GnuMakeBuild
import org.ysb33r.gradle.gnumake.model.GnuMakeExtension

/**
 * @author Schalk W. Cronjé
 */
class TaskUtils {
    static List<String> buildCmdArgs(Project project,GnuMakeBuild task,List<String> targets) {

        List<String> execArgs = []

        if (!task.noExecArgs) {
            execArgs = project.extensions.findByName(GnuMakeExtension.EXTENSION_NAME)?.execArgs ?: []
        }

        def switches = [
            [task.alwaysMake, '-B'],
            [task.environmentOverrides, '-e'],
            [task.ignoreErrors, '-i'],
            [task.keepGoing, '-k'],
            [task.jobs > 1, '-j', task.jobs.toString()],
            [task.makefile, '-f', "${task.makefile.toString()}"],
            [task.chDir, '-C', "${task.chDir?.absolutePath}" ],
        ].collectMany { it.head() ? it.tail() : [] }

        def includes = (task.includeDirs.files.collectMany { ['-I', "${it.absolutePath}"] })

        def flags = task.flags.collect { k, v -> "$k=$v" }

        execArgs + switches  + includes + targets + flags + task.switches
    }

    static ExecResult runMake(Project project,final String exec,final List<String> cmdargs,final File wd=null,Map env = null) {
        project.exec {

            executable = exec

            if (wd) {
                workingDir = wd
            }

            if(env) {
                environment env
            }

            args = cmdargs
        }

    }
}

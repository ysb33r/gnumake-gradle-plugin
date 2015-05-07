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

import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.StopActionException
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecResult
import org.ysb33r.gradle.gnumake.GnuMakeBuild

/**
 * @author Schalk W. Cronjé
 * @since 1.0
 */
@CompileStatic
class TrackerTask extends DefaultTask {

    /** Runs a single target only
     *
     */
    @Input
    String target

    /** The GnuMakeBuild task that is tracked.
     *
     */
    @Input
    String trackName

    /** Stores the result of running a make command */
    ExecResult execResult


    @TaskAction
    void exec() {

        assert trackName != null
        GnuMakeBuild tracks = project.tasks.getByName(trackName) as GnuMakeBuild

        if(!executor) {
            executor = new MakeExecutor(project)
        }
        def cmdargs = TaskUtils.buildCmdArgs(project,tracks,[target])
        execResult = executor.runMake(tracks.executable,cmdargs,tracks.workingDir,tracks.environment)

    }

    Executor  executor
}

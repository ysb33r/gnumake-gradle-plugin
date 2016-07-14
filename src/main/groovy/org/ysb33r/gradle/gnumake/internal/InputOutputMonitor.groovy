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

package org.ysb33r.gradle.gnumake.internal

import groovy.transform.CompileDynamic
import org.gradle.api.Task
import org.gradle.api.tasks.TaskInputs
import org.gradle.api.tasks.TaskOutputs

/** Use to select files and directories for monitoring by a GnuMakeBuild task
 *
 * @since 1.0
 *
 * @author Schalk W. Cronjé
 *
 */
@CompileDynamic
class InputOutputMonitor {

    /** Attaches the monitor to a task and select whether it will be for
     * inputs or outpus
     *
     * @param taskInOrOut Select a task.inputs or task.outputs.
     * @param inputs Set to {@code true} for inputs or {@code false} for outputs
     */
    InputOutputMonitor(Task taskInOrOut,final String inOrOut)  {
        this.ioTask  = taskInOrOut
        this.inOrOut = inOrOut
    }

    void dir(Object dirPath)    { ioTask."${inOrOut}".dir dirPath }
    void file(Object path)      { ioTask."${inOrOut}".file path   }
    void files(Object... paths) { ioTask."${inOrOut}".files paths }

    private Task ioTask
    private String inOrOut
}

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
import org.gradle.process.ExecResult

/**
 * @author Schalk W. Cronjé
 */
@CompileStatic
class FakeExecutor implements Executor {

    String executable
    List<String> cmdargs
    File workingDir
    Map environment

    ExecResult runMake(String exec, List<String> cmdargs, File wd,Map env) {
        this.executable = exec
        this.cmdargs = cmdargs
        this.workingDir = wd
        this.environment = env
        return new FakeExecResult()
    }
}

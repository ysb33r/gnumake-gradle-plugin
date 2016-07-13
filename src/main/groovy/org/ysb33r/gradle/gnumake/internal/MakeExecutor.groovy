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

import groovy.transform.TupleConstructor
import org.gradle.api.Project
import org.gradle.process.ExecResult

/**
 * @author Schalk W. Cronjé
 */
@TupleConstructor
class MakeExecutor implements Executor {
    Project project

    ExecResult runMake(final String exec,final List<String> cmdargs,final File wd=null) {
        TaskUtils.runMake(project,exec,cmdargs,wd)
    }
}

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

import org.gradle.process.ExecResult
import org.gradle.process.internal.ExecException

/**
 * @author Schalk W. Cronjé
 */
class FakeExecResult implements ExecResult {
    int getExitValue() { 0 }
    ExecResult assertNormalExitValue() throws ExecException { null }
    ExecResult rethrowFailure() throws ExecException { null }
}

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
import org.gradle.util.CollectionUtils

/**
 * Created by schalkc on 01/01/15.
 * @since 1.0
 */
class GnuMakeExtension {

    static final String EXTENSION_NAME = 'gnumake'

    String executable = 'make'
    String makefile

    void executable(final String exe ) {
        this.executable=exe
    }

    void makefile(final String mfile ) {
        this.makefile = mfile
    }

    GnuMakeExtension(Project p) {
        project = p

        switch(OperatingSystem.current()) {
            case OperatingSystem.SOLARIS:
            case OperatingSystem.FREE_BSD:
                executable = 'gmake'
                break;
            default:
                executable = 'make'
        }
    }


    /** Arguments that will be added to make invocations.
     *
     * @return List of executable arguments
     *
     * @see {@link #execArgs}
     */
    List<String> getExecArgs() {
        CollectionUtils.stringize(args)
    }

    /** Arguments that will be added to make invocations.
     * These arguments added to the front of the GnuMakeBuild tasks
     * arguments i.e. directly following the executable when run.
     * This is the recommended approach when running some kind of environment
     * preparation i.e runnign another tool which invokes make
     *
     * @code
     * gnumake {
     *     executable 'run_on_remote_build_server.sh'
     *     execArgs '--server', 'server.com', '/opt/pi/make'
     * }
     * @endcode
     *
    */
    void execArgs(Object... args) {
        this.args.addAll(args as List)
    }

    /** Default flags that are applied to all {#code GnuMakeBuild} tasks.
     *
     */
    Map defaultFlags = [:]

    private List<Object> args = []
    private Project project

}

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

package org.ysb33r.gradle.gnumake

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class GnuMakePluginSpec extends spock.lang.Specification {

    Project project = ProjectBuilder.builder().build()

    def "Can apply GnuMake plugin to project"() {

        when:
        project.apply plugin: 'org.ysb33r.gnumake'

        then:
        project.extensions.gnumake != null
        project.tasks.make instanceof GnuMakeBuild
    }

}


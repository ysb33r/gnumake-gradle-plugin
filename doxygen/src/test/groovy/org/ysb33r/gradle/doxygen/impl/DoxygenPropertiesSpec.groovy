// ============================================================================
// (C) Copyright Schalk W. Cronje 2014
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and limitations under the License.
//
// ============================================================================

package org.ysb33r.gradle.doxygen.impl

import spock.lang.Ignore
import spock.lang.Specification


/**
 * Created by schalkc on 21/05/2014.
 */
class DoxygenPropertiesSpec extends Specification {

    def DoxygenProperties dox = new DoxygenProperties()

    def "Setting a boolean to false creates a prop with 'NO' value"() {
        when:
            dox.setProperty('CREATE_SUBDIRS',false)

        then:
            dox.properties.CREATE_SUBDIRS == 'NO'
    }

    def "Setting a boolean to true creates a prop with 'YES' value"() {
        when:
            dox.setProperty('CREATE_SUBDIRS',true)

        then:
            dox.properties.CREATE_SUBDIRS == 'YES'
    }

    @Ignore
    def "Setting from File creates an absolutePath"() {

    }

    @Ignore
    def "Setting from File with spaces creates a quoted absolutePath"() {

    }

    @Ignore
    def "Setting from File[] creates space-separated list of absolute paths"() {

    }

}
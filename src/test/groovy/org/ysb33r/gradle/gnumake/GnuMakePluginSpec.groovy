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
import org.gradle.testfixtures.ProjectBuilder
import org.ysb33r.gradle.gnumake.model.GnuMakeExtension

class GnuMakePluginSpec extends spock.lang.Specification {

    Project project = ProjectBuilder.builder().build()

    void setup() {
        project.apply plugin: 'org.ysb33r.gnumake.model'
    }

    def "Can apply GnuMake plugin to project"() {

        when: "When the plugin is applied by id"
        def gnumake = project.modelRegistry.find('gnumake',GnuMakeExtension)

        then: "Expect a model of type GnuMakePlugin to exist"
        gnumake != null
        gnumake.makefile == null
        gnumake.executable.endsWith('make')
    }

    def "Configuring the model settings (declaritive)"() {

        given: "When the plugin is applied by id"

        when: "The executable & makefile settings are configured"
        project.allprojects {
            model {
                gnumake {
                    executable 'amake'
                    makefile 'GNUMakefile'
                }
            }
        }
        def gnumake = project.modelRegistry.find('gnumake',GnuMakeExtension)

        then: "The new values should be reflected on the model"
        gnumake?.executable == 'amake'
        gnumake?.makefile == 'GNUMakefile'
    }

    def "Configuring the model settings (assignment)"() {

        given: "When the plugin is applied by id"

        when: "The executable & makefile settings are configured"
        project.allprojects {
            model {
                gnumake {
                    executable = 'amake'
                    makefile = 'GNUMakefile'
                }
            }
        }
        def gnumake = project.modelRegistry.find('gnumake',GnuMakeExtension)

        then: "The new values should be reflected on the model"
        gnumake?.executable == 'amake'
        gnumake?.makefile == 'GNUMakefile'
    }

    def "Configuring the model settings (execArgs)"() {

        given: "When the plugin is applied by id"

        when: "The executable & makefile settings are configured"
        project.allprojects {
            model {
                gnumake {
                    execArgs 'a'
                    execArgs 'b', 'c'
                }
            }
        }
        def gnumake = project.modelRegistry.find('gnumake',GnuMakeExtension)

        then: "The new values should be reflected on the model"
        gnumake?.execArgs == ['a','b','c']
    }
}


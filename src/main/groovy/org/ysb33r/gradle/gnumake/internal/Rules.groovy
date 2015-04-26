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

/**
 * @author Schalk W. Cronjé
 */
class Rules {

    static void addRule(Project project,String taskBaseName ) {

        String ruleText = "Pattern: ${taskBaseName}<Target>: " +
                "Runs 'make <target>' with ${taskBaseName} arguments."


        project.tasks.addRule ruleText, { taskName ->
            def matcher = ( taskName =~ /${taskBaseName}([\p{Upper}\p{Digit}].+)/ )

            if (matcher.matches()) {
                def target = matcher[0][1][0].toLowerCase() + matcher[0][1][1..-1]
                TrackerTask linkedTask = project.tasks.create(taskName,TrackerTask)
                linkedTask.description = "Runs 'make ${target}' with ${taskBaseName} attributes"
                linkedTask.trackName = taskBaseName
                linkedTask.target = target
            }
        }
    }
}

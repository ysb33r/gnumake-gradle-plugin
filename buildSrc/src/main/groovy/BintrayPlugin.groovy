// ============================================================================
// (C) Copyright Schalk W. Cronje 2013
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and limitations under the License.
//
// ============================================================================

package org.ysb33r.gradle.bintray

// http://mrhaki.blogspot.co.uk/2010/09/gradle-goodness-define-short-plugin-id.html
// https://github.com/Ullink/gradle-repositories-plugin/blob/master/src/main/groovy/com/ullink/RepositoriesPlugin.groovy
import org.gradle.api.*
import org.apache.ivy.plugins.resolver.DependencyResolver

class BintrayPlugin implements Plugin<Project> {
    void apply(Project project) {
        addJCenter(project)
        addBintray(project)
    }
    
    /** Adds jCenter() to repositories {} 
     */
    static boolean addJCenter(Project project) {
        if (!project.repositories.metaClass.respondsTo(project.repositories, 'jCenter')) {
            project.logger.debug 'Adding jCenter() to project RepositoryHandler'
            
            project.repositories.metaClass.jCenter = {
                project.repositories.mavenRepo( name : 'BintrayJCenter', url : 'http://jcenter.bintray.com' )
            }
        }
        return true
    }
    
    /** Adds bintray( 'username', 'repository', Closure? ) to repositories {} 
     */
    static boolean addBintray(Project project) {
        if (!project.repositories.metaClass.respondsTo(project.repositories, 'bintray',String,String,Object)) {
            project.logger.debug 'Adding bintray(String,String,Closure?) to project RepositoryHandler'
            
            project.repositories.metaClass.bintray = { String repoUser,String repoName,Closure cfg=null -> 
                
            }
        }
        return false
    }
} 

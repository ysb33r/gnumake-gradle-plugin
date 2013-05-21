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
    
    static final String BINTRAY_DOWNLOAD_URL = 'http://dl.bintray.com';
    
    void apply(Project project) {
        addJCenter(project)
        addBintray(project)
    }
 
    static def bintrayRepoName( def repoOwner,def repoName ) {
        "bintray:${repoOwner}:${repoName}"
    }
    
    /** Adds jCenter() to repositories {} 
     */
    static void addJCenter(Project project) {
        if (!project.repositories.metaClass.respondsTo(project.repositories, 'jCenter')) {
            project.logger.debug 'Adding jCenter() to project RepositoryHandler'
            
            project.repositories.metaClass.jCenter = { ->
                project.repositories.mavenRepo( name : 'BintrayJCenter', url : 'http://jcenter.bintray.com' )
            }
        }
    }
    
    /** Adds ivyBintray( 'username', 'repository', Closure? ) and mavenBintray( ... ) to repositories {} 
     */
    static void addBintray(Project project) {

        def extname = 'ivyBintray'
        if (!project.repositories.metaClass.respondsTo(project.repositories, extname,String,String,Object)) {
            project.logger.debug "Adding ${extname}(String,String,Closure?) to project RepositoryHandler"
            
            project.repositories.metaClass."${extname}" = { String repoOwner,String repoName ,Closure cfg=null -> 
                project.repositories.ivy {
                    name = bintrayRepoName(repoOwner,repoName)
                    url = "${BINTRAY_DOWNLOAD_URL}/${repoOwner}/${repoName}"
                    if(cfg) { cfg.call() }
                }
                 
            }

        }
        
        extname = 'mavenBintray'
        if (!project.repositories.metaClass.respondsTo(project.repositories, extname,String,String,Object)) {
            project.logger.debug "Adding ${extname}(String,String,Closure?) to project RepositoryHandler"
            
            project.repositories.metaClass."${extname}" = { String repoOwner,String repoName ,Closure cfg=null -> 
                project.repositories.mavenRepo( 
                    name : bintrayRepoName(repoOwner,repoName),
                    url : "${BINTRAY_DOWNLOAD_URL}/${repoOwner}/${repoName}",
                    cfg
                )
                 
            }
        }
    }
} 

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

import org.gradle.api.*
import org.apache.ivy.plugins.resolver.DependencyResolver

class BintrayPlugin implements Plugin<Project> {
    
    static final String BINTRAY_DOWNLOAD_URL = 'http://dl.bintray.com'
    static final String BINTRAY_JCENTER_URL = 'http://jcenter.bintray.com'
    
    void apply(Project project) {
        addJCenter(project)
        addBintray(project)
    }
 
    static def bintrayRepoName( def repoOwner,def repoName ) {
        "bintray:${repoOwner}:${repoName}"
    }
    
    static def bintrayRepoUrl( def repoOwner, def repoName ) {
        "${BINTRAY_DOWNLOAD_URL}/${repoOwner}/${repoName}"
    }
    
    /** Adds jCenter() to repositories {} 
     */
    static void addJCenter(Project project) {
        if (!project.repositories.metaClass.respondsTo(project.repositories, 'jCenter')) {
            project.logger.debug 'Adding jCenter() to project RepositoryHandler'
            
            project.repositories.metaClass.jCenter = { ->
                project.repositories.mavenRepo( name : 'bintrayJCenter', url : BINTRAY_JCENTER_URL )
            }
        }
    }
    
    /** Adds ivyBintray( 'username', 'repository', Closure? ) and mavenBintray( ... ) to repositories {} 
     */
    static void addBintray(Project project) {

        final def ivyName = 'ivyBintray'
        if (!project.repositories.metaClass.respondsTo(project.repositories, ivyName,String,String,Closure)) {
            project.logger.debug "Adding ${ivyName}(String,String,Closure?) to project RepositoryHandler"
            
            project.repositories.metaClass."${ivyName}" = { String repoOwner,String repoName ,Closure cfg=null -> 
                project.repositories.ivy {
                    name = bintrayRepoName(repoOwner,repoName)
                    url = bintrayRepoUrl(repoOwner,repoName)
                    if(cfg) { cfg.call() }
                }
                 
            }
            
        }
        
        final def mavenName = 'mavenBintray'
        if (!project.repositories.metaClass.respondsTo(project.repositories, mavenName,String,String,Closure)) {
            project.logger.debug "Adding ${mavenName}(String,String,Closure?) to project RepositoryHandler"
            
            project.repositories.metaClass."${mavenName}" = { String repoOwner,String repoName ,Closure cfg=null -> 
                project.repositories.mavenRepo( 
                    name : bintrayRepoName(repoOwner,repoName),
                    url : bintrayRepoUrl(repoOwner,repoName),
                    cfg
                )   
            }
        }
    }
} 

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


/**
 * 
 * @author schalkc
 * 
 * @code
 * uploadArchives {
 *   repositories {
 *     bintrayMavenDeployer {
 *         username = 'ysb33r'
 *         password = bintray_api_key
 *         repoOwner = 'ysb33r'
 *         repoName = 'grysb33r'
 *         package = 'gnumake-gradle-plugin'
 *         description = 'This is an example to simplifying bintray publishing'
 *         descUrl = 'https://github.com/ysb33r/Gradle/blob/master/buildSrc/src/main/groovy/BintrayPackage.groovy'
 *         tags = ['gradle']
 *   // Listing version here, but should really be determined from project version
 *         version = '0.0.3'
 *       }
 *     }
 *   }
 * @endcode
 */

class BintrayPublishPlugin implements Plugin<Project> {

    void apply(Project project) {
        Task createBintrayMetadata = addBintrayPackageTask (project)
        addMavenDeployer (project, createBintrayMetadata) 
        //TODO: addIvyDeployer project 
    }
    
    static Task addBintrayPackageTask (Project project ) {
        def chkTask= project.getTasksByName('createBintrayMetadata',false)
        chkTask ? chkTask[0] : (
            project.tasks.create ( name : "createBintrayMetadata", type : BintrayPackage   )
        )
    }
   
    static void addMavenDeployer(Project project,Task createBintrayMetadata ) {
        if (!project.repositories.metaClass.respondsTo(project.repositories, 'bintrayMavenDeployer',Closure)) {
            project.logger.debug 'Adding bintrayMavenDeployer{} to project RepositoryHandler'
            
            // BUG: This is not correct. Somehow I need to book into uploadArchives.repositories
            project.repositories.metaClass.bintrayMavenDeployer = { final Closure c->
                
                Closure config = c.clone()
                config.delegate = createBintrayMetadata
                config()

                project.repositories.mavenDeployer( url: createBintrayMetadata.mavenUrl ) {
                    authentication(userName: createBintrayMetadata.username, password: createBintrayMetadata.apiKey)
                }
            }
        }
    }
}

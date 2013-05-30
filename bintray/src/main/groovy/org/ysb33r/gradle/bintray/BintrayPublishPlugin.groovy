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
import org.gradle.api.tasks.Upload

/**
 * 
 * @author schalkc
 * 
 * @code
 * uploadArchives {
 *   repositories {
 *     bintrayMavenDeployer {
 *         username = 'ysb33r'
 *         apiKey = bintray_api_key
 *         repoOwner = 'ysb33r'
 *         repoName = 'grysb33r'
 *         packageName = 'gnumake-gradle-plugin'
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
        addMavenDeployer (project) 
        //TODO: addIvyDeployer project 
    }
    
    static def addBintrayPackageTask (Project project,String taskName  ) {
                    // TODO: What if the task already exists    
        def chkTask= project.getTasksByName(taskName,false)
        chkTask ? chkTask[0] : (
            project.tasks.create ( name : "${taskName}", type : BintrayPackage   )
        )
        project.logger.debug "Added task ${taskName}"
        project.tasks."${taskName}"
    }
   
    static void addMavenDeployer(Project project ) {
        if (!project.repositories.metaClass.respondsTo(project.repositories, 'bintrayMavenDeployer',Closure)) {
           
            project.tasks.withType( Upload.class ).all { uploadTask ->
                uploadTask.repositories.metaClass.bintrayMavenDeployer << { final Closure c ->
                    
                    project.apply plugin : 'maven'
                    
                    def bintrayMetadata = addBintrayPackageTask(project,"bintrayMetadata_${uploadTask.name}")
                    //project.tasks.create ( name : "bintrayMetadataFor${uploadTask.name}", type : BintrayPackage)

                    Closure config = c.clone()
                    config.delegate = bintrayMetadata
                    config()
                    
                    uploadTask.dependsOn bintrayMetadata
                  
                    uploadTask.repositories {
                        mavenDeployer {
                            repository ( url: bintrayMetadata.mavenUrl() ) {
                                authentication(userName: bintrayMetadata.username, password: bintrayMetadata.apiKey)
                            }
                        }
                    }
                }
                
                project.logger.debug "Added bintrayMavenDeployer to Upload task ${uploadTask.name}"
            }
        }
    }
}

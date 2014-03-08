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
        addIvyDeployer project 
        // TODO: addMavenPublisher project
        addIvyPublisher project
    }
    
    static def addBintrayPackageTask (Project project,final String initialTaskName  ) {
        // TODO: What if the task already exists
        int index=0 
        String taskName=initialTaskName
        
        while(true) {
            def chkTask= project.getTasksByName(taskName,false)
        
            if (chkTask) {
                ++index
                taskName = "${initialTaskName}_${index}"    
            } else {
                project.tasks.create ( name : "${taskName}", type : BintrayPackage   )
                project.logger.debug "Added task ${taskName}"
                return project.tasks."${taskName}"
            }
        }         
    }
   
    static void addMavenDeployer(Project project ) {
        if (!project.repositories.metaClass.respondsTo(project.repositories, 'bintrayMavenDeployer',Closure)) {
           
            project.tasks.withType( Upload.class ).all { uploadTask ->
                uploadTask.repositories.metaClass.bintrayMavenDeployer << { final Closure c ->
                    
                    project.apply plugin : 'maven'
                    
                    def bintrayMetadata = addBintrayPackageTask(project,"bintrayMetadata_${uploadTask.name}")

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
    
    static void addIvyDeployer(Project project ) {
        if (!project.repositories.metaClass.respondsTo(project.repositories, 'bintrayIvyDeployer',Closure)) {
           
            project.tasks.withType( Upload.class ).all { uploadTask ->
                uploadTask.repositories.metaClass.bintrayIvyDeployer << { final Closure c ->
                    
                    def bintrayMetadata = addBintrayPackageTask(project,"bintrayMetadata_${uploadTask.name}")

                    Closure config = c.clone()
                    config.delegate = bintrayMetadata
                    config()
                    
                    uploadTask.dependsOn bintrayMetadata
                  
                    uploadTask.repositories {
                        ivy {
                            url bintrayMetadata.ivyUrl (project.version)
                            
                            credentials {
                                username bintrayMetadata.username
                                password bintrayMetadata.apiKey
                            }                        }
                    }
                }
                
                project.logger.debug "Added bintrayIvyDeployer to Upload task ${uploadTask.name}"
            }
        }
    }
    
    @Incubating
    static void addIvyPublisher(Project project) {
    
        Closure taskModification = { uploadTask ->
            
            uploadTask.repositories.metaClass.bintrayIvyDeployer << { final Closure c ->

                def bintrayMetadata = addBintrayPackageTask(project,"bintrayMetadata_${uploadTask.name}")

                Closure config = c.clone()
                config.delegate = bintrayMetadata
                config()

                uploadTask.dependsOn bintrayMetadata

                uploadTask.repositories {
                    ivy {
                        url bintrayMetadata.ivyUrl (project.version)

                        credentials {
                            username bintrayMetadata.username
                            password bintrayMetadata.apiKey
                        }
                    }
                }    
                project.logger.debug "Added bintrayIvyDeployer to task ${uploadTask.name}"
            }
        }
        
        if( project.plugins.hasPlugin('ivy-publish') ) {
            if (!project.publishing.repositories.metaClass.respondsTo(project.publishing.repositories, 'bintrayIvyDeployer',Closure)) {
                project.tasks.withType( org.gradle.api.publish.ivy.tasks.PublishToIvyRepository.class ).all taskModification
            }    
        } else {
            project.plugins.whenPluginAdded { plugin ->
                if( project.plugins.hasPlugin('ivy-publish') ) {
                    if (!project.publishing.repositories.metaClass.respondsTo(project.publishing.repositories, 'bintrayIvyDeployer',Closure)) {
                        project.tasks.withType( org.gradle.api.publish.ivy.tasks.PublishToIvyRepository.class ).all taskModification
                    }
                }
            }
        }     
    }
        
}        
            
//            project.apply plugin: 'ivy-publish'
            
//            project.tasks.withType( org.gradle.api.publish.ivy.tasks.PublishToIvyRepository.class ).all { uploadTask ->
//                
//              
//                    uploadTask.repositories.metaClass.bintrayIvyDeployer << { final Closure c ->
//                        
//                        def bintrayMetadata = addBintrayPackageTask(project,"bintrayMetadata_${uploadTask.name}")
//    
//                        Closure config = c.clone()
//                        config.delegate = bintrayMetadata
//                        config()
//                        
//                        uploadTask.dependsOn bintrayMetadata
//                      
//                        uploadTask.repositories {
//                            ivy {
//                                url bintrayMetadata.ivyUrl (project.version)
//                                
//                                credentials {
//                                    username bintrayMetadata.username
//                                    password bintrayMetadata.apiKey
//                                }                        
//                        }
//                    
//                    project.logger.debug "Added bintrayIvyDeployer to Upload task ${uploadTask.name}"
//                }
//            
//    
//            }
//        }    
//    }
//  }  



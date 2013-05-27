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

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional


// FROM: https://github.com/bintray/bintray-examples/blob/master/gradle-example/build.gradle
class BintrayPackage extends DefaultTask {
    
    /** Bintray username
     * 
     */
    @Input
    String username
    
    /* Bintray API Key
     * 
     */
    @Input
    String apiKey
    
    /** 
     * Bintray URL. Usually no need to set this unless testing. 
     */
    @Input
    @Optional
    def apiBaseUrl = 'https://api.bintray.com'
    
    /** Name of an existing repository
     * 
     */
    @Input
    String repoName
    
    /** Name of the user that created the repository. If not specified, then
     * task will use @b username instead.
     */
    @Input
    @Optional
    String repoOwner
    
    /** Package name where artefacts will be published to.
     * 
     */
    @Input
    String packageName
    
    /** Package textual description
     * 
     */
    @Input
    @Optional
    String description = ''
    
    /** URL to another document containing the package description
     * 
     */
    @Input
    @Optional
    def descUrl = ''
    
    /** Tags to apply to package
     * 
     */
    @Input
    @Optional
    def tags = []
    
    def getSource() {
        repoOwner ?: username
    }
    
    def mavenUrl(def moduleName = null) {
        "${apiBaseUrl}/maven/${source}/${repoName}/${moduleName?:packageName}"
    }
    
    def ivyUrl(def moduleName=null,def moduleVersion) {
        "${apiBaseUrl}/content/${source}/${repoName}/${moduleName?:packageName}/${moduleVersion}"
    }
    
    @TaskAction
    def createPackage() {
        def repoPath = '/packages/' + source + '/' + repoName
        def http = new HTTPBuilder(apiBaseUrl)
        http.auth.basic username, apiKey
        http.request(GET, JSON) {
            uri.path = repoPath + '/' + packageName
            response.'404' = {
                http = new HTTPBuilder(apiBaseUrl)
                http.auth.basic username, apiKey
                http.request(POST, JSON) {
                    uri.path = repoPath
                    body = [name: packageName, desc: description, desc_url: descUrl, labels: tags]
    
                    response.success = { resp ->
                        println 'Created!!!'
                    }
                }
            }
        }
    }
}





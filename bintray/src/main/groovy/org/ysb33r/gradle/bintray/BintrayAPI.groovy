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

import groovy.transform.*
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseException
import static groovyx.net.http.ContentType.JSON


// This code is WIP - I am planning to extend access
// to the Bintray API by manner of Gradle tasks
//@TupleConstructor
class BintrayAPI {

    final String API_BASE_URL = 'https://api.bintray.com'

    String baseUrl = API_BASE_URL
    String repoOwner
    String repoName
    String packageName
    String version
    String userName
    String apiKey
    def logger

    def createPackage () {
       // POST /packages/:subject/:repo
       // { name: '', desc: '', labels : [] }
       // 201 
    }

    boolean hasVersion() {
        assertVersionAttributes()
        def rest = client("packages/${repoOwner}/${repoName}/${packageName}")

        try {
            def response = rest.get( path : "versions/${version}"  )
            debugmsg "Response code is ${response.status}. Assuming ${version} exists"
            return response.isSuccess()
        } catch (HttpResponseException e) {
            debugmsg e.message
            if(e.response.status != 404) {
                throw e
            }

            debugmsg "Response code is ${e.response.status}. Assuming ${version} does not exist"
            return false
        }
    }

    def createVersion ( String description ) {
        assertVersionAttributes()

        def rest = client("packages/${repoOwner}/${repoName}/${packageName}")

        debugmsg "About to create ${repoOwner}/${repoName}/${packageName}/${version}"
        def response = rest.post(
                contentType : JSON,
                path : "versions",
                body : [ name : version, desc : description ]
        )
        debugmsg "${repoOwner}/${repoName}/${packageName}/${version}: ${response.status}"
        return response.isSuccess()
    }

    boolean deleteVersion() {
        assertVersionAttributes()
        def rest = client("packages/${repoOwner}/${repoName}/${packageName}")

        try {
            def response = rest.delete( path : "versions/${version}"  )
            return response.isSuccess()
        } catch (HttpResponseException e) {

            if(e.response.status != 404) {
                throw e
            }

            return false
        }
    }

//    Can only be supported once HTTPBuilder supports PATCH (0.8.0)
    def updateVersion () {
        return true
//        assertVersionAttributes()
//        def rest = client("packages/${repoOwner}/${repoName}/${packageName}")
//        try {
//            def response = rest.patch(
//                    contentType : JSON,
//                    path : "versions/${packageVersion}",
//                    body : [ name : packageVersion, desc : description ]
//            )
//            return response.isSuccess()
//        } catch (HttpResponseException e) {
//
//            if(e.response.status != 404) {
//                throw e
//            }
//
//            return false
//        }
    }

    private RESTClient client(String path) {
        RESTClient rest = new RESTClient("${baseUrl}/${path}/")
        rest.auth.basic userName,apiKey
        return rest
    }

    private def assertVersionAttributes() {
        assert baseUrl?.size()
        assert repoOwner?.size()
        assert repoName?.size()
        assert packageName?.size()
        assert version?.size()
        assert userName?.size()
        assert apiKey?.size()
    }

    private void debugmsg( String msg ) {
        logger?.debug msg
    }
}





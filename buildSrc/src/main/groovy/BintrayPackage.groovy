// ============================================================================
// (C) Copyright Schalk W. Cronje 2013
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
// ============================================================================

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.process.ExecResult

// FROM: https://github.com/bintray/bintray-examples/blob/master/gradle-example/build.gradle
class BintrayPackage extends DefaultTask {
    
    @Input
    String username
    
    @Input
    String apiKey
    
    @Input
    def apiBaseUrl = 'https://api.bintray.com'
    
    @Input
    String repository
    
    @Input
    String packageName
    
    @Input
    @Optional
    String description = ''
    
    @Input
    @Optional
    def descUrl = ''
    
    @Input
    @Optional
    def tags = []
    
    @TaskAction
    def createPackage() {
        def http = new HTTPBuilder(apiBaseUrl)
        http.auth.basic username, apiKey
        http.request(GET, JSON) {
            uri.path = '/packages/' + username + '/' + repository + '/' + packageName
            response.'404' = {
                http = new HTTPBuilder(apiBaseUrl)
                http.auth.basic username, apiKey
                http.request(POST, JSON) {
                    uri.path = '/packages/' + username + '/' + repository
                    body = [name: packageName, desc: description, desc_url: descUrl, labels: tags]
    
                    response.success = { resp ->
                        println 'Created!!!'
                    }
                }
            }
        }
    }
}





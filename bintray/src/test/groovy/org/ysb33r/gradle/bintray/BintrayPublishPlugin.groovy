package org.ysb33r.gradle.bintray

import spock.lang.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class BintrayPublishPluginSpec extends Specification {
            
    
    def "Upload Task must support 'bintrayMavenDeployer' under repositories"() {
        given:
            def Project project = ProjectBuilder.builder().build()
            project.apply plugin : 'bintray-publish'
            project.apply plugin : 'java'
            
            project.uploadArchives {
                repositories {
                    bintrayMavenDeployer {
                        username    'someUser'
                        apiKey      'some_bintray_api_key_not_valid'
                        repoOwner   'someOwner'
                        repoName    'someRepo'
                        packageName 'somePackage'
                        description 'This is a unittest for bintray maven publishing'
                        descUrl     'http://somesite.example'
                       // tags        ['gradle','bintray','spock']
                    }
                }
            }
   println project.uploadArchives.repositories         
            
        expect:
            project.tasks.bintrayMetadata_uploadArchives != null
            project.tasks.bintrayMetadata_uploadArchives.username == 'someUser'
            project.tasks.bintrayMetadata_uploadArchives.apiKey == 'some_bintray_api_key_not_valid'
            project.uploadArchives.repositories.size() == 1
    }
}
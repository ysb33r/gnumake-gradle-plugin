package org.ysb33r.gradle.bintray

import spock.lang.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class BintrayPublishPluginSpec extends Specification {
            
    def "Plugin should create a task called 'createBintrayMetdata'" () {
        given:
            def Project project = ProjectBuilder.builder().build()
            project.apply plugin : 'bintray-publish'
        
        expect:
            project.tasks.createBintrayMetadata != null
            project.tasks.'createBintrayMetadata' instanceof BintrayPackage
        
    }
    
    def "uploadArchives must support 'bintrayMavenDeployer' under repositories"() {
        given:
            def Project project = ProjectBuilder.builder().build()
            project.apply plugin : 'bintray-publish'
            project.apply plugin : 'java'
            
            project.uploadArchives {
                repositories {
                    bintrayMavenDeployer {
                        username  'someUser'
                        password  'some_bintray_api_key_not_valid'
                        repoOwner 'someOwner'
                        repoName  'someRepo'
                        'package' 'somePackage'
                        description 'This is a unittest for bintray maven publishing'
                        descUrl = 'http://somesite.example'
                        tags = ['gradle','bintray','spock']
                    }
                }
            }
            
        expect:
            project.uploadArchives instanceof boolean
    }
}
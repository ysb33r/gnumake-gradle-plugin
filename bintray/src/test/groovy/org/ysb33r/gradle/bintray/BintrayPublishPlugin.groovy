package org.ysb33r.gradle.bintray

import groovy.mock.interceptor.Ignore
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
                        tags        'gradle','bintray','spock'
                    }
                }
            }
            
        expect:
            project.uploadArchives.repositories.size() == 1
            project.tasks.bintrayMetadata_uploadArchives != null
            project.tasks.bintrayMetadata_uploadArchives.username == 'someUser'
            project.tasks.bintrayMetadata_uploadArchives.apiKey == 'some_bintray_api_key_not_valid'
    }
    
    def "Upload Task must support 'bintrayIvyDeployer' under repositories"() {
        given:
            def Project project = ProjectBuilder.builder().build()
            project.apply plugin : 'bintray-publish'
            project.apply plugin : 'java'
            
            project.uploadArchives {
                repositories {
                    bintrayIvyDeployer {
                        username    'someUser'
                        apiKey      'some_bintray_api_key_not_valid'
                        repoOwner   'someOwner'
                        repoName    'someRepo'
                        packageName 'somePackage'
                        description 'This is a unittest for bintray maven publishing'
                        descUrl     'http://somesite.example'
                       tags         'gradle','bintray','spock'
                    }
                }
            }
            
        expect:
            project.uploadArchives.repositories.size() == 1
            project.tasks.bintrayMetadata_uploadArchives != null
            project.tasks.bintrayMetadata_uploadArchives.username == 'someUser'
            project.tasks.bintrayMetadata_uploadArchives.apiKey == 'some_bintray_api_key_not_valid'
    }
    
    def "Upload task must handle more than one repository"() {
        given:
            def Project project = ProjectBuilder.builder().build()
            project.apply plugin : 'bintray-publish'
            project.apply plugin : 'java'
            
            project.uploadArchives {
                repositories {
                    bintrayMavenDeployer {
                        username    'someUser1'
                        apiKey      'some_bintray_api_key_not_valid1'
                        repoOwner   'someOwner1'
                        repoName    'someRepo1'
                        packageName 'somePackage1'
                    }
                    bintrayIvyDeployer {
                        username    'someUser2'
                        apiKey      'some_bintray_api_key_not_valid2'
                        repoOwner   'someOwner2'
                        repoName    'someRepo2'
                        packageName 'somePackage2'
                    }
                }
            }
        expect:
            project.uploadArchives.repositories.size() == 2
            project.tasks.bintrayMetadata_uploadArchives != null
            project.tasks.bintrayMetadata_uploadArchives.username == 'someUser1'
            project.tasks.bintrayMetadata_uploadArchives.apiKey == 'some_bintray_api_key_not_valid1'

            project.tasks.bintrayMetadata_uploadArchives_1 != null
            project.tasks.bintrayMetadata_uploadArchives_1.username == 'someUser2'
            project.tasks.bintrayMetadata_uploadArchives_1.apiKey == 'some_bintray_api_key_not_valid2'

    }

//    def "New publishing extension must support mavenIvyDeployer"() {
//        given:
//            def Project project = ProjectBuilder.builder().build()
//            project.apply plugin : 'ivy-publish'
//            project.apply plugin : 'bintray-publish'
//
//            project.publishing {
//                repositories {
//                    bintrayIvyDeployer {
//                        username    'someUser'
//                        apiKey      'some_bintray_api_key_not_valid'
//                        repoOwner   'someOwner'
//                        repoName    'someRepo'
//                        packageName 'somePackage'
//                        description 'This is a unittest for bintray maven publishing'
//                        descUrl     'http://somesite.example'
//                       tags         'gradle','bintray','spock'
//                    }
//                }
//            }
//
//       expect:
//           project.publishing.repositories.size() == 1
//    }
    
    

}
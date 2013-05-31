package org.ysb33r.gradle.bintray

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional



import spock.lang.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class BintrayPackageSpec extends Specification {

    def Project project = ProjectBuilder.builder().build()
    def bintrayPkg = project.tasks.create(name : 'bintray', type: BintrayPackage )

    def "Default initialisation"() {
    
        expect:
            bintrayPkg.username == null
            bintrayPkg.apiKey == null
            bintrayPkg.apiBaseUrl == 'https://api.bintray.com'
            bintrayPkg.repoName == null
            bintrayPkg.repoOwner == null
            bintrayPkg.packageName == null
            bintrayPkg.description == ''
            bintrayPkg.descUrl == ''
            bintrayPkg.tags == []
        
    }
    
    def "Configuring the BintrayPackage task, should set all of the appropriate fields"() {
        given:
            bintrayPkg.with {
                username  'me'   
                apiKey      '0000111122223333'
                repoName    'repoForMe'
                repoOwner   'MeMeMe'
                packageName 'foo'
                description 'some description'
                descUrl     'http://foo'
             }    
            
        expect:
            bintrayPkg.mavenUrl()    == 'https://api.bintray.com/maven/MeMeMe/repoForMe/foo'
            bintrayPkg.ivyUrl('3.0.0') == 'https://api.bintray.com/content/MeMeMe/repoForMe/foo/3.0.0' 
            bintrayPkg.mavenUrl('new-module') == 'https://api.bintray.com/maven/MeMeMe/repoForMe/new-module'
            bintrayPkg.ivyUrl('new-module','3.0.0') == 'https://api.bintray.com/content/MeMeMe/repoForMe/new-module/3.0.0'
            
            bintrayPkg.username    == 'me'
            bintrayPkg.apiKey      == '0000111122223333'
            bintrayPkg.repoName    == 'repoForMe'
            bintrayPkg.repoOwner   == 'MeMeMe'
            bintrayPkg.packageName == 'foo'
            bintrayPkg.description == 'some description'
            bintrayPkg.descUrl     == 'http://foo'
            bintrayPkg.source      == 'MeMeMe'
    }
    
    def "When repoOwner not supplied, default to username"() {
        given:
            bintrayPkg.with {
                username    'me'   
                apiKey      '0000111122223333'
                repoName    'repoForMe'
                packageName 'foo'
                description 'some description'
                descUrl     'http://foo'
             }    

        expect:
            bintrayPkg.mavenUrl()    == 'https://api.bintray.com/maven/me/repoForMe/foo'
            bintrayPkg.ivyUrl('3.0.0') == 'https://api.bintray.com/content/me/repoForMe/foo/3.0.0' 
            bintrayPkg.mavenUrl('new-module') == 'https://api.bintray.com/maven/me/repoForMe/new-module'
            bintrayPkg.ivyUrl('new-module','3.0.0') == 'https://api.bintray.com/content/me/repoForMe/new-module/3.0.0'
            
            bintrayPkg.username    == 'me'
            bintrayPkg.apiKey      == '0000111122223333'
            bintrayPkg.repoName    == 'repoForMe'
            bintrayPkg.repoOwner   == null
            bintrayPkg.packageName == 'foo'
            bintrayPkg.description == 'some description'
            bintrayPkg.descUrl     == 'http://foo'
            bintrayPkg.source      == 'me'
    }
    
    def "A tag can be a single string"() {
        given: 
            bintrayPkg.with {
                tags 'bintray'
            }
            
        expect:
            bintrayPkg.tags == ['bintray']
    }
    
    def "Tags can be a list of parameters"() {
        given: 
            bintrayPkg.with {
                tags 'bintray','gradle'
            }
            
        expect:
            bintrayPkg.tags == ['bintray','gradle']
    }
    
}
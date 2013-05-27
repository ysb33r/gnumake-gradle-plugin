package org.ysb33r.gradle.bintray

import spock.lang.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class BintrayPluginSpec extends Specification {
    
    def "Bintray plugin static test verification"() {
    
        expect:
            BintrayPlugin.BINTRAY_DOWNLOAD_URL == 'http://dl.bintray.com'
            BintrayPlugin.BINTRAY_JCENTER_URL == 'http://jcenter.bintray.com'

            BintrayPlugin.bintrayRepoName( 'ysb33r','grysb33r' ) == "bintray:ysb33r:grysb33r"
            BintrayPlugin.bintrayRepoUrl( 'ysb33r','grysb33r' ) ==  "http://dl.bintray.com/ysb33r/grysb33r"
        
    }
    
    def "Plugin should load if apply plugin : 'bintray' is used" () {
        given:
            Project project = ProjectBuilder.builder().build()
            project.apply plugin : 'bintray'
        
        expect:
            project.repositories.metaClass.respondsTo(project.repositories, 'jCenter')
            project.repositories.metaClass.respondsTo(project.repositories, 'mavenBintray',String,String,Closure)
            project.repositories.metaClass.respondsTo(project.repositories, 'ivyBintray',String,String,Closure)
            
    }
    
    def "When using jCenter() expect jCenter to be added to repo list" () {
        given:
            Project project = ProjectBuilder.builder().build()
            project.apply plugin : 'bintray'
            int repCount = project.repositories.size() 
            project.repositories {
                jCenter()
            }
            
        expect:
            repCount + 1 == project.repositories.size()
            project.repositories.bintrayJCenter != null
    }
    
    def "Adding a read-only Maven repository from Bintray" () {
        given:
            Project project = ProjectBuilder.builder().build()
            project.apply plugin : 'bintray'
            final def repCount = project.repositories.size()
            project.repositories {
                mavenBintray( 'ysb33r','grysb33r' ) 
            }
            
        expect:
            repCount + 1 == project.repositories.size()
            project.repositories."${BintrayPlugin.bintrayRepoName('ysb33r','grysb33r')}" != null
    }
    

}
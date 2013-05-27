package org.ysb33r.gradle.bintray

import spock.lang.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class BintrayPublishPluginSpec extends Specification {
    
    def Project project = ProjectBuilder.builder().build()
    
    def "Plugin should load if apply plugin : 'bintray-publish' is used" () {
        given:
            1 //project.apply plugin : 'bintray-publish'
        
        expect:
            project.tasks.createBintrayMetadata not null
            //project.tasks.'createBintrayMetadata' instanceof BintrayPackage
            //project.tasks.'createBintrayMetadata' instanceof BintrayPakage
        
    }
}
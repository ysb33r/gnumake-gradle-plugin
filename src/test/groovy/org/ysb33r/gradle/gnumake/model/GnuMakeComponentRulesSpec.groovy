package org.ysb33r.gradle.gnumake.model

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification


/**
 * @author Schalk W. Cronjé
 */
class GnuMakeComponentRulesSpec extends Specification {
    Project project = ProjectBuilder.builder().build()

    void setup() {
        project.apply plugin: GnuMakeComponentRules
    }

    def "Can create a component"() {
        when: "A GNU Make component is configured"
        project.with {
            model {
                components {
                    make(GnuMakeSpec) {
                        srcDir = "${buildDir}/unpacked"
                    }
                }
            }
        }

        GnuMakeSpec node = project.modelRegistry.find('components.make',GnuMakeSpec)

        then: "The default values must be defined and the non-null values must be set as per the model"
        node != null
        node?.toolName  == 'GNU Make'
        node?.srcDir == "${project.buildDir}/unpacked"
        node?.cleanTargets == ['clean']
        node?.targets == []
    }
}
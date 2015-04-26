package org.ysb33r.gradle.gnumake.internal

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification


/**
 * @author Schalk W. Cronjé
 */
class TrackerTaskSpec extends Specification {

    Project project

    void setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'org.ysb33r.gnumake'
    }

    def "Creating a GNUMakeBuild task will enable other tasks to track it"() {
        given:
        Task tracker = project.tasks.findByName('makeClean')

        expect:
        tracker != null
        tracker instanceof TrackerTask
        tracker.trackName == 'make'
        tracker.target == 'clean'

    }

    def "Executing a make clean should invoke job with the correct settings"() {
        given:
        Task tracker = project.tasks.findByName('makeClean')
        FakeExecutor exec = new FakeExecutor()
        tracker.executor = exec

        when:
        tracker.execute()

        then:
        exec.executable == project.gnumake.executable
        exec.cmdargs.contains('clean')
    }

}
package org.ysb33r.gradle.gnumake.internal

import org.gradle.api.Project
import org.gradle.process.ExecResult
import org.ysb33r.gradle.gnumake.GnuMakeBuild

/**
 * @author Schalk W. Cronjé
 */
class TaskUtils {
    static List<String> buildCmdArgs(Project project,GnuMakeBuild task,List<String> targets) {

        project.extensions.getByName('gnumake').execArgs + [
            [task.alwaysMake, '-B'],
            [task.environmentOverrides, '-e'],
            [task.ignoreErrors, '-i'],
            [task.keepGoing, '-k'],
            [task.jobs > 1, '-j', task.jobs.toString()],
            [task.makefile, '-f', "${task.makefile.toString()}"],
            [task.chDir, '-C', "${task.chDir?.absolutePath}" ],
        ].collectMany { it.head() ? it.tail() : [] } +

            (task.includeDirs.files.collectMany { ['-I', "${it.absolutePath}"] }) +
            targets +
            task.flags.collect { k, v -> "$k=$v" } +
            task.switches
    }

    static ExecResult runMake(Project project,final String exec,final List<String> cmdargs,final File wd=null) {
        project.exec {

            executable = exec

            if (wd) {
                workingDir = wd
            }

            args = cmdargs
        }

    }
}

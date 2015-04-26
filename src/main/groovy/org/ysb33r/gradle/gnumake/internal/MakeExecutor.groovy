package org.ysb33r.gradle.gnumake.internal

import groovy.transform.TupleConstructor
import org.gradle.api.Project
import org.gradle.process.ExecResult

/**
 * @author Schalk W. Cronjé
 */
@TupleConstructor
class MakeExecutor implements Executor {
    Project project

    ExecResult runMake(final String exec,final List<String> cmdargs,final File wd=null) {
        TaskUtils.runMake(project,exec,cmdargs,wd)
    }
}

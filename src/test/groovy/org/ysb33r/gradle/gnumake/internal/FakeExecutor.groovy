package org.ysb33r.gradle.gnumake.internal

import groovy.transform.CompileStatic
import org.gradle.process.ExecResult

/**
 * @author Schalk W. Cronjé
 */
@CompileStatic
class FakeExecutor implements Executor {

    String executable
    List<String> cmdargs
    File workingDir

    ExecResult runMake(String exec, List<String> cmdargs, File wd) {
        this.executable = exec
        this.cmdargs = cmdargs
        this.workingDir = wd
        return new FakeExecResult()
    }
}

package org.ysb33r.gradle.gnumake.internal

import org.gradle.api.Project
import org.gradle.process.ExecResult

/**
 * @author Schalk W. Cronjé
 */
interface Executor {
    ExecResult runMake(final String exec,final List<String> cmdargs,final File wd)
}
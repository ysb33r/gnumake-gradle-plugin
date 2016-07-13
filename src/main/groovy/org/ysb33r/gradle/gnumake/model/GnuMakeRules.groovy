package org.ysb33r.gradle.gnumake.model

import groovy.transform.CompileStatic
import org.gradle.internal.os.OperatingSystem
import org.gradle.model.Defaults
import org.gradle.model.Model
import org.gradle.model.RuleSource

/**
 * @author Schalk W. Cronjé
 */
@CompileStatic
class GnuMakeRules extends RuleSource {
    /** Create the {@code gnumake} top-level model.
     *
     * @return The model which is of type {@link GnuMakeExtension}
     */
    @Model
    GnuMakeExtension gnumake() {
        new GnuMakeExtension()
    }

    /** Override the default name for make when the operating system is
     * Solaris and FreeBSD.
     *
     * @param gnumake
     */
    @Defaults
    void executableName(GnuMakeExtension gnumake) {
        switch(OperatingSystem.current()) {
            case OperatingSystem.SOLARIS:
            case OperatingSystem.FREE_BSD:
                gnumake.executable = 'gmake'
        }
    }
}

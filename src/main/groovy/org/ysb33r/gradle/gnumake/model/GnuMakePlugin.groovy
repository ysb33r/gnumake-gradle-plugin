package org.ysb33r.gradle.gnumake.model

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.component.Component
import org.gradle.internal.os.OperatingSystem
import org.gradle.model.Defaults
import org.gradle.model.Finalize
import org.gradle.model.Model
import org.gradle.model.ModelMap
import org.gradle.model.ModelSet
import org.gradle.model.Mutate
import org.gradle.model.Path
import org.gradle.model.RuleSource
import org.gradle.platform.base.ComponentBinaries
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.TypeBuilder
import org.ysb33r.gradle.gnumake.tasks.GnuMakeBuild

/** Will create a new top-level model called {@code gnumake}.
 *
 * @since 2.0
 */
@CompileStatic
class GnuMakePlugin implements Plugin<Project> {

    void apply(Project project) {
        project.with {
            apply plugin : GnuMakeRules
        }
    }
}

package org.ysb33r.gradle.gnumake.model

import groovy.transform.CompileStatic
import org.gradle.model.Defaults
import org.gradle.model.Each
import org.gradle.model.RuleSource
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.TypeBuilder

/**
 * @since 2.0
 */
@CompileStatic
class GnuMakeComponentRules extends RuleSource {
//    @Model
//    void externalComponents(ModelSet<Component> components,@Path('gnumake') GnuMakeExtension) {
//
//    }

//    /** Sets the default target list to empty and the default clean target list to 'clean'
//     *
//     * @param spec GnuMakeSpec to configure
//     */
    @Defaults
    void defaultTargets(@Each GnuMakeSpec spec) {
        spec.targets = []
        spec.cleanTargets = ['clean']
    }
//
//
    @ComponentType
    void registerMakeComponent(TypeBuilder<GnuMakeSpec> builder ) {
        builder.defaultImplementation(GnuMakeSpec)
    }

//    @ComponentBinaries
//    void
    // @ComponentBinaries must list all of the artifacts created by the Makefile and a list of targets
    // @BinaryTasks will create a GNUMake task with appropriate targets
    //   Also add a clean Task that runs 'make clean'
}

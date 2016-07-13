package org.ysb33r.gradle.gnumake.model

import org.gradle.model.Managed
import org.gradle.model.ModelMap
import org.gradle.model.Unmanaged
import org.ysb33r.gradle.buildtool.spec.BuildToolArtifact
import org.ysb33r.gradle.buildtool.spec.GeneralBuildToolSpec

/** A specification whereby distinctive outputs can be defined
 * for a make project which can then be re-used elsewhere in Gradle projects.
 *
 * @since 2.0
 */
@Managed
abstract class GnuMakeSpec implements GeneralBuildToolSpec {

    @Override
    String getToolName() { 'GNU Make' }

//    abstract ModelMap<BuildToolArtifact> getArtifact()
//    abstract ModelMap<BuildToolArtifact> getArtifact()


}

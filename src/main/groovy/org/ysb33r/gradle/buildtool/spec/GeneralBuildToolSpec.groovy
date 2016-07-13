package org.ysb33r.gradle.buildtool.spec

import groovy.transform.CompileStatic
import org.gradle.model.Managed
import org.gradle.platform.base.ComponentSpec
import org.gradle.platform.base.VariantComponentSpec

/** Base interface for a component that can be built from an external
 * build tool.
 *
*/
//@Managed
@CompileStatic
interface GeneralBuildToolSpec extends ComponentSpec, VariantComponentSpec {

    String getSrcDir()
    void setSrcDir(String path)

    List<String> getTargets()
    void setTargets(List<String> t)

    List<String> getCleanTargets()
    void setCleanTargets(List<String> t)

    String getToolName()

}
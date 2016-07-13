package org.ysb33r.gradle.buildtool.toolchain

import org.gradle.platform.base.ToolChain

/**  Models any external tool chains that can be used to create an artifact that can be reused
 * by another Gradle task or project.
 */
interface BuildToolChain extends ToolChain {

    String getExecutable()
    void setExecutable(String exe)
}
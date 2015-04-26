//
// ============================================================================
// (C) Copyright Schalk W. Cronje 2013-2015
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and limitations under the License.
//
// ============================================================================
//



package org.ysb33r.gradle.gnumake

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.process.ExecResult
import org.gradle.util.CollectionUtils
import org.ysb33r.gradle.gnumake.internal.Executor
import org.ysb33r.gradle.gnumake.internal.InputOutputMonitor
import org.ysb33r.gradle.gnumake.internal.MakeExecutor
import org.ysb33r.gradle.gnumake.internal.Rules

/**
 * A wrapper task for calling GNU Make. This is useful for migrating legacy builds
 * or when complex build need to construct components that use GNU Make as a
 * build tool.
 *
 * @author Schalk W. Cronjé
 */
@CompileStatic
class GnuMakeBuild extends DefaultTask {

    GnuMakeBuild() {
        Rules.addRule(project,name)
    }

    /** Indicates whether default flags should be inherited from the {@code gnumake} extension.
     * Default is {@code true}
     * @since 1.0
     */
    @Input
    boolean defaultFlags = true

    /** Force targets to be rebuilt, even if they are already up to date
     * This is equivalent of passing -B to make
     */
    @Input
    @Optional
    boolean alwaysMake = false

    /** Tell make that varibales from the environment takes precedence over variables defined
     * inside makefile.
     * This is equivalent of passing -e to make
     */
    @Input
    @Optional
    boolean environmentOverrides = false

    /** Tell make to ignore all errors in commands executed to remake files.
     * This is equivalant to passing -i to make
     */
    @Input
    @Optional
    boolean ignoreErrors = false

    /** Set make concurrency level.
     * This is equivalent of passing -j to make.
     */
    @Input
    @Optional
    Integer jobs = 1

    /** Tell make to carry on as far as possible after errors.
     * This is equivalant to passing -k to make
     */
    @Input
    @Optional
    boolean keepGoing = false


    /** List of targets to execute
     *
     */
    @Input
    @Optional
    List<String> getTargets() {
        CollectionUtils.stringize(this.targets)
    }

    /** Resets targets and assigns a new set.
     *
     * @param targets_ List of targets to use
     */
    void setTargets(Object... targets_) {
        this.targets.clear()
        this.targets.addAll(targets_ as List)
    }

    /** Appends to current set of targets.
     *
     * @param targets_ List of targets to use
     * @since 1.0
     */
    void targets(Object... targets_) {
        this.targets.addAll(targets_ as List)
    }

    /** Returns the set of flags that were supplied. Unless {@code defaultFlags} is {@code false}
     * the flags from {@code gnumake.defaultFlags} will be included
     *
     * @return A sets of flags that needs to be passed to make
     */
    @Input
    @Optional
    @CompileDynamic
    Map getFlags() {
        if( defaultFlags && project.extensions.findByName('gnumake') ) {
            project.gnumake.defaultFlags + this.flags
        } else {
            this.flags
        }
    }

    /** Resets the existing flags to the supplied set of flags/variables to pass to make.
     * @code
     * setFlags DESTDIR : '/path/to/somewhere', BUILD_NUMBER : 12345
     * @endcode
     */
    void setFlags(Map a) {
        this.flags.clear()
        this.flags += a
    }

    /** Appends flags/variables to pass to make.
     * @code
     * flags DESTDIR : '/path/to/somewhere', BUILD_NUMBER : 12345
     * @endcode
     * @since 1.0
     */
    void flags(Map a) {
        this.flags+= a
    }

    /** Arbitrary GNU Make command-line switches to pass. This allows flexibility to
     * pass anything above and beyond basic functionality already supported in this
     * task class. Supplied switches are passed as the last items on the command-line to
     * make and as such can override any previous setting set up through properties
     * on the GnuMake class.
     */
    @Input
    @Optional
    List<String> getSwitches() {
        CollectionUtils.stringize(this.switches)
    }

    /** Resets the list of arbitrary GNU Make command-line switches and replace them with
     * the supplied list.
     *
     * @param sw New list of switches to use.
     */
    void setSwitches(Object... sw) {
        this.switches.clear()
        this.switches.addAll(sw as List)
    }

    /** Appends to the list of arbitrary GNU Make command-line switches.
     *
     * @param sw List of switches to append.
     */
    void switches(Object... sw) {
        this.switches.addAll(sw as List)
    }

    /** The make executable. If not set, will default to an OS-specific name
     * without any prefix, therefore relying on the system path to find.
     */
    @Input
    @CompileDynamic
    String getExecutable() {
        this.executable ?: project.extensions.getByName('gnumake').executable
    }

    /** The make executable.
     */
    String setExecutable(final Object exe) {
        this.executable = exe.toString()
    }

    /** The make executable.
     */
    String executable(final Object exe) {
        setExecutable(exe)
    }

    /** Directory where to start make from. This is @b NOT the same as passing
     * -C. (See chdir for that).
     */
    @Input
    @Optional
    @CompileDynamic
    File getWorkingDir() {
        this.workingDir ? project.file(this.workingDir) : null
    }

    /** Sets the working directory where to start make from. This is @b NOT the same as passing
     * -C. (See chdir for that).
     * @param dir Working directory. This will be evaluated use {@code project.file}/
     */
    void setWorkingDir(Object dir) {
        this.workingDir = dir
    }

    /** Sets the working directory where to start make from. This is @b NOT the same as passing
     * -C. (See chdir for that).
     * @param dir Working directory. This will be evaluated use {@code project.file}/
     */
    void workingDir(Object dir) {
       setWorkingDir(dir)
    }

    /** After starting make, change to this directory before reading the Makefile.
     * This is the equivalent of passing -C to make
     */
    @Input
    @Optional
    @CompileDynamic
    File getChDir() {
        this.chDir ? project.file(this.chDir) : null
    }

    /** After starting make, change to this directory before reading the Makefile.
     * This is the equivalent of passing -C to make
     * @param dir Change-to directory. This will be evaluated use {@code project.file}/
     */
    void setChDir(Object dir) {
        this.chDir = dir
    }

    /** After starting make, change to this directory before reading the Makefile.
     * This is the equivalent of passing -C to make
     * @param dir Change-to directory. This will be evaluated use {@code project.file}/
     */
    void chDir(Object dir) {
        setChDir(dir)
    }

    /** Makefile to use. If not supplied will resort to the default behaviour of make,
     * which is usually to look for a file called Makefile or GNUMakefile.
     * This is the equivalent of passing -f to make.
     */
    @Input
    @CompileDynamic
    String getMakefile() {
        this.makefile ?: project.extensions.getByName('gnumake').makefile
    }

    /** Makefile to use. If not supplied will resort to the default behaviour of make,
     * which is usually to look for a file called Makefile or GNUMakefile.
     * This is the equivalent of passing -f to make.
     */
    void setMakefile(final String name) {
        this.makefile = name
    }

    /** Makefile to use. If not supplied will resort to the default behaviour of make,
     * which is usually to look for a file called Makefile or GNUMakefile.
     * This is the equivalent of passing -f to make.
     */
    void makefile(final String name) {
        setMakefile(name)
    }

    /** List of include directories to search for included makefiles.
     * This is equivalent to passing -I to make
     *
     * @code
     * includeDirs '/path/to/place1', new File ('/path/to/place2' )
     * @endcode
     */
    @Input
    @Optional
    @CompileDynamic
    FileCollection getIncludeDirs() {
        project.files(this.includeDirs)
    }

    /** Resets the list of directories to search for included makefiles and
     * replaces with the supplied list
     *
     * @param dirs List of directories. Will be evaluated using {@pcode roject.files}
     */
    void setIncludeDirs(Object... dirs) {
        includeDirs.clear()
        includeDirs.addAll(dirs as List)
    }

    /** Appends to the list of directories to search for included makefiles.
     *
     * @param dirs List of directories. Will be evaluated using {@pcode roject.files}
     */
    void includeDirs(Object... dirs) {
        includeDirs.addAll(dirs as List)
    }

    /** Adds sources that need to be monitored as part of the decision to determine up to date status.
     *
     * @param cfg Configurating closure. Takes {@code dir}, {@code file} and {@code files} as per {@code TaskInputs}
     */
    @CompileDynamic
    void makeInputs(Closure cfg) {
        def cfg2 = cfg.clone()
        cfg2.delegate = inMonitor
        cfg2.resolveStrategy = Closure.DELEGATE_FIRST
        cfg2()
    }

    /** Adds outputs that need to be monitored as part of the decision to determine up to date status.
     *
     * @param cfg Configurating closure. Takes {@code dir}, {@code file} and {@code files} as per {@code TaskOutputs}
     */
    @CompileDynamic
    void makeOutputs(Closure cfg) {
        def cfg2 = cfg.clone()
        cfg2.delegate = outMonitor
        cfg2.resolveStrategy = Closure.DELEGATE_FIRST
        cfg2()
    }

    /** Stores the result of running a make command */
    ExecResult execResult

    /** Returns the command line arguments passed to make
     * on last run.
     */
    List<String> getCmdArgs() {
        cmdargs
    }

    @TaskAction
    @CompileDynamic
    void exec() {

        if(!executor) {
            executor = new MakeExecutor(project)
        }
        buildCmdArgs()
        execResult = executor.runMake(executable,cmdArgs,getWorkingDir())
    }

    @Deprecated
    void setTasks(Object... targets_) {
        logger.warn "'tasks/setTasks' is deprecated. Please use 'targets/setTargets' instead."
        setTargets targets_
    }

    @Deprecated
    List<String> getTasks() {
        logger.warn "'getTasks' is deprecated. Please use 'getTargets' instead."

        getTargets()
    }

    @Deprecated
    void setDir(def chDir_) {
        logger.warn "'dir/setDir' is deprecated. Please use 'chdir/setChDir' instead."
        chDir = chDir_
    }

    @Deprecated
    File getDir() {
        logger.warn "'getDir' is deprecated. Please use 'getChDir' instead."
        getChDir()
    }

    @Deprecated
    void setBuildFile(final String makefile_) {
        logger.warn "'setBuildFile' is deprecated. Please use 'setMakefile' instead."
        setMakefile(makefile_)
    }

    @Deprecated
    String getBuildFile() {
        logger.warn "'getBuildFile' is deprecated. Please use 'getMakefile' instead."
        getMakefile()
    }

    /** Builds the command line and updates cmdargs.
     * This is not usually called directly by an external process,
     * but it is useful to call in order to determine what the effect
     * of the current properties will be on the command-line.
     */
    @CompileDynamic
    @PackageScope
    List<String> buildCmdArgs() {

        cmdargs = project.extensions.getByName('gnumake').execArgs + [
                [alwaysMake, '-B'],
                [environmentOverrides, '-e'],
                [ignoreErrors, '-i'],
                [keepGoing, '-k'],
                [jobs > 1, '-j', jobs as String],
                [makefile, '-f', "${makefile.toString()}"],
                [chDir, '-C', "${chDir.toString()}"],

        ].collectMany { it.head() ? it.tail() : [] } +

                (getIncludeDirs().files.collectMany { ['-I', "${it.toString()}"] }) +
                targets +
                flags.collect { k, v -> "$k=$v" } +
                switches
    }



    private String executable
    private String makefile

    private List<Object> switches = []
    private List<Object> targets = []
    private List<Object> includeDirs = []
    private Map flags = [:]
    private Object workingDir
    private Object chDir
    private List<String> cmdargs = []
    private InputOutputMonitor inMonitor = new InputOutputMonitor(this,'inputs')
    private InputOutputMonitor outMonitor = new InputOutputMonitor(this,'outputs')

    Executor executor
}

package org.ysb33r.gradle.gnumake.toolchain

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.gradle.api.file.FileCollection
import org.gradle.util.CollectionUtils
import org.ysb33r.gradle.buildtool.toolchain.BuildToolChain
import org.ysb33r.gradle.gnumake.model.GnuMakeExtension

/**
 * @author Schalk W. Cronjé
 */
@CompileStatic
class GnuMake implements BuildToolChain {

    @Override
    String getExecutable() {
        this.exe
    }

    @Override
    void setExecutable(String exe) {
        this.exe = exe.toString()
    }

    /**
     * Returns a human consumable name for this tool chain.
     *
     * @since 1.11
     */
    @Override
    String getDisplayName() {
        "GNU Make"
    }

    /**
     * The object's name.
     * <p>
     * Must be constant for the life of the object.
     *
     * @return The name. Never null.
     */
    @Override
    String getName() {
        'gnumake'
    }

    /** Force targets to be rebuilt, even if they are already up to date
     * This is equivalent of passing -B to make
     */
    boolean alwaysMake = false

    /** Tell make that variables from the environment takes precedence over variables defined
     * inside makefile.
     * This is equivalent of passing -e to make
     */
    boolean environmentOverrides = false

    /** Tell make to ignore all errors in commands executed to remake files.
     * This is equivalent to passing -i to make
     */
    boolean ignoreErrors = false

    /** Set make concurrency level.
     * This is equivalent of passing -j to make.
     */
    Integer jobs = 1

    /** Tell make to carry on as far as possible after errors.
     * This is equivalant to passing -k to make
     */
    boolean keepGoing = false

    /** Indicates whether default flags should not be inherited from the {@code gnumake} extension.
     * Default is {@code false}
     * @since 1.0
     */
    boolean noDefaultFlags = false

    /** If set to {@code true} then {@code gnumake.execArgs} are not inherited.
     * Default is to inherit.
     * @since 1.0
     */
    boolean noExecArgs = false


//    /** List of targets to execute
//     *
//     */
//    @CompileDynamic
//    List<String> getTargets() {
//        CollectionUtils.stringize(this.targets)
//    }
//
//    /** Resets targets and assigns a new set.
//     *
//     * @param targets_ List of targets to use
//     */
//    void setTargets(Object... targets_) {
//        this.targets.clear()
//        this.targets.addAll(targets_ as List)
//    }
//
//    /** Appends to current set of targets.
//     *
//     * @param targets_ List of targets to use
//     * @since 1.0
//     */
//    void targets(Object... targets_) {
//        this.targets.addAll(targets_ as List)
//    }

    /** Returns the set of flags that were supplied. Unless {@code defaultFlags} is {@code false}
     * the flags from {@code gnumake.defaultFlags} will be included
     *
     * @return A sets of flags that needs to be passed to make
     */
    @CompileDynamic
    Map getFlags() {
        if( !noDefaultFlags && project.extensions.findByName(GnuMakeExtension.EXTENSION_NAME) ) {
            project.extensions.findByName(GnuMakeExtension.EXTENSION_NAME).defaultFlags + this.flags
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

    /** Returns the map of environment variables that will be added to the environment that make be executed within.
     *
     * @return A map of environment variables or null
     * @since 1.0.2
     */
    Map getEnvironment() {
        this.environment
    }

    /** Resets the existing environment variables that is to be added to make environment.
     * @code
     * setEnvironment INCPATH : '/foo/bar'
     * @endcode
     * @since 1.0.2
     */
    void setEnvironment(Map a) {
        if(this.environment != null) {
            this.environment.clear()
        } else {
            this.environment = [:]
        }
    }

    /** Appends variables to the make environment.
     * @code
     * environment INCPATH : '/foo/bar', LIBPATH : '/lib/path'
     * @endcode
     * @since 1.0.2
     */
    void environment(Map a) {
        if(this.environment != null) {
            this.environment+= a
        } else {
            this.environment= a
        }
    }

    /** Arbitrary GNU Make command-line switches to pass. This allows flexibility to
     * pass anything above and beyond basic functionality already supported in this
     * task class. Supplied switches are passed as the last items on the command-line to
     * make and as such can override any previous setting set up through properties
     * on the GnuMake class.
     */
    List<String> getSwitches() {
        CollectionUtils.stringize(this.switches)
    }

    /** Resets the list of arbitrary GNU Make command-line switches and replace them with
     * the supplied list.
     *
     * @param sw New list of switches to use.
     */
    void setSwitches(String... sw) {
        this.switches.clear()
        this.switches.addAll(sw as List)
    }

    /** Appends to the list of arbitrary GNU Make command-line switches.
     *
     * @param sw List of switches to append.
     */
    void switches(String... sw) {
        this.switches.addAll(sw as List)
    }

    /** The make executable. If not set, will default to an OS-specific name
     * without any prefix, therefore relying on the system path to find.
     */
//    String getExecutable() {
//        this.executable ?: project.extensions.getByName(GnuMakeExtension.EXTENSION_NAME)?.executable
//    }

    /** The make executable.
     */
//    String setExecutable(final Object exe) {
//        this.executable = exe.toString()
//    }

    /** The make executable.
     */
    String executable(final String exe) {
        setExecutable(exe)
    }

    /** Directory where to start make from. This is @b NOT the same as passing
     * -C. (See chdir for that).
     */
//    File getWorkingDir() {
//        this.workingDir ? project.file(this.workingDir) : null
//    }

    /** Sets the working directory where to start make from. This is @b NOT the same as passing
     * -C. (See chdir for that).
     * @param dir Working directory. This will be evaluated use {@code project.file}/
     */
//    void setWorkingDir(Object dir) {
//        this.workingDir = dir
//    }

    /** Sets the working directory where to start make from. This is @b NOT the same as passing
     * -C. (See chdir for that).
     * @param dir Working directory. This will be evaluated use {@code project.file}/
     */
//    void workingDir(Object dir) {
//        setWorkingDir(dir)
//    }

    /** After starting make, change to this directory before reading the Makefile.
     * This is the equivalent of passing -C to make
     */
    File getChDir() {
        null //this.chDir ? project.file(this.chDir) : null
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
     *
     * @return Returns the makefile or null
     */
    String getMakefile() {
       null // this.makefile ?: project.extensions.findByName(GnuMakeExtension.EXTENSION_NAME)?.makefile
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
    FileCollection getIncludeDirs() {
        null //project.files(this.includeDirs)
    }

    /** Resets the list of directories to search for included makefiles and
     * replaces with the supplied list
     *
     * @param dirs List of directories. Will be evaluated using {@pcode roject.files}
     */
    void setIncludeDirs(Object... dirs) {
        this.includeDirs.clear()
        this.includeDirs.addAll(dirs as List)
    }

    /** Appends to the list of directories to search for included makefiles.
     *
     * @param dirs List of directories. Will be evaluated using {@pcode roject.files}
     */
    void includeDirs(Object... dirs) {
        includeDirs.addAll(dirs as List)
    }

    private String exe = 'make'
    private List<Object> includeDirs = []
}
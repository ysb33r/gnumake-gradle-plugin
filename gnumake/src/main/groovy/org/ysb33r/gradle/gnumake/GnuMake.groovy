// ============================================================================
// (C) Copyright Schalk W. Cronje 2013
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
// ============================================================================
package org.ysb33r.gradle.gnumake


import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.process.ExecResult

/**
 * A wrapper task for calling GNU Make. This is useful for migrating legacy builds
 * or when complex build need to construct components that use GNU Make as a
 * build tool.
 *
 * @author Schalk W. Cronjé
 */
class GnuMake extends DefaultTask {

  /** List of targets to execute
   *
   */
  @Input
  @Optional
  List<String> targets = []
  
  /** Flags/variables to pass to make. 
   * @code
   * flags = [ DESTDIR : '/path/to/somewhere', BUILD_NUMBER : 12345 ]
   * @endcode
   */
  @Input
  @Optional
  Map<String,Object> flags = [:]
  
  /** Arbitrary GNU Make command-line switches to pass. This allows flexibility to 
   * pass anything above and beyond basic functionality already supported in this
   * task class. Supplied are passed as the last items on the command-line to
   * make and as such can override any previous setting set up through properties
   * on the GnuMake class. 
   * 
   */
  @Input
  @Optional
  def switches = []

  /** The make executable. If not set, will default to an OS-specific name
   * without any prefix, therefore relying on the system path to find.
   */
  @Input
  @Optional
  String executable
  
  /** Directory where to start make from. This is @b NOT the same as passing
   * -C. (See chdir for that).
   */
  @Input
  @Optional
  String workingDir
  
  /** After starting make, change to this directory before reading the Makefile.
   * This is the equivalent of passing -C to make
   */
  @Input
  @Optional
  String chDir
  
  /** Makefile to use. If not supplied will resort to the defaut behaviour of make, 
   * which is usually to look for a file called Makefile or GNUMakefile.
   * This is the equivalent of passing -f to make.
   */
  @Input
  @Optional
  String makefile
  
  /** Force targets to be rebuilt, even if they are already up to date
   * This is equivalent of passing -B to make
   */
  @Input
  @Optional
  boolean alwaysMake = false

  /** Tell make that varibales from the environment takes precedence over varibales defined 
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

  /** List of include directories to search for included makefiles.
   * This is equivalent to passing -I to make
   * 
   * @code
   * includeDirs = [ '/path/to/place1', new File ('/path/to/place2' ) ]
   * @endcode
   */
  @Input
  @Optional
  def includeDirs = []
  
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
  
  /** Stores the result of running an make command */
  ExecResult execResult
  
  /** Stores the command line arguments passed to make 
   * on last run. Do not attempt to update this as it will
   * automatically be overwritten by an invovation of the
   * task action.
   */
  def cmdargs = []
  
  
  /** Constructs the basic task class and sets executble path
   * if not already set.
   */
  GnuMake() {
    // Can make this assume certain defaults based upon the OS
    executable = executable ?: 'make'
  }
  
  /** Builds the command line and updates cmdargs.
   * This is not usually called directly by an external process,
   * but it is useful to call in order to determine what the effect
   * of the current properties will be on the command-line.
   */
  void buildCmdArgs() {
      cmdargs = [
          
          [ alwaysMake,           '-B' ],
          [ environmentOverrides, '-e' ],
          [ ignoreErrors,         '-i' ],
          [ keepGoing,            '-k' ],
          [ jobs > 1,             '-j', jobs as String ],
          [ makefile,             '-f',"${makefile.toString()}" ],
          [ chDir,                '-C',"${chDir.toString()}" ],
          
      ].collectMany { it.head() ? it.tail() : [] } + 
      
          ( includeDirs.collectMany { ['-I',"${it.toString()}"] } ) +     
          targets +
          flags.collect { k,v -> "$k=$v" } +
          switches
  }
    
  @TaskAction
  void exec() {

      buildCmdArgs()
              
      execResult = project.exec {

          executable = this.executable
          
          if (this.workingDir) {
              workingDir= this.workingDir
          }
          
          args = cmdargs
    }
  }
}

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
  
  @Input
  @Optional
  Map<String,Object> flags = [:]
  
  @Input
  @Optional
  def switches = []

  @Input
  @Optional
  String executable
  
  @Input
  @Optional
  String workingDir
  
  @Input
  @Optional
  String makefile
  
  @Input
  @Optional
  boolean force = false

  @Input
  @Optional
  boolean alwaysMake = false

  @Input
  @Optional
  boolean environmentOverrides = false

  @Input
  @Optional
  boolean ignoreErrors = false

  @Input
  @Optional
  def includeDirs = []
  
  @Input
  @Optional
  Integer jobs = 1
  
  @Input
  @Optional
  boolean keepGoing = false
  
  ExecResult execResult
  def cmdargs = []
  
  
  GnuMake() {
    // Can make this assume certain defaults based upon the OS
    executable = executable ?: 'make'
  }
  
  void buildCmdArgs() {
      cmdargs = [
          
          [ alwaysMake,           '-B' ],
          [ environmentOverrides, '-e' ],
          [ ignoreErrors,         '-i' ],
          [ keepGoing,            '-k' ],
          [ jobs > 1,             '-j', jobs as String ],
          [ makefile,             '-f',"${makefile.toString()}" ],
          
      ].collectMany { it.head() ? it.tail() : [] } + 
      
          (includeDirs.size() ? includeDirs.collectMany { ['-I',"${it.toString()}"] } : []) +     
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

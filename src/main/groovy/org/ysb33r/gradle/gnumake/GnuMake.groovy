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
  List<String> targets
  
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
  List<String> includeDirs
  
  @Input
  @Optional
  Integer jobs = 1
  
  @Input
  @Optional
  boolean keepGoing = false
  
  ExecResult execResult
  
  GnuMake() {
    // Can make this assume certain defaults based upon the OS
    executable = executable ?: 'make'
  }

  @TaskAction
  void exec() {
      
      List<String> cmdline
      
      if (makefile) {
          cmdline+= ['-f',"'${makefile}'" ]
      }
      
      if (alwaysMake) {
          cmdline+= ['-B']
      }
      
      if (environmentOverrides) {
          cmdline+= ['-e']
      }
      
      if (ignoreErrors) {
          cmdline+= ['-i']
      }
       
      includeDirs.each {
          cmdline+= ['-I',"'${it}'"]
      }
  
      if(jobs > 1) {
        cmdline+= ['-j',"${jobs}"]
      }

      if (keepGoing) {
          cmdline+= ['-k']
      }

      cmdline+= targets
          
      if(this.flags.size) {
          cmdline+= flags.collect { k,v -> "'$k=$v'" }
      }
      
      cmdline+= switches
  
      execResult = project.exec {

          executable = this.executable
          
          if (this.workingDir) {
              workingDir= this.workingDir
          }
          
          args = cmdline
    }
  }
  
}

// ============================================================================
// (C) Copyright Schalk W. Cronje 2013
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
// ============================================================================
import spock.lang.*
import org.ysb33r.gradle.gnumake.GnuMake
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class GnuMakeSpec extends spock.lang.Specification {
    def Project project = ProjectBuilder.builder().build()
    def gnumake = project.task('gnumake', type: GnuMake )
    
    def "Newly created Task will set executable to OS-specific value"() {
        expect:
        gnumake.executable == 'make'     
    }
    
    def "Newly created Task will have empty command line"() {
        expect:
        gnumake.cmdargs.size() == 0
    }
    
    def "alwaysMake adds -B" () {
        
        given:
        gnumake.alwaysMake = true
        gnumake.buildCmdArgs()
            
        expect:
        gnumake.cmdargs.size() == 1
        gnumake.cmdargs.contains('-B')
    }

    def "keepGoing adds -k" () {
        
        given:
        gnumake.keepGoing = true
        gnumake.buildCmdArgs()
            
        expect:
        gnumake.cmdargs.size() == 1
        gnumake.cmdargs.contains('-k')
    }

    def "environmentOverrides adds -e" () {
        
        given:
        gnumake.environmentOverrides = true
        gnumake.buildCmdArgs()
            
        expect:
        gnumake.cmdargs.size() == 1
        gnumake.cmdargs.contains('-e')
    }

    def "ignoreErrors adds -i" () {
        
        given:
        gnumake.ignoreErrors = true
        gnumake.buildCmdArgs()
            
        expect:
        gnumake.cmdargs.size() == 1
        gnumake.cmdargs.contains('-i')
    }

    def "jobs affect -j" () {
                
        when: "jobs == 1"
        gnumake.jobs = 1
        gnumake.buildCmdArgs()
            
        then: "-j will not be added"
        gnumake.cmdargs.size() == 0

        when: "jobs < 1"
        gnumake.jobs = -3
        gnumake.buildCmdArgs()
        
        then: "-j will not be added"
        gnumake.cmdargs.size() == 0

        when: "jobs > 1"
        gnumake.jobs = 4
        gnumake.buildCmdArgs()

        then: "-j will be added"
        gnumake.cmdargs.size() == 2
        gnumake.cmdargs[0] == '-j'
        gnumake.cmdargs[1] == '4'
    }

    def "if makefile is empty -f is not added"() {
        given:
        gnumake.makefile = ''   
        gnumake.buildCmdArgs()
        
        expect:
        gnumake.cmdargs.size() == 0
    }
    
    def "if makefile is String add asis"() {
        given:
        gnumake.makefile = 'Makefile'
        gnumake.buildCmdArgs()
        
        expect:
        gnumake.cmdargs.size() == 2
        gnumake.cmdargs[0] == '-f'
        gnumake.cmdargs[1] == "Makefile"

    }
    
    def "if makefile is File then abolute path should not be added"() {
        given:
        gnumake.makefile = new File ('../../Makefile')
        gnumake.buildCmdArgs()
        
        expect:
        gnumake.cmdargs.size() == 2
        gnumake.cmdargs[0] == '-f'
        gnumake.cmdargs[1] == "../../Makefile"
    }
 
    def "chDir affects -C"() {
        given:
        gnumake.chDir = './change/here'
        gnumake.buildCmdArgs()
        
        expect:
        gnumake.cmdargs.size() == 2
        gnumake.cmdargs[0] == '-C'
        gnumake.cmdargs[1] == "./change/here"

    }
  
    def "includeDirs adds -I plus path"() {
        given:
        gnumake.includeDirs = [ 'localDir', new File ('../FileObjectDir'), '/absolutePath' ]
        gnumake.buildCmdArgs()
        
        expect:
        gnumake.cmdargs.size() == 6
        gnumake.cmdargs[0] == '-I'
        gnumake.cmdargs[1] == "localDir"
        gnumake.cmdargs[2] == '-I'
        gnumake.cmdargs[3] == "../FileObjectDir"
        gnumake.cmdargs[4] == '-I'
        gnumake.cmdargs[5] == "/absolutePath"

    }
    
    def "targets are added in order specified"() {
        given:
        gnumake.targets = ['clean','install']
        gnumake.buildCmdArgs()
        
        expect:
        gnumake.cmdargs.size() == 2
        gnumake.cmdargs[0] == 'clean'
        gnumake.cmdargs[1] == 'install'
    }
    
    def "flags are added in the format X=Y"() {
        given:
        gnumake.flags = [ DESTDIR : '/path/somewhere', BUILD_NUMBER : 12345 ]          
        gnumake.buildCmdArgs()
            
        expect:
        gnumake.cmdargs.size() == 2
        gnumake.cmdargs.contains( "BUILD_NUMBER=12345${''}" )
        gnumake.cmdargs.contains( "DESTDIR=/path/somewhere${''}" )
    }
    
    def "arbitrary switches are added at the end"() {
        given:
        gnumake.ignoreErrors = true
        gnumake.targets = ['clean']
        gnumake.switches = ['-X','-Y','-Z']
        gnumake.buildCmdArgs()
        
        expect:
        gnumake.cmdargs.size() == 5
        gnumake.cmdargs[2] == '-X'
        gnumake.cmdargs[3] == '-Y'
        gnumake.cmdargs[4] == '-Z'   
    }
    
    def "Ordering is InternalSwitches + targets + flags + extraSwitches"() {
        given:
        gnumake.ignoreErrors = true
        gnumake.targets = ['clean']
        gnumake.flags = [DESTDIR:'/path/somewhere']
        gnumake.switches = [ '-q', '-n']
        gnumake.buildCmdArgs()
        
        expect:
        gnumake.cmdargs.join(' ') == "-i clean DESTDIR=/path/somewhere -q -n"
    }
}


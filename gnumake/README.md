

GNU Make Gradle Plugin
======================

Provides a wrapper for calling GNU Make from Gradle. This is especially useful
for projects migrating to Gradle or when constructing complex build systems of which
some components utilise GNU make as it build tool. Most common command-line switches
are supported, but for full flexibiliy it is possible to add any additional switch via 
the *switches* property.

Previous versions of this document
----------------------------------

0.0.9 - https://github.com/ysb33r/Gradle/blob/0.0.9_RELEASE/README.md
0.0.2 - https://github.com/ysb33r/Gradle/blob/0.0.2_RELEASE/README.md

Known compatibility
-------------------

+ 0.2 - Gradle 2.0
+ 0.1 - Gradle 1.12
+ 0.0.9 - Gradle 1.9
+ 0.0.3 - Gradle 1.6
+ 0.0.2 - Gradle 1.6


Supported Properties
--------------------
The following properties are supported:

   + *alwaysMake* boolean, same as -B.
   + *environmentOverrides* boolean, same as -e
   + *ignoreErrors* boolean, same as -i
   + *keepGoing* boolean, same as -k
   + *jobs*, integer, same as -j
   + *makefile* same as -f
   + *chDir* same as -C
   + *includeDirs* list, same as -I
   + *targets* list of targets to execute 
   + *flags* map of variables to pass to make
   + *switches* list, arbitrary list of switches to pass to make. 
   + *executable* location of make executable, defaults to 'make'       
   + *workingDir* location where to start make from (not the same as *chDir*)
   + *tasks* alias for targets.
   
Synopsis
--------
```groovy

import org.ysb33r.gradle.gnumake.GnuMakeBuild

buildscript { 
  repositories {
	jcenter()
  }  
  dependencies {
    classpath 'org.ysb33r.gradle:gnumake:0.2'
  }
}

task runMake (type:GnuMakeBuild) {
  targets = ['build','install']
  flags = [ DESTDIR : '/copy/files/here', BUILD_NUMBER : 12345 ]
}
```

It is also possible to set the executable and makefile name globally for all tasks

```groovy
gnumake {
  executable = 'gmake'
  makefile = 'OurOwnMakefileConvention'
}
```

If the above two are not set in a task it will take the value from the global configuration.
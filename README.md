

GNU Make Gradle Plugin
======================

Provides a wrapper for calling GNU Make from Gradle. This is especially useful
for projects migrating to Gradle or when constructing complex build systems of which
some components utilise GNU make as it build tool. Most common command-line switches
are supported, but for fullflexibiliy it is possible to add any additionak switch via 
the *switches* property.

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
   
```groovy
buildscript { 
  repositories {
    ivy {
      url 'http://dl.bintray.com/ysb33r/grysb33r'
    }
  }
  dependencies {
    classpath 'org.ysb33r.gradle:gradle-plugins:0.0.2'
  }
}

task runMake (type:org.ysb33r.gradle.gnumake.GnuMake) {
  targets = ['build','install']
  flags = [ DESTDIR : '/copy/files/here', BUILD_NUMBER : 12345 ]
}
```

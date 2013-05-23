

GNU Make Gradle Plugin
======================

Provides a wrapper for calling GNU Make from Gradle. This is especially useful
for projects migrating to Gradle or when constructing complex build systems of which
some components utilise GNU make as it build tool. Most common command-line switches
are supported, but for full flexibiliy it is possible to add any additional switch via 
the *switches* property.

Previous versions of this document
----------------------------------

0.0.2 - https://github.com/ysb33r/Gradle/blob/0.0.2_RELEASE/README.md

Known compatibility
-------------------

+ 0.0.6 - Gradle 1.6

Synopsis
--------
```groovy


buildscript {
    repositories {
        mavenCentral()
        mavenRepo(url: 'http://repository.codehaus.org')
        ivy {
          url 'http://dl.bintray.com/ysb33r/grysb33r'
        }
      }
      dependencies {
        classpath 'org.ysb33r.gradle:bintray:0.0.6'
      }
}

import org.ysb33r.gradle.gnumake.GnuMakeBuild

createBintrayPackage  {
    username    ( project.properties.bintrayUserName )
    apiKey      ( project.properties.bintrayApiKey )
    repoOwner   ( 'ysb33r' )
    repoName    ( 'grysb33r' )
    packageName ( 'gradle-bintray-plugin' )
    description ( 'A plugin to help with publishing to bintray' )
    descUrl     ( '' )
    tags        ( ['gradle','bintray'] )
}

uploadArchives  {
    repositories {
                   
        ivy {
            
            url createBintrayPackage.apiBaseUrl + '/content/' + project.properties.bintrayUserName + '/' + bintrayRepo + '/' + project.moduleName + '/' + version
            
            credentials {
                username project.properties.bintrayUserName
                password project.properties.bintrayApiKey
            }
        }

    }
}

uploadArchives.dependsOn createBintrayPackage
```

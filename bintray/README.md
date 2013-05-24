

Bintray Gradle Plugin
======================

A plugin to assist with working with Bintray. On the publishing side side it 
currently provides a task for creating the metadata required.

Previous versions of this document
----------------------------------

0.0.2 - https://github.com/ysb33r/Gradle/blob/0.0.2_RELEASE/README.md

Known compatibility
-------------------

+ 0.0.6 - Gradle 1.6

Synopsis
========

Adding the plugin
-----------------

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
```

Adding Bintray repositories
---------------------------
```groovy

apply plugin: 'bintray'

repositories {

	// Adding JCenter
	jCenter()
	
	// Adding arbitrary Bintray Ivy-style repository
	ivyBintray( 'repoOwner', 'repoName' )
	ivyBintray ('ysb33r','grysb33r')
}
```

Publising to Bintray
--------------------

```groovy
import org.ysb33r.gradle.bintray.BintrayPackage

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
 
        // Publishing as ivy              
        ivy {
            
            url createBintrayPackage.ivyUrl (project.moduleName,version)
            
            credentials {
                username createBintrayPackage.username
                password createBintrayPackage.apiKey
            }
        }

		// Publishing as maven
		mavenDeployer {
		
            repository ( url: createBintrayPackage.mavenUrl(project.moduleName) ) {
              authentication(userName: createBintrayPackage.username, password: createBintrayPackage.apiKey)
            }
            
        }
    }
}

uploadArchives.dependsOn createBintrayPackage
```

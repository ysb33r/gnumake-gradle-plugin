

The Unofficial Bintray Gradle Plugin
=====================================

A plugin to assist with publishing to. It was originally created before
Bintray published their own plugin. At the time, it was thought that this
plugin might become deprecated once Bintray launched theirs, but the
approach of this plugin is slightly different, therefore it remains active  

It will publish to Bintray as either a Ivy or a Maven style repository. A separate
task is also available for purely creating package metadata on Bintray.

Previous versions of this document
----------------------------------

This is version 1.0 of the document.

+ 0.0.9 - https://github.com/ysb33r/Gradle/blob/RELEASE_0_0_9/bintray/README.md
+ 0.0.7 - https://github.com/ysb33r/Gradle/blob/RELEASE_0_0_7/bintray/README.md
+ 0.0.6 - https://github.com/ysb33r/Gradle/blob/RELEASE_0_0_6/bintray/README.md


Known compatibility
-------------------

+ 1.0 - Gradle 1.11, V1 Bintray API
+ 0.0.6 - Gradle 1.6, Old Bintray API

Synopsis
========

Adding the plugin
-----------------

```groovy
buildscript {
    repositories {
        jcenter()
	mavenRepo(url: 'http://repository.codehaus.org')
        mavenRepo(url: 'http://dl.bintray.com/ysb33r/grysb33r')
      }
      dependencies {
        classpath 'org.ysb33r.gradle:bintray:1.0'
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

Publishing to Bintray
---------------------

The plugin hooks in the Upload task type. In the below example we
configure the uploadArchives task which is created through the java 
plugin to use Bintray.

```groovy
apply plugin : 'bintray-publish'
apply plugin : 'java'

uploadArchives  {
    repositories {
 
        // Publishing as ivy              
		bintrayIvyDeployer {
			username    'someBintrayUser'
            apiKey      'SomeBinTrayUsersApiKey'
         	repoOwner   'ysb33r'
         	repoName    'grysb33r'
         	packageName 'someNewPackageToBePublished'
            description 'This is an example to simplifying bintray publishing'
            descUrl     'https://github.com/ysb33r/Gradle/blob/master/bintray/README.md'
            tags        'gradle','bintray'
        }
       
		// Publishing as maven
		bintrayMavenDeployer {
			username    'someBintrayUser'
            apiKey      'SomeBinTrayUsersApiKey'
         	repoOwner   'ysb33r'
         	repoName    'grysb33r'
         	packageName 'someNewPackageToBePublished'
            description 'This is an example to simplifying bintray publishing'
            descUrl     'https://github.com/ysb33r/Gradle/blob/master/bintray/README.md'
            tags        'gradle','bintray'
       }
    }
}

```

Publishing using new Gradle Plublishing mechanism
=================================================

This is not yet supported, but will be in a future version. Currently the publication feature is still in incubation in 1.6 and a number of changes
are expected, so I am holding off for the moment.


Acknowledgements
================

Writing this was not possible without the help of others. The following links were specifically helpful

- http://mrhaki.blogspot.co.uk/2010/09/gradle-goodness-define-short-plugin-id.html
- https://github.com/Ullink/gradle-repositories-plugin/blob/master/src/main/groovy/com/ullink/RepositoriesPlugin.groovy
- https://github.com/bintray/bintray-examples/blob/master/gradle-example/build.gradle

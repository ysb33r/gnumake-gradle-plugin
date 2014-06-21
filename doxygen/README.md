Doxygen Plugin for Gradle
=========================

[Doxygen](http://www.doxygen.org) is a popular documentation tool, especially in the C/C++ world. This plugin makes
it possible to generate documentation for any of the ```Doxygen``` supported languages, if such projects are built with
[Gradle](http://www.gradle.org). There is an existing [Doxygen extension](***) for [Ant](http://ant.apache.org), which already 
makes it possible for Gradle builds to utilise ```Doxygen``` via ```AntBuilder```, however this plugin takes a much more 
_gradlesque_ approach.

Requirements
------------
The plugin currently requires that the ```doxygen``` native binary be available. Any other optional binaries called by
```doygen``` such as ```dot``` also needs to be installed if they are required as part of the documentation build process.
By default Gradle will use the search path to find these binaries, but it is possible to explicitly define locations
 if necessary,
 
Synopsis
--------

To bootstrap the plugin:

```groovy

buildscript { 
  repositories {
	jcenter()
  }  
  dependencies {
    classpath 'org.ysb33r.gradle:doxygen:0.2'
  }
}

apply plugin : 'org.ysb33r.doxygen'
```

This will create a default task called ```doxygen``` which can be configured.

```groovy
doxygen {
    generate_html true
    
    source new File(projectDir,'src/main/cpp')
    source new File(projectDir,'src/main/headers')
}
```

It is also possible to to define custom Doxygen tasks

```groovy
import org.ysb33r.gradle.doxygen.Doxygen

task myAwesomeDoxygenTask (type:Doxygen) {
    generate_latex false
}
```

Configuration
-------------
```Doxygen``` is a [SourceTask](***) and all appropriate operations can be used. 

```groovy
doxygen {
  source 'src/main/cpp'
  exclude 'foo.cpp'
}
```

A pre-configured ```Doxyfile``` template can be supplied. At build-time this template will be copied and appropriate
values supplied via the configuration closure will be substituted. This is done via the ```template``` parameter.

```groovy
doxygen {
  template 'src/main/dox/myDoxyfileTemplate'
}
```

If no template is supplied, the build process will call ```doxygen -g``` to generate a default template.

Output directory is set via ```outputDir```. The default output directory, if not supplied, is ```${buildDir}/docs/doxygen```.

```groovy
doxygen {
  outputDir new File(buildDir,'build/docs')
}
```

*NOTE*: ```output_directory``` is an alias for ```outputDir```)

Most ```Doxyfile``` properties can be used in the configuration closure. By convention all ```Doxyfile``` properties are 
in uppercase, but to keep it gradlesque, they must be lowercase in the configuration closure.

```groovy
doxygen {
  generate_xml true
  file_patterns '*.cpp', '*.cpp'
  html_colorstyle_sat  100
}
```

*NOTE 1*: Multiple items in a ```Doxyfile``` are space separated, but in the configuration script they are specified
comma-separated, just like any other list. The plugin will take care of the translation and also to quote any items
that may contain spaces,

*NOTE 2*: Any property that is boolean should be set using ```true``` or ```false```.


Certain ```Doxyfile``` properties which are treated differently:

* ```DOT_PATH``` - Use ```executables``` closure instead
* ```HHC_LOCATION``` - Use ```executables``` closure instead
* ```IMAGE_PATHS``` - Use ```image_paths``` instead and the plugin will take care of ensuring files and directories are
part of the dependencies of the task
* ```INPUT``` - Use ```source``` and ```sourceDir``` instead.
* ```MSCGEN_PATH``` - Use ```executables``` closure instead
* ```PERL_PATH``` - Use ```executables``` closure instead
* ```PROJECT_NAME``` - ```project.name``` will be used as the default value. If you want to override use ```project_name```
* ```PROJECT_NUMBER``` - ```project.version``` will be used as the default value. If you want to override use ```project_number```


